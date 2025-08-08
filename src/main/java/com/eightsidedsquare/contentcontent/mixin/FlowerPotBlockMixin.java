package com.eightsidedsquare.contentcontent.mixin;

import com.eightsidedsquare.contentcontent.core.ContentItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({FlowerPotBlock.class})
public class FlowerPotBlockMixin {
   @Inject(
      method = {"onUse"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onUse(
           BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir
   ) {
      if (player.getStackInHand(hand).isOf(ContentItems.FOLLY_SEED)) {
         cir.setReturnValue(ActionResult.PASS);
      }
   }
}
