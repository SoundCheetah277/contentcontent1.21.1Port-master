package com.eightsidedsquare.contentcontent.common.block;

import com.eightsidedsquare.contentcontent.core.ContentBlocks;
import com.eightsidedsquare.contentcontent.core.ContentItems;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PottedFollySeedBlock extends FlowerPotBlock {
   public static final IntProperty AGE = Properties.AGE_2;

   public PottedFollySeedBlock(AbstractBlock.Settings settings) {
      super(Blocks.AIR, settings);
      this.setDefaultState((BlockState)this.getDefaultState().with(AGE, 0));
   }

   protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{AGE});
   }

   public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
      return new ItemStack(ContentItems.FOLLY_SEED);
   }

   public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
      if (hand.equals(Hand.MAIN_HAND) && player.getStackInHand(hand).isEmpty()) {
         world.setBlockState(pos, Blocks.FLOWER_POT.getDefaultState(), 3);
         player.setStackInHand(hand, new ItemStack(ContentItems.FOLLY_SEED));
         return ActionResult.success(world.isClient);
      } else {
         return ActionResult.PASS;
      }
   }

   public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
      if (random.nextFloat() < 0.25F) {
         int age = (Integer)state.get(AGE) + 1;
         world.setBlockState(pos, age > 2 ? ContentBlocks.POTTED_FOLLY_SAPLING.getDefaultState() : (BlockState)state.with(AGE, age), 2);
      }
   }
}
