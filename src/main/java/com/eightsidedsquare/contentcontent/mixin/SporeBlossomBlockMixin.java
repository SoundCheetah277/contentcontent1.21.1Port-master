package com.eightsidedsquare.contentcontent.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.SporeBlossomBlock;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({SporeBlossomBlock.class})
public class SporeBlossomBlockMixin {
   @Inject(
      method = {"canPlaceAt"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void canPlaceAt(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
      if (world.getBlockState(pos.up()).isIn(BlockTags.LEAVES)) {
         cir.setReturnValue(true);
      }
   }
}
