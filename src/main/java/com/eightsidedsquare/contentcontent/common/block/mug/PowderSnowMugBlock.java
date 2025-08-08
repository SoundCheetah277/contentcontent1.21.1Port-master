package com.eightsidedsquare.contentcontent.common.block.mug;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PowderSnowMugBlock extends FilledMugBlock {
   public PowderSnowMugBlock(AbstractBlock.Settings settings) {
      super(settings);
   }

   @Override
   protected Boolean canFill(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand) {
      ItemStack stack = player.getStackInHand(hand);
      return stack.isOf(Items.POWDER_SNOW_BUCKET) && super.canFill(state, world, pos, player, hand);
   }

   @Override
   protected void drink(PlayerEntity player, BlockState state, World world, BlockPos pos) {
      super.drink(player, state, world, pos);
      player.setFrozenTicks(300);
      player.damage(world.getDamageSources().freeze(), 1.0F);
   }

   @Override
   protected void fill(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand) {
      super.fill(state, world, pos, player, hand);
      ItemStack stack = player.getStackInHand(hand);
      player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.BUCKET)));
   }

   @Override
   protected SoundEvent getDrinkSound() {
      return SoundEvents.BLOCK_POWDER_SNOW_BREAK;
   }

   @Override
   protected SoundEvent getFillSound() {
      return SoundEvents.ITEM_BUCKET_EMPTY_POWDER_SNOW;
   }

   @Override
   public BlockState getAppearance(BlockState state, BlockRenderView renderView, BlockPos pos, Direction side, @Nullable BlockState sourceState, @Nullable BlockPos sourcePos) {
      return super.getAppearance(state, renderView, pos, side, sourceState, sourcePos);
   }
}
