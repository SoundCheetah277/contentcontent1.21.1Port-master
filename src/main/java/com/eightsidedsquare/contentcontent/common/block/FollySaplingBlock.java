package com.eightsidedsquare.contentcontent.common.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class FollySaplingBlock extends SaplingBlock {
   public FollySaplingBlock(AbstractBlock.Settings settings) {
      super(null, settings);
   }

   public void generate(ServerWorld world, BlockPos pos, BlockState state, Random random) {
      if ((Integer)state.get(STAGE) == 0) {
         super.generate(world, pos, state, random);
      } else {
         FollyTrunkBlock.tryPlace(world, pos, random);
      }
   }
}
