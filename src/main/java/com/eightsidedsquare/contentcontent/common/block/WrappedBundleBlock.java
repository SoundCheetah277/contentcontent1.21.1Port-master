package com.eightsidedsquare.contentcontent.common.block;

import com.eightsidedsquare.contentcontent.common.entity.WrappedBundleBlockEntity;
import com.eightsidedsquare.contentcontent.core.ContentEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import com.eightsidedsquare.contentcontent.util.DyeableHelper;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
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
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class WrappedBundleBlock extends BlockWithEntity implements Waterloggable {
   private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
   private static final VoxelShape SHAPE = Block.createCuboidShape(3.0, 0.0, 3.0, 13.0, 12.0, 13.0);
   private static final VoxelShape COLLISION_SHAPE = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 7.0, 12.0);

   public WrappedBundleBlock(AbstractBlock.Settings settings) {
      super(settings);
      this.setDefaultState((BlockState)this.getDefaultState().with(WATERLOGGED, false));
   }

   @Override
   protected MapCodec<? extends BlockWithEntity> getCodec() {
      return null;
   }

   public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
      if (world.getBlockEntity(pos) instanceof WrappedBundleBlockEntity entity && entity.getOpenTicks() == 0) {
         entity.setOpenTicks(1);
         world.playSound(
                 null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.ITEM_BUNDLE_DROP_CONTENTS, SoundCategory.BLOCKS, 1.0F, 1.0F
         );
         return ActionResult.SUCCESS;
      } else {
            return super.onUse(state, world, pos, player, hit);
      }
   }

   public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
      return SHAPE;
   }

   public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
      return context instanceof EntityShapeContext entityShapeContext && entityShapeContext.getEntity() instanceof ItemEntity
              ? VoxelShapes.empty()
              : COLLISION_SHAPE;
   }

   public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity player, ItemStack itemStack) {
      if (player != null) {
         float angle = -player.getYaw();
         world.getBlockEntity(pos, ContentEntities.WRAPPED_BUNDLE).ifPresent(entity -> {
            entity.setYaw(player.isSneaking() ? Math.round(angle / 22.5F) * 22.5F : angle);
            ItemStack stack = new ItemStack(Items.BUNDLE);
            stack.set(itemStack.getComponents());
            entity.setBundle(stack);
            entity.setColor(DyeableHelper.getColor(itemStack));
         });
      }

      super.onPlaced(world, pos, state, player, itemStack);
   }

   protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{WATERLOGGED});
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

   @Nullable
   public BlockState getPlacementState(ItemPlacementContext ctx) {
      World world = ctx.getWorld();
      BlockPos pos = ctx.getBlockPos();
      return (BlockState)this.getDefaultState().with(WATERLOGGED, world.getFluidState(pos).getFluid().equals(Fluids.WATER));
   }

   @Nullable
   public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
      return new WrappedBundleBlockEntity(pos, state);
   }

   @Nullable
   public <T extends BlockEntity> BlockEntityTicker<T> checkType(World world, BlockState state, BlockEntityType<T> type) {
      return checkType(type, ContentEntities.WRAPPED_BUNDLE, WrappedBundleBlockEntity::tick);
   }
}
