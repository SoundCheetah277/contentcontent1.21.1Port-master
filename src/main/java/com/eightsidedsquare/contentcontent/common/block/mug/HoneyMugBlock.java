package com.eightsidedsquare.contentcontent.common.block.mug;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HoneyMugBlock extends FilledMugBlock {
   public HoneyMugBlock(AbstractBlock.Settings settings) {
      super(settings);
   }

   @Override
   protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
      return null;
   }

   @Override
   protected Boolean canFill(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand) {
      ItemStack stack = player.getStackInHand(hand);
      return stack.isOf(Items.HONEY_BOTTLE) && super.canFill(state, world, pos, player, hand);
   }

   @Override
   protected SoundEvent getDrinkSound() {
      return SoundEvents.ITEM_HONEY_BOTTLE_DRINK;
   }

   @Override
   protected void drink(PlayerEntity player, BlockState state, World world, BlockPos pos) {
      super.drink(player, state, world, pos);
      FoodComponent honey = FoodComponents.HONEY_BOTTLE;
      player.getHungerManager().add(honey.nutrition(), honey.saturation());
   }

   @Override
   protected void fill(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand) {
      ItemStack stack = player.getStackInHand(hand);
      int level = (Integer)state.get(LEVEL);
      this.setFilled(this.asBlock(), level + 1, state, world, pos);
      player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
   }
}
