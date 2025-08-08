package com.eightsidedsquare.contentcontent.common.item;

import com.eightsidedsquare.contentcontent.core.ContentBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FollySeedItem extends Item {
   public FollySeedItem(Item.Settings settings) {
      super(settings);
   }

   public ActionResult useOnBlock(ItemUsageContext ctx) {
      BlockPos pos = ctx.getBlockPos();
      World world = ctx.getWorld();
      BlockState state = world.getBlockState(pos);
      PlayerEntity player = ctx.getPlayer();
      if (state.isOf(Blocks.FLOWER_POT)) {
         if (player != null && !player.getAbilities().creativeMode) {
            ctx.getStack().decrement(1);
         }

         world.setBlockState(pos, ContentBlocks.POTTED_FOLLY_SEED.getDefaultState(), 3);
         return ActionResult.success(world.isClient);
      } else {
         return super.useOnBlock(ctx);
      }
   }
}
