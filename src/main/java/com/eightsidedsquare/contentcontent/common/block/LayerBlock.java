package com.eightsidedsquare.contentcontent.common.block;

import net.minecraft.block.*;
import net.minecraft.block.enums.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class LayerBlock extends Block {
   public static final EnumProperty<LayerShape> SHAPE = EnumProperty.of("shape", LayerBlock.LayerShape.class);
   public static final BooleanProperty UP = Properties.UP;
   public static final BooleanProperty NORTH = Properties.NORTH;
   public static final BooleanProperty EAST = Properties.EAST;
   public static final BooleanProperty SOUTH = Properties.SOUTH;
   public static final BooleanProperty WEST = Properties.WEST;

   public LayerBlock(AbstractBlock.Settings settings) {
      super(settings);
      this.setDefaultState(
              (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.getDefaultState().with(SHAPE, LayerBlock.LayerShape.SLAB))
                      .with(UP, true))
                      .with(NORTH, false))
                      .with(EAST, false))
                      .with(SOUTH, false))
                      .with(WEST, false)
      );
   }

   protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{SHAPE, UP, NORTH, EAST, SOUTH, WEST});
   }

   @Nullable
   public BlockState getPlacementState(ItemPlacementContext ctx) {
      BlockState blockState = super.getPlacementState(ctx);
      if (blockState != null) {
         BlockPos pos = ctx.getBlockPos().down();
         BlockState belowState = ctx.getWorld().getBlockState(pos);
         return this.getUpdatedBlockState(blockState, belowState, ctx.getWorld(), pos);
      } else {
         return null;
      }
   }

   public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
      BlockPos downPos = pos.down();
      BlockState newState = state;
      if (world instanceof World) {
         newState = this.getUpdatedBlockState(state, world.getBlockState(downPos), (World)world, downPos);
      }

      return newState != null ? newState : Blocks.AIR.getDefaultState();
   }

   @Nullable
   public BlockState getUpdatedBlockState(BlockState blockState, BlockState belowState, World world, BlockPos pos) {
      if (belowState.getBlock() instanceof SlabBlock) {
         return (BlockState)((BlockState)blockState.with(SHAPE, LayerBlock.LayerShape.SLAB))
                 .with(UP, !((SlabType)belowState.get(Properties.SLAB_TYPE)).equals(SlabType.BOTTOM));
      } else if (belowState.getBlock() instanceof StairsBlock) {
         return ((BlockHalf)belowState.get(Properties.BLOCK_HALF)).equals(BlockHalf.TOP)
                 ? (BlockState)((BlockState)blockState.with(SHAPE, LayerBlock.LayerShape.SLAB)).with(UP, true)
                 : (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)blockState.with(
                 NORTH, this.stairsDirection(belowState, Direction.NORTH)
         ))
                 .with(EAST, this.stairsDirection(belowState, Direction.EAST)))
                 .with(SOUTH, this.stairsDirection(belowState, Direction.SOUTH)))
                 .with(WEST, this.stairsDirection(belowState, Direction.WEST)))
                 .with(SHAPE, LayerBlock.LayerShape.STAIR);
      } else if (belowState.getBlock() instanceof FenceBlock || belowState.getBlock() instanceof PaneBlock) {
         return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)blockState.with(
                 SHAPE, belowState.getBlock() instanceof FenceBlock ? LayerBlock.LayerShape.FENCE : LayerBlock.LayerShape.PANE
         ))
                 .with(NORTH, (Boolean)belowState.get(Properties.NORTH)))
                 .with(EAST, (Boolean)belowState.get(Properties.EAST)))
                 .with(SOUTH, (Boolean)belowState.get(Properties.SOUTH)))
                 .with(WEST, (Boolean)belowState.get(Properties.WEST));
      } else if (belowState.getBlock() instanceof WallBlock) {
         return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)blockState.with(SHAPE, LayerBlock.LayerShape.WALL))
                 .with(NORTH, !((WallShape)belowState.get(Properties.NORTH_WALL_SHAPE)).equals(WallShape.NONE)))
                 .with(EAST, !((WallShape)belowState.get(Properties.EAST_WALL_SHAPE)).equals(WallShape.NONE)))
                 .with(SOUTH, !((WallShape)belowState.get(Properties.SOUTH_WALL_SHAPE)).equals(WallShape.NONE)))
                 .with(WEST, !((WallShape)belowState.get(Properties.WEST_WALL_SHAPE)).equals(WallShape.NONE)))
                 .with(UP, (Boolean)belowState.get(Properties.UP));
      } else if (belowState.getBlock() instanceof TrapdoorBlock) {
         return belowState.get(Properties.OPEN)
                 ? (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)blockState.with(
                 NORTH, ((Direction)belowState.get(HorizontalFacingBlock.FACING)).equals(Direction.NORTH.getOpposite())
         ))
                 .with(EAST, ((Direction)belowState.get(HorizontalFacingBlock.FACING)).equals(Direction.EAST.getOpposite())))
                 .with(SOUTH, ((Direction)belowState.get(HorizontalFacingBlock.FACING)).equals(Direction.SOUTH.getOpposite())))
                 .with(WEST, ((Direction)belowState.get(HorizontalFacingBlock.FACING)).equals(Direction.WEST.getOpposite())))
                 .with(SHAPE, LayerBlock.LayerShape.DOOR)
                 : (BlockState)((BlockState)blockState.with(SHAPE, LayerBlock.LayerShape.TRAPDOOR))
                 .with(UP, ((BlockHalf)belowState.get(Properties.BLOCK_HALF)).equals(BlockHalf.TOP));
      } else if (belowState.getBlock() instanceof DoorBlock) {
         Direction d = (Direction)belowState.get(HorizontalFacingBlock.FACING);
         if ((Boolean)belowState.get(Properties.OPEN)) {
            if (((DoorHinge)belowState.get(Properties.DOOR_HINGE)).equals(DoorHinge.LEFT)) {
               d = d.rotateYClockwise();
            } else {
               d = d.rotateYCounterclockwise();
            }
         }

         return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)blockState.with(NORTH, d.equals(Direction.NORTH.getOpposite())))
                 .with(EAST, d.equals(Direction.EAST.getOpposite())))
                 .with(SOUTH, d.equals(Direction.SOUTH.getOpposite())))
                 .with(WEST, d.equals(Direction.WEST.getOpposite())))
                 .with(SHAPE, LayerBlock.LayerShape.DOOR);
      } else if (belowState.getBlock() instanceof FenceGateBlock) {
         return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)blockState.with(
                 NORTH, ((Direction)belowState.get(HorizontalFacingBlock.FACING)).equals(Direction.NORTH.getOpposite())
         ))
                 .with(EAST, ((Direction)belowState.get(HorizontalFacingBlock.FACING)).equals(Direction.EAST.getOpposite())))
                 .with(SOUTH, ((Direction)belowState.get(HorizontalFacingBlock.FACING)).equals(Direction.SOUTH.getOpposite())))
                 .with(WEST, ((Direction)belowState.get(HorizontalFacingBlock.FACING)).equals(Direction.WEST.getOpposite())))
                 .with(UP, !(Boolean)belowState.get(Properties.IN_WALL)))
                 .with(SHAPE, belowState.get(Properties.OPEN) ? LayerBlock.LayerShape.OPEN_FENCE_GATE : LayerBlock.LayerShape.FENCE_GATE);
      } else {
         return !belowState.isSideSolidFullSquare(world, pos, Direction.UP) && !(belowState.getBlock() instanceof LeavesBlock)
                 ? null
                 : (BlockState)((BlockState)blockState.with(SHAPE, LayerBlock.LayerShape.SLAB)).with(UP, true);
      }
   }

   public boolean stairsDirection(BlockState state, Direction direction) {
      if (state.getBlock() instanceof StairsBlock) {
         if (((Direction)state.get(HorizontalFacingBlock.FACING)).equals(direction)
                 && !((StairShape)state.get(Properties.STAIR_SHAPE)).equals(StairShape.OUTER_LEFT)) {
            return true;
         } else if (((Direction)state.get(HorizontalFacingBlock.FACING)).equals(direction.rotateYClockwise())
                 && !((StairShape)state.get(Properties.STAIR_SHAPE)).equals(StairShape.OUTER_RIGHT)) {
            return true;
         } else {
            return ((Direction)state.get(HorizontalFacingBlock.FACING)).equals(direction.rotateYCounterclockwise())
                    && ((StairShape)state.get(Properties.STAIR_SHAPE)).equals(StairShape.INNER_RIGHT)
                    ? true
                    : ((Direction)state.get(HorizontalFacingBlock.FACING)).equals(direction.getOpposite())
                    && ((StairShape)state.get(Properties.STAIR_SHAPE)).equals(StairShape.INNER_LEFT);
         }
      } else {
         return false;
      }
   }

   public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
      BlockState belowState = world.getBlockState(pos.down());
      if (!belowState.isSideSolidFullSquare(world, pos.down(), Direction.UP) && !(belowState.getBlock() instanceof LeavesBlock)) {
         Block b = belowState.getBlock();
         return b instanceof SlabBlock
                 || b instanceof FenceBlock
                 || b instanceof WallBlock
                 || b instanceof StairsBlock
                 || b instanceof TrapdoorBlock
                 || b instanceof DoorBlock
                 || b instanceof FenceGateBlock
                 || b instanceof PaneBlock;
      } else {
         return true;
      }
   }

   public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
      return switch ((LayerBlock.LayerShape)state.get(SHAPE)) {
         case SLAB -> state.get(UP)
                 ? Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0)
                 : Block.createCuboidShape(0.0, -8.0, 0.0, 16.0, -7.0, 16.0);
         case STAIR -> VoxelShapes.union(
                 state.get(WEST) ? Block.createCuboidShape(0.0, 0.0, 0.0, 8.0, 1.0, 8.0) : Block.createCuboidShape(0.0, -8.0, 0.0, 8.0, -7.0, 8.0),
                 new VoxelShape[]{
                         state.get(NORTH) ? Block.createCuboidShape(8.0, 0.0, 0.0, 16.0, 1.0, 8.0) : Block.createCuboidShape(8.0, -8.0, 0.0, 16.0, -7.0, 8.0),
                         state.get(EAST) ? Block.createCuboidShape(8.0, 0.0, 8.0, 16.0, 1.0, 16.0) : Block.createCuboidShape(8.0, -8.0, 8.0, 16.0, -7.0, 16.0),
                         state.get(SOUTH) ? Block.createCuboidShape(0.0, 0.0, 8.0, 8.0, 1.0, 16.0) : Block.createCuboidShape(0.0, -8.0, 8.0, 8.0, -7.0, 16.0)
                 }
         );
         case TRAPDOOR -> state.get(UP)
                 ? Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0)
                 : Block.createCuboidShape(0.0, -13.0, 0.0, 16.0, -12.0, 16.0);
         case DOOR -> state.get(NORTH)
                 ? Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 3.0)
                 : (
                 state.get(EAST)
                         ? Block.createCuboidShape(13.0, 0.0, 0.0, 16.0, 1.0, 16.0)
                         : (
                         state.get(SOUTH)
                                 ? Block.createCuboidShape(0.0, 0.0, 13.0, 16.0, 1.0, 16.0)
                                 : Block.createCuboidShape(0.0, 0.0, 0.0, 3.0, 1.0, 16.0)
                 )
         );
         case WALL -> VoxelShapes.union(
                 state.get(UP) ? Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 1.0, 12.0) : VoxelShapes.empty(),
                 new VoxelShape[]{
                         state.get(WEST) ? Block.createCuboidShape(0.0, -2.0, 5.0, 11.0, -1.0, 11.0) : VoxelShapes.empty(),
                         state.get(EAST) ? Block.createCuboidShape(11.0, -2.0, 5.0, 16.0, -1.0, 11.0) : VoxelShapes.empty(),
                         state.get(NORTH) ? Block.createCuboidShape(5.0, -2.0, 0.0, 11.0, -1.0, 11.0) : VoxelShapes.empty(),
                         state.get(SOUTH) ? Block.createCuboidShape(5.0, -2.0, 11.0, 11.0, -1.0, 16.0) : VoxelShapes.empty()
                 }
         );
         case FENCE -> VoxelShapes.union(
                 Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 1.0, 10.0),
                 new VoxelShape[]{
                         state.get(WEST) ? Block.createCuboidShape(0.0, 0.0, 6.0, 10.0, 1.0, 10.0) : VoxelShapes.empty(),
                         state.get(EAST) ? Block.createCuboidShape(10.0, 0.0, 6.0, 16.0, 1.0, 10.0) : VoxelShapes.empty(),
                         state.get(NORTH) ? Block.createCuboidShape(6.0, 0.0, 0.0, 10.0, 1.0, 10.0) : VoxelShapes.empty(),
                         state.get(SOUTH) ? Block.createCuboidShape(6.0, 0.0, 10.0, 10.0, 1.0, 16.0) : VoxelShapes.empty()
                 }
         );
         case FENCE_GATE, OPEN_FENCE_GATE -> !state.get(NORTH) && !state.get(SOUTH)
                 ? Block.createCuboidShape(6.0, state.get(UP) ? 0.0 : -3.0, 0.0, 10.0, state.get(UP) ? 1.0 : -2.0, 16.0)
                 : Block.createCuboidShape(0.0, state.get(UP) ? 0.0 : -3.0, 6.0, 16.0, state.get(UP) ? 1.0 : -2.0, 10.0);
         case PANE -> VoxelShapes.union(
                 Block.createCuboidShape(7.0, 0.0, 7.0, 9.0, 1.0, 9.0),
                 new VoxelShape[]{
                         state.get(WEST) ? Block.createCuboidShape(0.0, 0.0, 7.0, 9.0, 1.0, 9.0) : VoxelShapes.empty(),
                         state.get(EAST) ? Block.createCuboidShape(9.0, 0.0, 7.0, 16.0, 1.0, 9.0) : VoxelShapes.empty(),
                         state.get(NORTH) ? Block.createCuboidShape(7.0, 0.0, 0.0, 9.0, 1.0, 9.0) : VoxelShapes.empty(),
                         state.get(SOUTH) ? Block.createCuboidShape(7.0, 0.0, 9.0, 9.0, 1.0, 16.0) : VoxelShapes.empty()
                 }
         );
      };
   }

   public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
      return state.getFluidState().isEmpty();
   }

   public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
      return VoxelShapes.empty();
   }

   public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
      return true;
   }

   public static enum LayerShape implements StringIdentifiable {
      SLAB("slab"),
      STAIR("stair"),
      WALL("wall"),
      FENCE("fence"),
      TRAPDOOR("trapdoor"),
      DOOR("door"),
      FENCE_GATE("fence_gate"),
      OPEN_FENCE_GATE("open_fence_gate"),
      PANE("pane");

      private final String name;

      private LayerShape(String name) {
         this.name = name;
      }

      public String asString() {
         return this.name;
      }

      @Override
      public String toString() {
         return this.name;
      }
   }
}
