package com.eightsidedsquare.contentcontent.common.block;

import com.eightsidedsquare.contentcontent.client.ContentClient;
import com.eightsidedsquare.contentcontent.core.ContentBlocks;
import com.google.common.collect.ImmutableList;
import java.util.List;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Triplet;

public class BrazierBlock extends IgnitableBlock {
   private static final BooleanProperty LEGS = BooleanProperty.of("legs");
   private static final BooleanProperty CHAIN = BooleanProperty.of("chain");

   public BrazierBlock(AbstractBlock.Settings settings) {
      super(settings, false, true);
      this.setDefaultState((BlockState)((BlockState)super.getDefaultState().with(LEGS, true)).with(CHAIN, false));
   }

   public static int getLuminance(BlockState state) {
      return state.get(LIT) ? 15 : 0;
   }

   @Override
   protected SoundEvent getSound(BlockState state) {
      return SoundEvents.BLOCK_CAMPFIRE_CRACKLE;
   }

   @Override
   protected SoundEvent getExtinguishedSound() {
      return SoundEvents.BLOCK_FIRE_EXTINGUISH;
   }

   @Nullable
   @Override
   public BlockState getPlacementState(ItemPlacementContext ctx) {
      World world = ctx.getWorld();
      BlockPos pos = ctx.getBlockPos();
      BlockState state = super.getPlacementState(ctx);
      return state != null
              ? (BlockState)((BlockState)state.with(LEGS, this.canHaveLegs(world, pos))).with(CHAIN, this.canHaveChain(world, pos))
              : null;
   }

   public boolean canHaveChain(WorldAccess world, BlockPos pos) {
      return Block.sideCoversSmallSquare(world, pos.up(), Direction.DOWN)
              || world.getBlockState(pos.up()).isOf(ContentBlocks.STEEL_BAR);
   }

   public boolean canHaveLegs(WorldAccess world, BlockPos pos) {
      BlockState neighborState = world.getBlockState(pos.down());
      return neighborState.isSideSolidFullSquare(world, pos.down(), Direction.UP);
   }

   @Override
   public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
      if (direction.equals(Direction.UP)) {
         return (BlockState)super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos).with(CHAIN, this.canHaveChain(world, pos));
      } else {
         return direction.equals(Direction.DOWN)
                 ? (BlockState)super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos).with(LEGS, this.canHaveLegs(world, pos))
                 : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
      }
   }

   @Override
   protected List<Vec3d> getParticlePositions(BlockState state) {
      return ImmutableList.of(new Vec3d(0.0, 0.5, 0.0));
   }

   @Override
   protected ImmutableList<Triplet<SimpleParticleType, Float, Float>> getParticles(BlockState state) {
      return ImmutableList.of(new Triplet(ParticleTypes.CAMPFIRE_COSY_SMOKE, 0.4F, 0.07F), new Triplet(ContentClient.EMBER, 1.0F, 0.0F));
   }

   public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
      return VoxelShapes.combineAndSimplify(
              Block.createCuboidShape(1.0, state.get(LEGS) ? 0.0 : 5.0, 1.0, 15.0, 9.0, 15.0),
              Block.createCuboidShape(3.0, 7.0, 3.0, 13.0, 9.0, 13.0),
              BooleanBiFunction.ONLY_FIRST
      );
   }

   protected int getDamage() {
      return 1;
   }

   public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
      if (!entity.isFireImmune()
              && state.get(LIT)
              && entity instanceof LivingEntity
              && !EnchantmentHelper.hasFrostWalker((LivingEntity)entity)
              && entity.getY() > pos.getY() + 0.4) {
         entity.damage(world.getDamageSources().inFire(), this.getDamage());
      }

      super.onEntityCollision(state, world, pos, entity);
   }

   @Override
   protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{LIT, WATERLOGGED, LEGS, CHAIN});
   }
}
