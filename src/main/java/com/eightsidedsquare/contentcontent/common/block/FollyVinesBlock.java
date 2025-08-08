package com.eightsidedsquare.contentcontent.common.block;

import com.eightsidedsquare.contentcontent.core.ContentBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.WorldView;

public class FollyVinesBlock extends AbstractPlantStemBlock {
   public static final VoxelShape SHAPE = Block.createCuboidShape(1.0, 2.0, 1.0, 15.0, 16.0, 15.0);

   public FollyVinesBlock(AbstractBlock.Settings settings) {
      super(settings, Direction.DOWN, SHAPE, false, 0.1);
   }

   @Override
   protected MapCodec<? extends AbstractPlantStemBlock> getCodec() {
      return null;
   }

   protected int getGrowthLength(Random random) {
      return 1;
   }

   protected boolean chooseStemState(BlockState state) {
      return state.isAir();
   }

   protected Block getPlant() {
      return ContentBlocks.FOLLY_VINES_PLANT;
   }

   public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
      return super.canPlaceAt(state, world, pos) || world.getBlockState(pos.up()).isIn(BlockTags.LEAVES);
   }
}
