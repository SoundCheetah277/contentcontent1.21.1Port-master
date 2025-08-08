package com.eightsidedsquare.contentcontent.common.block;

import com.eightsidedsquare.contentcontent.core.ContentSounds;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;

public class StickshiftBlock extends WallMountedBlock {
   protected static final VoxelShape NORTH_WALL_SHAPE = Block.createCuboidShape(5.0, 4.0, 10.0, 11.0, 12.0, 16.0);
   protected static final VoxelShape SOUTH_WALL_SHAPE = Block.createCuboidShape(5.0, 4.0, 0.0, 11.0, 12.0, 6.0);
   protected static final VoxelShape WEST_WALL_SHAPE = Block.createCuboidShape(10.0, 4.0, 5.0, 16.0, 12.0, 11.0);
   protected static final VoxelShape EAST_WALL_SHAPE = Block.createCuboidShape(0.0, 4.0, 5.0, 6.0, 12.0, 11.0);
   protected static final VoxelShape FLOOR_Z_AXIS_SHAPE = Block.createCuboidShape(5.0, 0.0, 4.0, 11.0, 6.0, 12.0);
   protected static final VoxelShape FLOOR_X_AXIS_SHAPE = Block.createCuboidShape(4.0, 0.0, 5.0, 12.0, 6.0, 11.0);
   protected static final VoxelShape CEILING_Z_AXIS_SHAPE = Block.createCuboidShape(5.0, 10.0, 4.0, 11.0, 16.0, 12.0);
   protected static final VoxelShape CEILING_X_AXIS_SHAPE = Block.createCuboidShape(4.0, 10.0, 5.0, 12.0, 16.0, 11.0);
   private static final IntProperty POWER = IntProperty.of("power", 0, 6);
   private static final BooleanProperty BAD_TO_THE_BONE = BooleanProperty.of("bad_to_the_bone");

   public StickshiftBlock(AbstractBlock.Settings settings) {
      super(settings);
      this.setDefaultState(
              (BlockState)((BlockState)((BlockState)((BlockState)this.getDefaultState().with(BAD_TO_THE_BONE, false))
                      .with(FACING, Direction.NORTH))
                      .with(FACE, BlockFace.WALL))
                      .with(POWER, 0)
      );
   }

   @Override
   protected MapCodec<? extends WallMountedBlock> getCodec() {
      return null;
   }

   private static void spawnParticles(BlockState state, WorldAccess world, BlockPos pos, float alpha) {
      Direction direction = ((Direction)state.get(FACING)).getOpposite();
      Direction direction2 = getDirection(state).getOpposite();
      world.addParticle(
              new DustParticleEffect(DustParticleEffect.RED, alpha),
              pos.getX() + 0.5 + 0.1 * direction.getOffsetX() + 0.2 * direction2.getOffsetX(),
              pos.getY() + 0.5 + 0.1 * direction.getOffsetY() + 0.2 * direction2.getOffsetY(),
              pos.getZ() + 0.5 + 0.1 * direction.getOffsetZ() + 0.2 * direction2.getOffsetZ(),
              0.0,
              0.0,
              0.0
      );
   }

   public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
      return switch ((BlockFace)state.get(FACE)) {
         case FLOOR -> ((Direction)state.get(FACING)).getAxis().equals(Direction.Axis.X)
                 ? FLOOR_X_AXIS_SHAPE
                 : FLOOR_Z_AXIS_SHAPE;
         case WALL -> {
            switch ((Direction)state.get(FACING)) {
               case EAST:
                  yield EAST_WALL_SHAPE;
               case WEST:
                  yield WEST_WALL_SHAPE;
               case SOUTH:
                  yield SOUTH_WALL_SHAPE;
               default:
                  yield NORTH_WALL_SHAPE;
            }
         }
         default -> ((Direction)state.get(FACING)).getAxis().equals(Direction.Axis.X) ? CEILING_X_AXIS_SHAPE : CEILING_Z_AXIS_SHAPE;
      };
   }

   public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
      return getDirection(state) == direction ? this.getWeakRedstonePower(state, world, pos, direction) : 0;
   }

   public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
      return (Integer)state.get(POWER) * 2;
   }

   public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
      if (random.nextFloat() < 0.05F * ((Integer)state.get(POWER)).intValue()) {
         spawnParticles(state, world, pos, 0.5F);
      }
   }

   public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
      boolean sneaking = player.isSneaking();
      ItemStack stack = player.getStackInHand(hand);
      if (stack.isOf(Items.SKELETON_SKULL) && !(Boolean)state.get(BAD_TO_THE_BONE)) {
         if (world.isClient) {
            return ActionResult.SUCCESS;
         } else {
            if (!player.isCreative()) {
               stack.decrement(1);
            }

            world.setBlockState(pos, (BlockState)state.with(BAD_TO_THE_BONE, true), 3);
            world.playSound(
                    null, pos, ContentSounds.BAD_TO_THE_BONE, SoundCategory.BLOCKS, 1.0F, 0.8F + ((Integer)state.get(POWER)).intValue() * 0.2F
            );
            return ActionResult.CONSUME;
         }
      } else if ((!sneaking || (Integer)state.get(POWER) != 0) && (sneaking || (Integer)state.get(POWER) != 6)) {
         if (world.isClient) {
            return ActionResult.SUCCESS;
         } else {
            this.cyclePower(state, world, pos, sneaking);
            float f = sneaking ? 0.6F : 0.5F;
            world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, f);
            world.emitGameEvent(player, sneaking ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos);
            return ActionResult.CONSUME;
         }
      } else {
         return ActionResult.PASS;
      }
   }

   public void cyclePower(BlockState state, World world, BlockPos pos, boolean sneaking) {
      int power = (Integer)state.get(POWER);
      power = Math.max(Math.min(power + (sneaking ? -1 : 1), 6), 0);
      state = (BlockState)state.with(POWER, power);
      world.setBlockState(pos, state, 3);
      this.updateNeighbors(state, world, pos);
   }

   public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
      if (!moved && !state.isOf(newState.getBlock())) {
         if ((Integer)state.get(POWER) > 0) {
            this.updateNeighbors(state, world, pos);
         }

         super.onStateReplaced(state, world, pos, newState, moved);
      }
   }

   public boolean emitsRedstonePower(BlockState state) {
      return true;
   }

   private void updateNeighbors(BlockState state, World world, BlockPos pos) {
      world.updateNeighborsAlways(pos, this);
      world.updateNeighborsAlways(pos.offset(getDirection(state).getOpposite()), this);
   }

   protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, FACE, POWER, BAD_TO_THE_BONE});
   }
}
