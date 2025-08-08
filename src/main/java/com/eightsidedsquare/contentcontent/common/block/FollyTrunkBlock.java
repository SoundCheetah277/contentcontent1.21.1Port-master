package com.eightsidedsquare.contentcontent.common.block;

import com.eightsidedsquare.contentcontent.core.ContentBlocks;
import com.eightsidedsquare.contentcontent.core.tags.ContentBlockTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class FollyTrunkBlock extends Block {
   private static final IntProperty AGE = Properties.AGE_3;
   private static final int MAX_CHECKS = 64;
   private static final UniformIntProvider BRANCH_LENGTH = UniformIntProvider.create(2, 5);
   private static final UniformIntProvider BRANCH_HEIGHT = UniformIntProvider.create(3, 4);
   private static final UniformIntProvider LEAF_NODE_RADIUS = UniformIntProvider.create(2, 3);
   private static final UniformIntProvider VINE_LENGTH = UniformIntProvider.create(1, 6);
   private static final UniformIntProvider FIRST_HEIGHT = UniformIntProvider.create(5, 8);

   public FollyTrunkBlock(AbstractBlock.Settings settings) {
      super(settings);
      this.setDefaultState((BlockState)this.getDefaultState().with(AGE, 0));
   }

   protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{AGE});
   }

   public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
      int age = (Integer)state.get(AGE);
      if (age == 3) {
         this.grow(state, world, pos, random);
      } else {
         for (int i = 0; i < 10; i++) {
            BlockPos grassPos = pos.add(random.nextBetween(-5, 5), random.nextBetween(-2, 0), random.nextBetween(-5, 5));
            if (world.getBlockState(grassPos).isOf(Blocks.GRASS_BLOCK)) {
               world.breakBlock(grassPos, false);
               world.setBlockState(grassPos, (random.nextBoolean() ? Blocks.COARSE_DIRT : Blocks.ROOTED_DIRT).getDefaultState());
               world.setBlockState(pos, (BlockState)state.with(AGE, age + 1));
               return;
            }
         }
      }
   }

   private void grow(BlockState state, ServerWorld world, BlockPos pos, Random random) {
      BlockPos cursorPos = pos.mutableCopy();
      BlockPos previousPos = pos.mutableCopy();
      BlockState cursorState = state;
      int i = 0;

      while (i < 64) {
         if (++i > 1 && !FollyLogBlock.isNaturalFollyLog(cursorState)) {
            break;
         }

         Direction d = getOffset(world, new BlockPos(cursorPos), random, previousPos);
         if (d == null) {
            d = getBranchDirection(world, new BlockPos(cursorPos), random);
            if (d != null && tryPlaceBranch(world, new BlockPos(cursorPos), random, d)) {
               world.setBlockState(pos, this.getDefaultState());
            }
            break;
         }

         previousPos = new BlockPos(cursorPos);
         cursorPos = cursorPos.offset(d);
         cursorState = world.getBlockState(cursorPos);
      }
   }

   private static boolean tryPlaceBranch(ServerWorld world, BlockPos pos, Random random, Direction d) {
      int length = BRANCH_LENGTH.get(random);
      int height = BRANCH_HEIGHT.get(random);

      for (int i = 1; i <= length; i++) {
         if (!canReplace(world.getBlockState(pos.offset(d, i)))) {
            return false;
         }
      }

      for (int ix = 1; ix <= height; ix++) {
         if (!canReplace(world.getBlockState(pos.offset(d, length).up(ix)))) {
            return false;
         }
      }

      for (int ixx = 1; ixx <= length; ixx++) {
         world.setBlockState(
                 pos.offset(d, ixx),
                 (BlockState)((BlockState)(ixx == length ? ContentBlocks.FOLLY_WOOD : ContentBlocks.FOLLY_LOG)
                         .getDefaultState()
                         .with(FollyLogBlock.NATURAL, true))
                         .with(Properties.AXIS, d.getAxis())
         );
      }

      for (int ixx = 1; ixx <= height; ixx++) {
         world.setBlockState(
                 pos.offset(d, length).up(ixx),
                 (BlockState)((BlockState)ContentBlocks.FOLLY_LOG.getDefaultState().with(FollyLogBlock.NATURAL, true))
                         .with(Properties.AXIS, Direction.Axis.Y)
         );
      }

      placeLeafNode(pos.offset(d, length).up(height), world, random);
      return true;
   }

   @Nullable
   private static Direction getOffset(ServerWorld world, BlockPos pos, Random random, BlockPos previousPos) {
      if (FollyLogBlock.isNaturalFollyLog(world.getBlockState(pos.up()))) {
         return Direction.UP;
      } else {
         BlockState state = world.getBlockState(pos);
         return state.isOf(ContentBlocks.FOLLY_LOG)
                 && !((Direction.Axis)state.get(Properties.AXIS)).isHorizontal()
                 && !Direction.Type.HORIZONTAL.stream().allMatch(d -> FollyLogBlock.isNaturalFollyLog(world.getBlockState(pos.offset(d))))
                 ? null
                 : (Direction) Util.getRandomOrEmpty(
                         Direction.Type.HORIZONTAL
                                 .stream()
                                 .filter(d -> !pos.offset(d).equals(previousPos))
                                 .filter(d -> FollyLogBlock.isNaturalFollyLog(world.getBlockState(pos.offset(d))))
                                 .toList(),
                         random
                 )
                 .orElse(null);
      }
   }

   @Nullable
   private static Direction getBranchDirection(ServerWorld world, BlockPos pos, Random random) {
      return Direction.Type.HORIZONTAL.getShuffled(random).stream().filter(d -> canReplace(world.getBlockState(pos.offset(d)))).findFirst().orElse(null);
   }

   private static void placeLeafNode(BlockPos pos, ServerWorld world, Random random) {
      int r = LEAF_NODE_RADIUS.get(random);
      BlockPos.stream(pos.add(-r, -1, -r), pos.add(r, 1, r))
              .map(blockPos -> placeLeaf(blockPos, world, random))
              .filter(Objects::nonNull)
              .forEach(blockPos -> placeVines(blockPos, world, random));
      BlockPos.stream(pos.add(1 - r, 2, 1 - r), pos.add(r - 1, r, r - 1)).forEach(blockPos -> placeLeaf(blockPos, world, random));
   }

   @Nullable
   private static BlockPos placeLeaf(BlockPos pos, ServerWorld world, Random random) {
      if (canReplace(world.getBlockState(pos)) && random.nextFloat() < 0.925F) {
         world.setBlockState(pos, ContentBlocks.FOLLY_LEAVES.getDefaultState());
         return pos;
      } else {
         return null;
      }
   }

   private static void placeVines(BlockPos pos, ServerWorld world, Random random) {
      if (random.nextFloat() < 0.075F) {
         int length = VINE_LENGTH.get(random);

         for (int i = 1; i <= length; i++) {
            BlockPos vinePos = pos.down(i);
            if (!world.getBlockState(vinePos).isAir()) {
               break;
            }

            world.setBlockState(vinePos, (BlockState)ContentBlocks.FOLLY_VINES.getDefaultState().with(FollyVinesBlock.AGE, 25));
         }
      }
   }

   private static boolean canReplace(BlockState state) {
      return state.isAir()
              || state.isIn(BlockTags.REPLACEABLE)
              || state.isIn(BlockTags.LEAVES)
              || state.isIn(ContentBlockTags.FOLLY_VINES)
              || state.isOf(ContentBlocks.FOLLY_SAPLING);
   }

   public static void tryPlace(ServerWorld world, BlockPos pos, Random random) {
      int height = FIRST_HEIGHT.get(random);

      for (int i = 0; i < height; i++) {
         if (!canReplace(world.getBlockState(pos.up(i)))) {
            return;
         }
      }

      for (int ix = 0; ix < height; ix++) {
         world.setBlockState(
                 pos.up(ix),
                 ix == 0 ? ContentBlocks.FOLLY_TRUNK.getDefaultState() : (BlockState)ContentBlocks.FOLLY_LOG.getDefaultState().with(FollyLogBlock.NATURAL, true)
         );
      }

      placeLeafNode(pos.up(height - 1), world, random);
   }
}
