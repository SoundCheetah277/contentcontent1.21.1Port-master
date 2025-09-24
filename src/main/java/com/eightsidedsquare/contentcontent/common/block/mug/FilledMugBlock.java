package com.eightsidedsquare.contentcontent.common.block.mug;

import com.eightsidedsquare.contentcontent.core.ContentBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class FilledMugBlock extends MugBlock {
   public static final IntProperty LEVEL = IntProperty.of("level", 1, 3);

   public FilledMugBlock(AbstractBlock.Settings settings) {
      super(settings);
      this.setDefaultState((BlockState)super.getDefaultState().with(LEVEL, 3));
   }

      @Override
      public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
         if (this.canFill(state, world, pos, player, hand)) {
            this.fill(state, world, pos, player, hand);
            if (!world.isClient) {
               world.playSound(null, pos, this.getFillSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
            }

            return ActionResult.success(world.isClient);
         } else if (this.canDrink(state, world, pos, player, hand)) {
            this.drink(player, state, world, pos);
            if (!world.isClient) {
               world.playSound(null, pos, this.getDrinkSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
            }

            return ActionResult.success(world.isClient);
         } else {
            return ActionResult.PASS;
         }
      }

   protected SoundEvent getFillSound() {
      return SoundEvents.ITEM_BOTTLE_EMPTY;
   }

   protected SoundEvent getDrinkSound() {
      return SoundEvents.ENTITY_GENERIC_DRINK;
   }

   protected Boolean canDrink(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand) {
      return true;
   }

   public boolean hasComparatorOutput(BlockState state) {
      return true;
   }

   public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
      return (Integer)state.get(LEVEL);
   }

   protected void fill(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand) {
      this.setFilled(this.asBlock(), 3, state, world, pos);
   }

   protected Boolean canFill(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand) {
      return (Integer)state.get(LEVEL) < 3;
   }

   protected void drink(PlayerEntity player, BlockState state, World world, BlockPos pos) {
      int level = (Integer)state.get(LEVEL);
      if (level == 1) {
         world.setBlockState(pos, ContentBlocks.MUG.getStateWithProperties(state), 3);
      } else {
         world.setBlockState(pos, (BlockState)state.with(LEVEL, level - 1), 3);
      }
   }

   @Override
   protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, LEVEL});
   }
}
