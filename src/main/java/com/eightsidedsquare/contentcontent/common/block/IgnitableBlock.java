package com.eightsidedsquare.contentcontent.common.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import java.util.List;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Triplet;

public abstract class IgnitableBlock extends Block implements Waterloggable {
   public static final BooleanProperty LIT = Properties.LIT;
   public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
   public final boolean handExtinguishable;

   public IgnitableBlock(AbstractBlock.Settings settings, boolean handExtinguishable, boolean litByDefault) {
      super(settings);
      this.handExtinguishable = handExtinguishable;
      this.setDefaultState((BlockState)((BlockState)this.getDefaultState().with(LIT, litByDefault)).with(WATERLOGGED, false));
   }

   public static boolean canBeLit(BlockState state) {
      return state.getBlock() instanceof IgnitableBlock && !(Boolean)state.get(LIT) && !(Boolean)state.get(WATERLOGGED);
   }

   private static void setLit(WorldAccess world, BlockState state, BlockPos pos, boolean lit) {
      world.setBlockState(pos, (BlockState)state.with(LIT, lit), 2);
   }

   private static boolean isLit(BlockState state) {
      return state.getBlock() instanceof IgnitableBlock && (Boolean)state.get(LIT);
   }

   public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
      if ((Boolean)state.get(LIT)) {
         for (Vec3d offset : this.getParticlePositions(state)) {
            this.spawnParticles(world, state, offset.add(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5), random);
         }
      }
   }

   private void spawnParticles(World world, BlockState state, Vec3d vec3d, Random random) {
      float f = random.nextFloat();
      UnmodifiableIterator var6 = this.getParticles(state).iterator();

      while (var6.hasNext()) {
         Triplet<ParticleType, Float, Float> particle = (Triplet<ParticleType, Float, Float>)var6.next();
         if (f < (Float)particle.getB()) {
            world.addParticle(
                    (ParticleEffect)particle.getA(), vec3d.x, vec3d.y, vec3d.z, 0.0, ((Float)particle.getC()).floatValue(), 0.0
            );
         }
      }

      if (f < 0.17F) {
         world.playSound(
                 vec3d.x + 0.5,
                 vec3d.y + 0.5,
                 vec3d.z + 0.5,
                 this.getSound(state),
                 SoundCategory.BLOCKS,
                 1.0F + random.nextFloat(),
                 random.nextFloat() * 0.7F + 0.3F,
                 false
         );
      }
   }

   protected abstract SoundEvent getSound(BlockState var1);

   protected abstract SoundEvent getExtinguishedSound();

   protected abstract List<Vec3d> getParticlePositions(BlockState var1);

   protected abstract ImmutableList<Triplet<ParticleType, Float, Float>> getParticles(BlockState var1);

   public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
      ItemStack stack = player.getStackInHand(hand);
      Random random = world.getRandom();
      if (!isLit(state)) {
         if (canBeLit(state)) {
            if (stack.getItem() instanceof FireChargeItem) {
               setLit(world, state, pos, true);
               world.playSound(null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
               if (!player.isCreative()) {
                  stack.decrement(1);
               }

               return ActionResult.success(world.isClient);
            }

            if (stack.getItem() instanceof FlintAndSteelItem || EnchantmentHelper.get(stack).containsKey(Enchantments.FIRE_ASPECT)) {
               setLit(world, state, pos, true);
               world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
               if (!player.isCreative()) {
                  stack.damage(1, player, p -> p.sendToolBreakStatus(hand));
               }

               return ActionResult.success(world.isClient);
            }
         }
      } else if (this.handExtinguishable && stack.isEmpty() || stack.getItem() instanceof ShovelItem) {
         this.extinguish(player, state, world, pos);
         if (!player.isCreative()) {
            stack.damage(1, player, p -> p.sendToolBreakStatus(hand));
         }

         return ActionResult.success(world.isClient);
      }

      return ActionResult.PASS;
   }

   public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
      if (!(Boolean)state.get(WATERLOGGED) && fluidState.getFluid() == Fluids.WATER) {
         BlockState blockState = (BlockState)state.with(WATERLOGGED, true);
         if ((Boolean)state.get(LIT)) {
            this.extinguish(null, blockState, world, pos);
         } else {
            world.setBlockState(pos, blockState, 3);
         }

         world.scheduleFluidTick(pos, fluidState.getFluid(), fluidState.getFluid().getTickRate(world));
         return true;
      } else {
         return false;
      }
   }

   public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
      if ((Boolean)state.get(WATERLOGGED)) {
         world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
      }

      return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
   }

   public FluidState getFluidState(BlockState state) {
      return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
   }

   public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
      if (!world.isClient && projectile.isOnFire() && canBeLit(state)) {
         setLit(world, state, hit.getBlockPos(), true);
      }
   }

   @Nullable
   public BlockState getPlacementState(ItemPlacementContext ctx) {
      World world = ctx.getWorld();
      BlockPos pos = ctx.getBlockPos();
      FluidState fluidState = world.getFluidState(pos);
      return fluidState.getFluid().equals(Fluids.WATER)
              ? (BlockState)((BlockState)this.getDefaultState().with(WATERLOGGED, true)).with(LIT, false)
              : this.getDefaultState();
   }

   public void extinguish(@Nullable PlayerEntity player, BlockState state, WorldAccess world, BlockPos pos) {
      setLit(world, state, pos, false);
      world.playSound(null, pos, this.getExtinguishedSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
      world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
   }

   public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
      return false;
   }

   protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{LIT, WATERLOGGED});
   }
}
