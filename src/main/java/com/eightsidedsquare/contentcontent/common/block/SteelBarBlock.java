package com.eightsidedsquare.contentcontent.common.block;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class SteelBarBlock extends Block implements Waterloggable {
   public static final EnumProperty<Direction.Axis> AXIS = Properties.AXIS;
   public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
   public static final BooleanProperty CHAIN = BooleanProperty.of("chain");
   public static final BooleanProperty TOP_CAP = BooleanProperty.of("top_cap");
   public static final BooleanProperty BOTTOM_CAP = BooleanProperty.of("bottom_cap");
   protected static final VoxelShape Y_TOP_SHAPE = Block.createCuboidShape(6.0, 14.0, 6.0, 10.0, 16.0, 10.0);
   protected static final VoxelShape Z_TOP_SHAPE = Block.createCuboidShape(6.0, 6.0, 14.0, 10.0, 10.0, 16.0);
   protected static final VoxelShape X_TOP_SHAPE = Block.createCuboidShape(14.0, 6.0, 6.0, 16.0, 10.0, 10.0);
   protected static final VoxelShape Y_BOTTOM_SHAPE = Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 2.0, 10.0);
   protected static final VoxelShape Z_BOTTOM_SHAPE = Block.createCuboidShape(6.0, 6.0, 0.0, 10.0, 10.0, 2.0);
   protected static final VoxelShape X_BOTTOM_SHAPE = Block.createCuboidShape(0.0, 6.0, 6.0, 2.0, 10.0, 10.0);
   private static final VoxelShape Y_SHAPE = Block.createCuboidShape(7.0, 0.0, 7.0, 9.0, 16.0, 9.0);
   private static final VoxelShape Z_SHAPE = Block.createCuboidShape(7.0, 7.0, 0.0, 9.0, 9.0, 16.0);
   private static final VoxelShape X_SHAPE = Block.createCuboidShape(0.0, 7.0, 7.0, 16.0, 9.0, 9.0);
   protected static final VoxelShape TOP_BOTTOM_X_SHAPE = VoxelShapes.union(X_SHAPE, new VoxelShape[]{X_TOP_SHAPE, X_BOTTOM_SHAPE});
   protected static final VoxelShape TOP_X_SHAPE = VoxelShapes.union(X_SHAPE, X_TOP_SHAPE);
   protected static final VoxelShape BOTTOM_X_SHAPE = VoxelShapes.union(X_SHAPE, X_BOTTOM_SHAPE);
   protected static final VoxelShape TOP_BOTTOM_Y_SHAPE = VoxelShapes.union(Y_SHAPE, new VoxelShape[]{Y_TOP_SHAPE, Y_BOTTOM_SHAPE});
   protected static final VoxelShape TOP_Y_SHAPE = VoxelShapes.union(Y_SHAPE, Y_TOP_SHAPE);
   protected static final VoxelShape BOTTOM_Y_SHAPE = VoxelShapes.union(Y_SHAPE, Y_BOTTOM_SHAPE);
   protected static final VoxelShape TOP_BOTTOM_Z_SHAPE = VoxelShapes.union(Z_SHAPE, new VoxelShape[]{Z_TOP_SHAPE, Z_BOTTOM_SHAPE});
   protected static final VoxelShape TOP_Z_SHAPE = VoxelShapes.union(Z_SHAPE, Z_TOP_SHAPE);
   protected static final VoxelShape BOTTOM_Z_SHAPE = VoxelShapes.union(Z_SHAPE, Z_BOTTOM_SHAPE);

   public SteelBarBlock(AbstractBlock.Settings settings) {
      super(settings);
      this.setDefaultState(
              (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.getDefaultState().with(AXIS, Direction.Axis.Y))
                      .with(WATERLOGGED, false))
                      .with(CHAIN, false))
                      .with(TOP_CAP, true))
                      .with(BOTTOM_CAP, true)
      );
   }

   public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
      boolean top = (Boolean)state.get(TOP_CAP);
      boolean bottom = (Boolean)state.get(BOTTOM_CAP);

      return switch ((Direction.Axis)state.get(AXIS)) {
         case X -> top && bottom ? TOP_BOTTOM_X_SHAPE : (top ? TOP_X_SHAPE : (bottom ? BOTTOM_X_SHAPE : X_SHAPE));
         case Y -> top && bottom ? TOP_BOTTOM_Y_SHAPE : (top ? TOP_Y_SHAPE : (bottom ? BOTTOM_Y_SHAPE : Y_SHAPE));
         case Z -> top && bottom ? TOP_BOTTOM_Z_SHAPE : (top ? TOP_Z_SHAPE : (bottom ? BOTTOM_Z_SHAPE : Z_SHAPE));
         default -> throw new IncompatibleClassChangeError();
      };
   }

   public FluidState getFluidState(BlockState state) {
      return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
   }

   @Nullable
   public BlockState getPlacementState(ItemPlacementContext ctx) {
      FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
      boolean bl = fluidState.getFluid() == Fluids.WATER;
      return (BlockState)((BlockState)this.getDefaultState().with(WATERLOGGED, bl)).with(AXIS, ctx.getSide().getAxis());
   }

   protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{AXIS, WATERLOGGED, CHAIN, TOP_CAP, BOTTOM_CAP});
   }

   public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
      if ((Boolean)state.get(WATERLOGGED)) {
         world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
      }

      if (direction.equals(Direction.DOWN)) {
         state = (BlockState)state.with(
                 CHAIN,
                 (
                         neighborState.getBlock() instanceof LanternBlock && (Boolean)neighborState.get(Properties.HANGING)
                                 || neighborState.getBlock() instanceof ChainBlock
                                 && ((Direction.Axis)neighborState.get(Properties.AXIS)).equals(Direction.Axis.Y)
                                 || neighborState.getBlock() instanceof BrazierBlock
                 )
                         && ((Direction.Axis)state.get(AXIS)).isHorizontal()
         );
      }

      if (((Direction.Axis)state.get(AXIS)).equals(direction.getAxis())) {
         boolean bl = neighborState.getBlock() instanceof SteelBarBlock && ((Direction.Axis)neighborState.get(AXIS)).equals(state.get(AXIS));
         int i = direction.getOffsetX() + direction.getOffsetY() + direction.getOffsetZ();
         if (i == 1) {
            state = (BlockState)state.with(TOP_CAP, !bl);
         } else {
            state = (BlockState)state.with(BOTTOM_CAP, !bl);
         }
      }

      return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
   }
}
