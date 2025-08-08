package com.eightsidedsquare.contentcontent.common.block;

import com.eightsidedsquare.contentcontent.core.ContentBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Property;

public class FollyLogBlock extends PillarBlock {
   public static final BooleanProperty NATURAL = BooleanProperty.of("natural");

   public FollyLogBlock(AbstractBlock.Settings settings) {
      super(settings);
      this.setDefaultState((BlockState)this.getDefaultState().with(NATURAL, false));
   }

   protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
      super.appendProperties(builder);
      builder.add(new Property[]{NATURAL});
   }

   public static boolean isNaturalFollyLog(BlockState state) {
      return (state.isOf(ContentBlocks.FOLLY_LOG) || state.isOf(ContentBlocks.FOLLY_WOOD)) && (Boolean)state.get(NATURAL);
   }
}
