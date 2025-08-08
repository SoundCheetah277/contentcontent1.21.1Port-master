package com.eightsidedsquare.contentcontent.common.block;

import com.eightsidedsquare.contentcontent.common.entity.DisplayCaseBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class DisplayCaseBlock extends WallMountedBlock implements Waterloggable, BlockEntityProvider {
   private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
   private static final VoxelShape DOWN_SHAPE = Block.createCuboidShape(0.0, 13.0, 0.0, 16.0, 16.0, 16.0);
   private static final VoxelShape UP_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 3.0, 16.0);
   private static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 3.0, 16.0, 16.0);
   private static final VoxelShape WEST_SHAPE = Block.createCuboidShape(13.0, 0.0, 0.0, 16.0, 16.0, 16.0);
   private static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 3.0);
   private static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 13.0, 16.0, 16.0, 16.0);

   public DisplayCaseBlock(AbstractBlock.Settings settings) {
      super(settings);
      this.setDefaultState(
              (BlockState)((BlockState)((BlockState)this.getDefaultState().with(FACING, Direction.NORTH))
                      .with(FACE, BlockFace.WALL))
                      .with(WATERLOGGED, false)
      );
   }

   @Override
   protected MapCodec<? extends WallMountedBlock> getCodec() {
      return null;
   }

   public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
      return switch ((BlockFace)state.get(FACE)) {
         case FLOOR -> UP_SHAPE;
         case CEILING -> DOWN_SHAPE;
         case WALL -> {
            switch ((Direction)state.get(FACING)) {
               case NORTH:
                  yield NORTH_SHAPE;
               case EAST:
                  yield EAST_SHAPE;
               case SOUTH:
                  yield SOUTH_SHAPE;
               default:
                  yield WEST_SHAPE;
            }
         }
         default -> throw new IncompatibleClassChangeError();
      };
   }

   public FluidState getFluidState(BlockState state) {
      return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
   }

   public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
      if ((Boolean)state.get(WATERLOGGED)) {
         world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
      }

      return state;
   }

   @Nullable
   public BlockState getPlacementState(ItemPlacementContext ctx) {
      BlockState state = super.getPlacementState(ctx);
      return state != null
              ? (BlockState)state.with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).isOf(Fluids.WATER))
              : null;
   }

   @Nullable
   public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
      return new DisplayCaseBlockEntity(pos, state);
   }

   protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACE, FACING, WATERLOGGED});
   }

   public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
      if (world.getBlockEntity(pos) instanceof DisplayCaseBlockEntity entity) {
         ItemStack heldStack = player.getStackInHand(hand);
         ItemStack displayStack = entity.getStack();
         if (!heldStack.isEmpty() && displayStack.isEmpty()) {
            entity.setStack(heldStack.copy());
            world.updateComparators(pos, this);
            world.playSound(null, pos, SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if (!player.getAbilities().creativeMode) {
               player.setStackInHand(hand, ItemStack.EMPTY);
            }

            return ActionResult.success(world.isClient);
         }

         if (heldStack.isEmpty() && !displayStack.isEmpty()) {
            player.setStackInHand(hand, displayStack.copy());
            entity.setStack(ItemStack.EMPTY);
            world.updateComparators(pos, this);
            world.playSound(null, pos, SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM, SoundCategory.BLOCKS, 1.0F, 1.0F);
            return ActionResult.success(world.isClient);
         }
      }

      return ActionResult.PASS;
   }

   public boolean hasComparatorOutput(BlockState state) {
      return true;
   }

   public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
      if (world.getBlockEntity(pos) instanceof DisplayCaseBlockEntity entity) {
         return entity.getStack().isEmpty() ? 0 : 1;
      } else {
         return 0;
      }
   }

   public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
      if (!state.isOf(newState.getBlock()) && world.getBlockEntity(pos) instanceof DisplayCaseBlockEntity entity && !entity.getStack().isEmpty()) {
         ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), entity.getStack());
         world.updateComparators(pos, this);
         world.playSound(null, pos, SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM, SoundCategory.BLOCKS, 1.0F, 1.0F);
      }

      super.onStateReplaced(state, world, pos, newState, moved);
   }
}
