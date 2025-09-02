package com.eightsidedsquare.contentcontent.common.block.mug;

import com.eightsidedsquare.contentcontent.common.entity.PotionMugBlockEntity;
import com.eightsidedsquare.contentcontent.core.ContentBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MilkMugBlock extends FilledMugBlock {
   public MilkMugBlock(AbstractBlock.Settings settings) {
      super(settings);
   }

   @Override
   protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
      return null;
   }

   @Override
   protected Boolean canFill(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand) {
      ItemStack stack = player.getStackInHand(hand);
      return (stack.isOf(Items.MILK_BUCKET) || stack.getItem() instanceof PotionItem) && super.canFill(state, world, pos, player, hand);
   }

   @Override
   protected void fill(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand) {
      ItemStack stack = player.getStackInHand(hand);
      if (stack.isOf(Items.MILK_BUCKET)) {
         super.fill(state, world, pos, player, hand);
         player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.BUCKET)));
      } else {
         int level = (Integer)state.get(LEVEL);
         player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
         this.setFilled(ContentBlocks.PILK_MUG, level + 1, state, world, pos);
         if (world.getBlockEntity(pos) instanceof PotionMugBlockEntity blockEntity) {
            PotionContentsComponent potionContents = stack.get(DataComponentTypes.POTION_CONTENTS);
            if (potionContents != null) {
               // You'll need to implement addEffects in your block entity
               ((PotionMugBlockEntity)blockEntity).addEffects(potionContents.customEffects());
            }
         }

         player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
      }
   }

   @Override
   protected SoundEvent getDrinkSound() {
      return SoundEvents.ENTITY_WANDERING_TRADER_DRINK_MILK;
   }

   @Override
   protected void drink(PlayerEntity player, BlockState state, World world, BlockPos pos) {
      super.drink(player, state, world, pos);
      player.clearStatusEffects();
   }
}
