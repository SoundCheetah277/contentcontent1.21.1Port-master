package com.eightsidedsquare.contentcontent.common.block.mug;

import com.eightsidedsquare.contentcontent.common.entity.PotionMugBlockEntity;
import com.eightsidedsquare.contentcontent.core.ContentBlocks;
import java.util.List;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
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
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PotionMugBlock extends FilledMugBlock implements BlockEntityProvider {
   private final boolean isPilk;

   public PotionMugBlock(AbstractBlock.Settings settings, boolean isPilk) {
      super(settings);
      this.isPilk = isPilk;
   }

   @Nullable
   public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
      return new PotionMugBlockEntity(pos, state);
   }

   @Override
   protected Boolean canFill(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand) {
      ItemStack stack = player.getStackInHand(hand);
      return (stack.isOf(Items.WATER_BUCKET) || stack.isOf(Items.MILK_BUCKET) || stack.getItem() instanceof PotionItem)
              && super.canFill(state, world, pos, player, hand);
   }

   @Override
   protected void fill(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand) {
      ItemStack stack = player.getStackInHand(hand);
      if (stack.getItem() instanceof PotionItem) {
         int level = (Integer)state.get(LEVEL);
         List<StatusEffectInstance> list = PotionContentsComponent.DEFAULT.customEffects();
         this.setFilled(this.asBlock(), level + 1, state, world, pos);
         if (!list.isEmpty() && world.getBlockEntity(pos) instanceof PotionMugBlockEntity blockEntity) {
            blockEntity.addEffects(list);
         }

         player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
      } else if (stack.isOf(Items.MILK_BUCKET)) {
         if (world.getBlockEntity(pos) instanceof PotionMugBlockEntity blockEntity) {
            List<StatusEffectInstance> list = blockEntity.getEffects();
            this.setFilled(ContentBlocks.PILK_MUG, 3, state, world, pos);
            if (world.getBlockEntity(pos) instanceof PotionMugBlockEntity pilkBlockEntity) {
               pilkBlockEntity.addEffects(list);
            }

            player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.BUCKET)));
         }
      } else {
         this.setFilled(this.asBlock(), 3, state, world, pos);
         player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.BUCKET)));
      }
   }

   @Override
   protected SoundEvent getDrinkSound() {
      return this.isPilk ? SoundEvents.ENTITY_WANDERING_TRADER_DRINK_MILK : super.getDrinkSound();
   }

   @Override
   protected void drink(PlayerEntity player, BlockState state, World world, BlockPos pos) {
      if (this.isPilk) {
         player.clearStatusEffects();
      }

      if (world.getBlockEntity(pos) instanceof PotionMugBlockEntity blockEntity) {
         blockEntity.drink(player);
      }

      super.drink(player, state, world, pos);
   }

   @Override
   public BlockState getAppearance(BlockState state, BlockRenderView renderView, BlockPos pos, Direction side, @Nullable BlockState sourceState, @Nullable BlockPos sourcePos) {
      return super.getAppearance(state, renderView, pos, side, sourceState, sourcePos);
   }
}
