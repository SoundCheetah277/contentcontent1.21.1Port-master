package com.eightsidedsquare.contentcontent.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.AxeItem;
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
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class BranchBlock extends ConnectingBlock implements Waterloggable {
   private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
   @Nullable
   private final BranchBlock strippedBlock;

   public BranchBlock(AbstractBlock.Settings settings, @Nullable BranchBlock stripped) {
      super(0.25F, settings);
      this.strippedBlock = stripped;
      this.setDefaultState(
              (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)super.getDefaultState().with(WATERLOGGED, false))
                      .with(NORTH, false))
                      .with(EAST, false))
                      .with(SOUTH, false))
                      .with(WEST, false))
                      .with(UP, false))
                      .with(DOWN, false)
      );
   }

   public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
      ItemStack stack = player.getStackInHand(hand);
      if (stack.getItem() instanceof AxeItem && this.strippedBlock != null) {
         if (!world.isClient) {
            world.playSound(null, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
         }

         world.setBlockState(pos, this.strippedBlock.getStateWithProperties(state), 3);
         stack.damage(1, player, p -> p.sendToolBreakStatus(hand));
         return ActionResult.success(world.isClient);
      } else {
         return super.onUse(state, world, pos, player, hand, hit);
      }
   }

   private static BlockState getConnectedState(BlockState state, BlockPos pos, WorldAccess world) {
      for (Direction d : Direction.values()) {
         BlockState neighborState = world.getBlockState(pos.offset(d));
         if (neighborState.getBlock() instanceof BranchBlock && (Boolean)neighborState.get((Property)FACING_PROPERTIES.get(d.getOpposite()))) {
            state = (BlockState)state.with((Property)FACING_PROPERTIES.get(d), true);
         }
      }

      return state;
   }

   public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
      if ((Boolean)state.get(WATERLOGGED)) {
         world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
      }

      return getConnectedState(state, pos, world);
   }

   @Nullable
   public BlockState getPlacementState(ItemPlacementContext ctx) {
      BlockPos pos = ctx.getBlockPos();
      World world = ctx.getWorld();
      FluidState fluidState = world.getFluidState(pos);
      boolean bl = fluidState.getFluid() == Fluids.WATER;
      return (BlockState)((BlockState)getConnectedState(this.getDefaultState(), pos, world).with(WATERLOGGED, bl))
              .with((Property)FACING_PROPERTIES.get(ctx.getSide().getOpposite()), true);
   }

   public FluidState getFluidState(BlockState state) {
      return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
   }

   protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{NORTH, EAST, SOUTH, WEST, UP, DOWN, WATERLOGGED});
   }

   @Override
   protected MapCodec<? extends ConnectingBlock> getCodec() {
      return null;
   }
}
