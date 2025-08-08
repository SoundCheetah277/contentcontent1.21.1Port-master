package com.eightsidedsquare.contentcontent.common.block.mug;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LavaMugBlock extends FilledMugBlock {
   public LavaMugBlock(AbstractBlock.Settings settings) {
      super(settings);
   }

   @Override
   protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
      return null;
   }

   public static int getLight(BlockState state) {
      return (Integer)state.get(LEVEL) * 3;
   }

   @Override
   protected SoundEvent getFillSound() {
      return SoundEvents.ITEM_BUCKET_EMPTY_LAVA;
   }

   @Override
   protected Boolean canFill(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand) {
      ItemStack stack = player.getStackInHand(hand);
      return stack.isOf(Items.LAVA_BUCKET) && super.canFill(state, world, pos, player, hand);
   }

   @Override
   protected SoundEvent getDrinkSound() {
      return SoundEvents.ITEM_HONEY_BOTTLE_DRINK;
   }

   @Override
   protected void fill(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand) {
      super.fill(state, world, pos, player, hand);
      ItemStack stack = player.getStackInHand(hand);
      player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.BUCKET)));
   }

   @Override
   protected void drink(PlayerEntity player, BlockState state, World world, BlockPos pos) {
      super.drink(player, state, world, pos);
      player.setOnFireFromLava();
   }
}
