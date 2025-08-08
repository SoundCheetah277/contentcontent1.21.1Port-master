package com.eightsidedsquare.contentcontent.mixin;

import net.minecraft.block.AbstractPlantPartBlock;
import net.minecraft.block.BlockState;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({AbstractPlantPartBlock.class})
public class AbstractPlantPartBlockMixin {
   @Inject(
      method = {"canPlaceAt"},
      at = {@At("RETURN")},
      cancellable = true
   )
   public void canPlaceAt(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
      if (!(Boolean)cir.getReturnValue()
         && state.isIn(BlockTags.CAVE_VINES)
         && world.getBlockState(pos.up()).isIn(BlockTags.LEAVES)) {
         cir.setReturnValue(true);
      }
   }
}
