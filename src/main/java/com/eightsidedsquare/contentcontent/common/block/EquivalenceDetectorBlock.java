package com.eightsidedsquare.contentcontent.common.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Objects;

public class EquivalenceDetectorBlock extends PillarBlock {
   private static final BooleanProperty POWERED = Properties.POWERED;

   public EquivalenceDetectorBlock(AbstractBlock.Settings settings) {
      super(settings);
      this.setDefaultState((BlockState)super.getDefaultState().with(POWERED, false));
   }

   protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
      super.appendProperties(builder);
      builder.add(new Property[]{POWERED});
   }

   public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
      Direction.Axis axis = (Direction.Axis)state.get(AXIS);
      if (direction.getAxis().equals(axis)) {
         world.scheduleBlockTick(pos, this, 1);
      }

      return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
   }

   private boolean neighborsAreEqual(Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos) {
      BlockState otherNeighbor = world.getBlockState(pos.offset(direction.getOpposite()));
      return neighborState.isOf(otherNeighbor.getBlock())
              && neighborState.getProperties()
              .stream()
              .allMatch(property -> Objects.equals(neighborState.get(property), otherNeighbor.get(property)));
   }

   public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
      Direction d = switch ((Direction.Axis)state.get(AXIS)) {
         case X -> Direction.EAST;
         case Y -> Direction.UP;
         case Z -> Direction.SOUTH;
         default -> throw new IncompatibleClassChangeError();
      };
      world.setBlockState(pos, (BlockState)state.with(POWERED, this.neighborsAreEqual(d, world.getBlockState(pos.offset(d)), world, pos)), 3);
   }

   public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
      super.onBlockAdded(state, world, pos, oldState, notify);
      world.scheduleBlockTick(pos, this, 1);
   }

   public boolean emitsRedstonePower(BlockState state) {
      return true;
   }

   public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
      return super.getStrongRedstonePower(state, world, pos, direction);
   }

   public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
      return state.get(POWERED) && !direction.getAxis().equals(state.get(AXIS)) ? 15 : 0;
   }
}
