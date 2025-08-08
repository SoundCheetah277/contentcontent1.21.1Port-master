package com.eightsidedsquare.contentcontent.mixin;

import net.minecraft.block.AzaleaBlock;
import net.minecraft.block.BlockState;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({AzaleaBlock.class})
public class AzaleaBlockMixin {
   @Inject(
      method = {"canPlantOnTop"},
      at = {@At("HEAD")},
      cancellable = true
   )
   protected void canPlantOnTop(BlockState floor, BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
      if (floor.getBlock() instanceof AzaleaBlock || floor.isIn(BlockTags.LEAVES)) {
         cir.setReturnValue(true);
      }
   }
}
