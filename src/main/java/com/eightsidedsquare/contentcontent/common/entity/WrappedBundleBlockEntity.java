package com.eightsidedsquare.contentcontent.common.entity;

import com.eightsidedsquare.contentcontent.core.ContentEntities;
import com.eightsidedsquare.contentcontent.util.BundleItemUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

public class WrappedBundleBlockEntity extends BlockEntity implements GeoBlockEntity {
   private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

   private int openTicks;
   private int color = 16777215;
   private float yaw;
   private ItemStack bundle = ItemStack.EMPTY;
   private static final int OPEN_ANIMATION_LENGTH = 30;
   private static final int DESPAWN_ANIMATION_LENGTH = 20;
   private static final RawAnimation CLOSED = RawAnimation.begin().thenLoop("animation.wrapped_bundle.closed");
   private static final RawAnimation OPENED = RawAnimation.begin().thenLoop("animation.wrapped_bundle.opened");
   private static final RawAnimation REMOVE = RawAnimation.begin().thenLoop("animation.wrapped_bundle.remove_item");
   private static final RawAnimation OPENING = RawAnimation.begin().thenLoop("animation.wrapped_bundle.opening");
   private static final RawAnimation DESPAWN = RawAnimation.begin().thenLoop("animation.wrapped_bundle.despawn");
   public WrappedBundleBlockEntity(BlockPos pos, BlockState state) {
      super(ContentEntities.WRAPPED_BUNDLE, pos, state);
   }

   public void setOpenTicks(int openTicks) {
      this.openTicks = openTicks;
      this.markDirty();
      if (this.world != null) {
         this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), 3);
      }
   }

   public int getOpenTicks() {
      return this.openTicks;
   }

   public void setYaw(float yaw) {
      this.yaw = yaw;
   }

   public float getYaw() {
      return this.yaw;
   }
   public ItemStack getBundle() {
      return this.bundle;
   }

   public void setBundle(ItemStack bundle) {
      this.bundle = bundle;
   }
   public int getColor() {
      return this.color;
   }

   public void setColor(int color) {
      this.color = color;
   }
   @Override
   protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryManager) {
      super.writeNbt(nbt, registryManager);

      nbt.putInt("OpenTicks", this.openTicks);
      nbt.putInt("Color", this.color);
      nbt.putFloat("Yaw", this.yaw);

      if (!this.bundle.isEmpty()) {
         nbt.put("Bundle", this.bundle.encode(registryManager));
      }
   }

   @Override
   public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryManager) {
      super.readNbt(nbt, registryManager);

      this.openTicks = nbt.getInt("OpenTicks");
      this.color = nbt.getInt("Color");
      this.yaw = nbt.getFloat("Yaw");

      if (nbt.contains("Bundle", NbtElement.COMPOUND_TYPE)) {
         this.bundle = ItemStack.fromNbt(registryManager, nbt.getCompound("Bundle")).orElse(ItemStack.EMPTY);
      } else {
         this.bundle = ItemStack.EMPTY;
      }
   }



   public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
      registrar.add(new AnimationController<>(this, "controller", 0, this::controller));
   }

   @Override
   public AnimatableInstanceCache getAnimatableInstanceCache() {
      return this.factory;
   }

   @Override
   public double getTick(Object o) {
      return this.getWorld() != null ? this.getWorld().getTime() : 0;
   }

   private PlayState controller(AnimationState<WrappedBundleBlockEntity> event) {
      if (this.getOpenTicks() == 0) {
         event.getController().setAnimation(CLOSED);
      } else if (this.getOpenTicks() == 30) {
         event.getController().setAnimation(OPENED);
      } else if (this.getOpenTicks() > 30) {
         event.getController().setAnimation(REMOVE);
      } else if (this.getOpenTicks() > 0) {
         event.getController().setAnimation(OPENING);
      } else {
         event.getController().setAnimation(DESPAWN);
      }

      return PlayState.CONTINUE;
   }

   public NbtCompound toInitialChunkDataNbt() {
      return this.createNbt(world.getRegistryManager());
   }

   @Nullable
   public Packet<ClientPlayPacketListener> toUpdatePacket() {
      return BlockEntityUpdateS2CPacket.create(this);
   }

   public void dropItem(ItemStack stack) {
      if (this.world != null) {
         Block.dropStack(this.world, this.pos, stack);
      }
   }

   public static void tick(World world, BlockPos pos, BlockState state, WrappedBundleBlockEntity entity) {
      int openTicks = entity.getOpenTicks();
      if (openTicks > 0 && openTicks < 30) {
         entity.setOpenTicks(openTicks + 1);
      } else if ((openTicks >= 0 || openTicks <= -20) && openTicks <= 30) {
         if (openTicks == 30) {
            ItemStack stack = entity.getBundle().copy();
            if (stack.isOf(Items.BUNDLE)) {
               float filled = BundleItem.getAmountFilled(stack);
               world.playSound(
                       null,
                       pos.getX() + 0.5,
                       pos.getY() + 0.5,
                       pos.getZ() + 0.5,
                       SoundEvents.ITEM_BUNDLE_REMOVE_ONE,
                       SoundCategory.BLOCKS,
                       1.0F,
                       1.0F
               );
               if (filled > 0.0F) {
                  BundleItemUtil.invokeRemoveFirstStack(stack).ifPresent(entity::dropItem);
                  entity.setBundle(stack);
                  entity.setOpenTicks(openTicks + 6);
               } else {
                  entity.setOpenTicks(-1);
                  entity.setBundle(ItemStack.EMPTY);
                  world.playSound(
                          null,
                          pos.getX() + 0.5,
                          pos.getY() + 0.5,
                          pos.getZ() + 0.5,
                          SoundEvents.ITEM_TRIDENT_RIPTIDE_1,
                          SoundCategory.BLOCKS,
                          1.0F,
                          1.4F
                  );
               }

               entity.markDirty();
               world.updateListeners(pos, state, state, 3);
            }
         } else if (openTicks <= -20) {
            entity.dropItem(new ItemStack(Items.BUNDLE));
            entity.dropItem(new ItemStack(Items.STRING));
            world.playSound(
                    null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 0.9F
            );
            world.setBlockState(pos, (state.get(Properties.WATERLOGGED) ? Blocks.WATER : Blocks.AIR).getDefaultState());
            entity.markRemoved();
         }
      } else {
         entity.setOpenTicks(openTicks - 1);
      }

      if (openTicks == 20) {
         world.playSound(
                 null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.ENTITY_LEASH_KNOT_PLACE, SoundCategory.BLOCKS, 1.0F, 1.5F
         );
      }
   }
}
