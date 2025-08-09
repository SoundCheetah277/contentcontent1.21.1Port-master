package com.eightsidedsquare.contentcontent.common.block.mug;

import com.eightsidedsquare.contentcontent.common.entity.PotionMugBlockEntity;
import com.eightsidedsquare.contentcontent.core.ContentBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.component.type.PotionContentsComponent;
import java.util.List;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.potion.Potion;
import org.jetbrains.annotations.Nullable;

public class MugBlock extends HorizontalFacingBlock {
   private static final VoxelShape SHAPE = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 10.0, 12.0);

   public MugBlock(AbstractBlock.Settings settings) {
      super(settings);
   }

   @Override
   protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
      return null;
   }

   public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
      ItemStack stack = player.getStackInHand(hand);
      if (stack.isOf(Items.LAVA_BUCKET)) {
         player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.BUCKET)));
         this.setFilled(ContentBlocks.LAVA_MUG, 3, state, world, pos);
         if (!world.isClient) {
            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY_LAVA, SoundCategory.BLOCKS, 1.0F, 1.0F);
         }

         return ActionResult.success(world.isClient);
      } else if (stack.isOf(Items.MILK_BUCKET)) {
         player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.BUCKET)));
         this.setFilled(ContentBlocks.MILK_MUG, 3, state, world, pos);
         if (!world.isClient) {
            world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
         }

         return ActionResult.success(world.isClient);
      } else if (stack.getItem() instanceof PotionItem) {
         player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
         this.setFilled(ContentBlocks.POTION_MUG, 1, state, world, pos);
         if (stack.getItem() instanceof PotionItem potionItem) {
            List<StatusEffectInstance> effects = potionItem.getEffects(stack);
            world.getBlockEntity(pos).addEffects(effects);
         }

         if (!world.isClient) {
            world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
         }

         return ActionResult.success(world.isClient);
      } else if (stack.isOf(Items.WATER_BUCKET)) {
         player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.BUCKET)));
         this.setFilled(ContentBlocks.POTION_MUG, 3, state, world, pos);
         if (!world.isClient) {
            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
         }

         return ActionResult.success(world.isClient);
      } else if (stack.isOf(Items.POWDER_SNOW_BUCKET)) {
         player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.BUCKET)));
         this.setFilled(ContentBlocks.POWDER_SNOW_MUG, 3, state, world, pos);
         if (!world.isClient) {
            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY_POWDER_SNOW, SoundCategory.BLOCKS, 1.0F, 1.0F);
         }

         return ActionResult.success(world.isClient);
      } else if (stack.isOf(Items.HONEY_BOTTLE)) {
         player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
         this.setFilled(ContentBlocks.HONEY_MUG, 1, state, world, pos);
         if (!world.isClient) {
            world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
         }

         return ActionResult.success(world.isClient);
      } else {
         ActionResult actionResult = super.onUse(state, world, pos, player, hit);
         return actionResult;
      }
   }

   protected void setFilled(Block block, int level, BlockState state, World world, BlockPos pos) {
      world.setBlockState(pos, (BlockState)block.getStateWithProperties(state).with(FilledMugBlock.LEVEL, level), 3);
   }

   public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
      return SHAPE;
   }

   public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
      return direction.equals(Direction.DOWN) && !this.canPlaceAt(state, world, pos)
              ? Blocks.AIR.getDefaultState()
              : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
   }

   @Nullable
   public BlockState getPlacementState(ItemPlacementContext ctx) {
      return (BlockState)this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing());
   }

   public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
      return Block.sideCoversSmallSquare(world, pos.down(), Direction.UP);
   }

   public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
      return false;
   }

   public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
      return new ItemStack(ContentBlocks.MUG.asItem());
   }

   protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING});
   }
}
