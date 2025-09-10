package com.eightsidedsquare.contentcontent.common.entity;

import com.eightsidedsquare.contentcontent.core.ContentBlocks;
import com.eightsidedsquare.contentcontent.core.ContentEntities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

public class PotionMugBlockEntity extends BlockEntity {
   @Nullable
   private List<StatusEffectInstance> effects;

   public PotionMugBlockEntity(BlockPos pos, BlockState state) {
      super(ContentEntities.POTION_MUG, pos, state);
   }

   public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
      return this.createNbt(registryLookup);
   }

   @Nullable
   public Packet<ClientPlayPacketListener> toUpdatePacket() {
      return BlockEntityUpdateS2CPacket.create(this);
   }

   protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
      super.writeNbt(nbt, registryLookup);
      NbtList nbtList = new NbtList();
      if (this.effects != null && !this.effects.isEmpty()) {
         for (StatusEffectInstance effect : this.effects) {
            nbtList.add(effect.writeNbt());
         }
      }

      nbt.put("CustomPotionEffects", nbtList);
   }

   @Override
   protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
      super.readNbt(nbt, registryLookup);
      this.effects = new LinkedList<>();
      List<StatusEffectInstance> customEffects = new ArrayList<>();
      if (nbt.contains("custom_potion_effects", 9)) { // 9 = LIST NBT type
         NbtList effectsNbt = nbt.getList("custom_potion_effects", 10); // 10 = COMPOUND NBT type
         for (int i = 0; i < effectsNbt.size(); i++) {
            StatusEffectInstance effect = StatusEffectInstance.fromNbt(effectsNbt.getCompound(i));
            if (effect != null) customEffects.add(effect);
         }
      }

      this.effects.addAll(customEffects);
      List<StatusEffectInstance> effectsFromNbt = new ArrayList<>();
      if (nbt.contains("custom_potion_effects", NbtElement.LIST_TYPE)) {
         NbtList effectsList = nbt.getList("custom_potion_effects", NbtElement.COMPOUND_TYPE);
         for (int i = 0; i < effectsList.size(); i++) {
            StatusEffectInstance effect = StatusEffectInstance.fromNbt(effectsList.getCompound(i));
            if (effect != null) {
               effectsFromNbt.add(effect);
            }
         }
      }
      this.effects.addAll(effectsFromNbt);
   }

   @Nullable
   public List<StatusEffectInstance> getEffects() {
      return this.effects;
   }

   public int getColor() {
      if (this.world == null) {
         return 16777215;
      } else if (this.effects != null && !this.effects.isEmpty()) {
         return PotionContentsComponent.getColor(this.effects);
      } else {
         return this.world.getBlockState(this.pos).isOf(ContentBlocks.PILK_MUG)
                 ? 16777215
                 : this.world.getColor(this.pos, BiomeColors.WATER_COLOR);
      }
   }

   public void addEffects(List<StatusEffectInstance> list) {
      if (this.effects != null && !this.effects.isEmpty()) {
         for (StatusEffectInstance newEffect : list) {
            this.effects = Stream.concat(
                            this.effects
                                    .stream()
                                    .filter(
                                            effect -> !newEffect.getEffectType().equals(effect.getEffectType())
                                                    || newEffect.getAmplifier() <= effect.getAmplifier() && newEffect.getDuration() <= effect.getDuration()
                                    ),
                            Stream.of(newEffect)
                    )
                    .toList();
         }

         this.markDirty();
         if (this.world != null) {
            this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), 3);
         }
      } else {
         this.effects = list;
         this.markDirty();
      }
   }

   public void drink(PlayerEntity player) {
      if (this.effects != null) {
         this.effects
                 .stream()
                 .map(
                         effect -> new StatusEffectInstance(
                                 effect.getEffectType(),
                                 (int)(effect.getDuration() * 1.0F / 3.0F),
                                 effect.getAmplifier(),
                                 effect.isAmbient(),
                                 effect.shouldShowParticles(),
                                 effect.shouldShowIcon()
                         )
                 )
                 .forEach(player::addStatusEffect);
         this.effects = this.effects
                 .stream()
                 .map(
                         effect -> new StatusEffectInstance(
                                 effect.getEffectType(),
                                 (int)(effect.getDuration() * 2.0F / 3.0F),
                                 Math.max(0, effect.getAmplifier() - 1),
                                 effect.isAmbient(),
                                 effect.shouldShowParticles(),
                                 effect.shouldShowIcon()
                         )
                 )
                 .toList();
         this.markDirty();
         if (this.world != null) {
            this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), 3);
         }
      }
   }

   public static int getColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex) {
      if (world != null && world.getBlockEntity(pos) instanceof PotionMugBlockEntity blockEntity) {
         return blockEntity.getColor();
      } else {
         return world != null ? world.getColor(pos, BiomeColors.WATER_COLOR) : 16777215;
      }
   }
}