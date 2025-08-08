package com.eightsidedsquare.contentcontent.common.block;

import com.eightsidedsquare.contentcontent.core.ContentBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.WorldView;

public class FollyVinesPlantBlock extends AbstractPlantBlock {
   public static final VoxelShape SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

   public FollyVinesPlantBlock(AbstractBlock.Settings settings) {
      super(settings, Direction.DOWN, SHAPE, false);
   }

   protected AbstractPlantStemBlock getStem() {
      return (AbstractPlantStemBlock)ContentBlocks.FOLLY_VINES;
   }

   public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
      return super.canPlaceAt(state, world, pos) || world.getBlockState(pos.up()).isIn(BlockTags.LEAVES);
   }

   @Override
   protected MapCodec<? extends AbstractPlantBlock> getCodec() {
      return null;
   }
}
