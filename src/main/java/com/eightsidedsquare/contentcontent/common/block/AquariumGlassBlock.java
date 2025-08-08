package com.eightsidedsquare.contentcontent.common.block;

import com.eightsidedsquare.contentcontent.core.ContentBlocks;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class AquariumGlassBlock extends Block implements Waterloggable {
   private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
   private static final DirectionProperty FACING = Properties.FACING;
   private static final VoxelShape DOWN_SHAPE = Block.createCuboidShape(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);
   private static final VoxelShape UP_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
   private static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
   private static final VoxelShape WEST_SHAPE = Block.createCuboidShape(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
   private static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
   private static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
   private static final VoxelShape THICK_DOWN_SHAPE = Block.createCuboidShape(0.0, 12.0, 0.0, 16.0, 16.0, 16.0);
   private static final VoxelShape THICK_UP_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);
   private static final VoxelShape THICK_EAST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 4.0, 16.0, 16.0);
   private static final VoxelShape THICK_WEST_SHAPE = Block.createCuboidShape(12.0, 0.0, 0.0, 16.0, 16.0, 16.0);
   private static final VoxelShape THICK_SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 4.0);
   private static final VoxelShape THICK_NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 12.0, 16.0, 16.0, 16.0);

   public AquariumGlassBlock(AbstractBlock.Settings settings) {
      super(settings);
      this.setDefaultState((BlockState)((BlockState)this.getDefaultState().with(FACING, Direction.NORTH)).with(WATERLOGGED, false));
   }

   public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
      if (context instanceof EntityShapeContext ctx
              && ctx.getEntity() instanceof PlayerEntity player
              && (
              player.getStackInHand(Hand.MAIN_HAND).isOf(ContentBlocks.AQUARIUM_GLASS.asItem())
                      || player.getStackInHand(Hand.OFF_HAND).isOf(ContentBlocks.AQUARIUM_GLASS.asItem())
      )) {
         return switch ((Direction)state.get(FACING)) {
            case NORTH -> THICK_NORTH_SHAPE;
            case SOUTH -> THICK_SOUTH_SHAPE;
            case UP -> THICK_UP_SHAPE;
            case DOWN -> THICK_DOWN_SHAPE;
            case EAST -> THICK_EAST_SHAPE;
            case WEST -> THICK_WEST_SHAPE;
            default -> throw new IncompatibleClassChangeError();
         };
      } else {
         return this.getCollisionShape(state, world, pos, context);
      }
   }

   public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
      return switch ((Direction)state.get(FACING)) {
         case NORTH -> NORTH_SHAPE;
         case SOUTH -> SOUTH_SHAPE;
         case UP -> UP_SHAPE;
         case DOWN -> DOWN_SHAPE;
         case EAST -> EAST_SHAPE;
         case WEST -> WEST_SHAPE;
         default -> throw new IncompatibleClassChangeError();
      };
   }

   @Nullable
   public BlockState getPlacementState(ItemPlacementContext ctx) {
      Direction side = ctx.getSide();
      BlockPos pos = ctx.getBlockPos();
      World world = ctx.getWorld();
      PlayerEntity player = ctx.getPlayer();
      BlockState clickedState = world.getBlockState(pos.offset(side.getOpposite()));
      boolean sneaking = player != null && player.isSneaking();
      if (sneaking
              && !player.isSubmergedInWater()
              && world.getBlockState(pos).isOf(Blocks.WATER)
              && world.getFluidState(pos).isOf(Fluids.WATER)) {
         return (BlockState)((BlockState)this.getDefaultState().with(WATERLOGGED, true)).with(FACING, side.getOpposite());
      } else {
         if (!sneaking
                 && clickedState.getBlock() instanceof AquariumGlassBlock
                 && !((Direction)clickedState.get(FACING)).getAxis().equals(side.getAxis())) {
            side = (Direction)clickedState.get(FACING);
         }

         FluidState fluidState = world.getFluidState(pos);
         return (BlockState)((BlockState)this.getDefaultState().with(WATERLOGGED, fluidState.getFluid().equals(Fluids.WATER)))
                 .with(FACING, side);
      }
   }

   public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
      return VoxelShapes.empty();
   }

   public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
      if (stateFrom.isOf(this)) {
         if (((Direction)stateFrom.get(FACING)).equals(direction)) {
            return ((Direction)state.get(FACING)).equals(direction.getOpposite());
         }

         if (((Direction)stateFrom.get(FACING)).equals(direction.getOpposite())) {
            return ((Direction)state.get(FACING)).equals(direction);
         }

         if (((Direction)stateFrom.get(FACING)).equals(state.get(FACING))) {
            return true;
         }
      }

      return super.isSideInvisible(state, stateFrom, direction);
   }

   public FluidState getFluidState(BlockState state) {
      return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
   }

   public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
      return false;
   }

   public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
      if ((Boolean)state.get(WATERLOGGED)) {
         world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
      }

      return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
   }

   protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, WATERLOGGED});
   }
}
