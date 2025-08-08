package com.eightsidedsquare.contentcontent.mixin;

import com.eightsidedsquare.contentcontent.core.ContentBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.root.AboveRootPlacement;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({AboveRootPlacement.class})
public class AboveRootPlacementMixin {
   @Shadow
   @Final
   private BlockStateProvider aboveRootProvider;

   @Inject(
      method = {"aboveRootProvider"},
      at = {@At("RETURN")},
      cancellable = true
   )
   public void aboveRootProvider(CallbackInfoReturnable<BlockStateProvider> cir) {
      if (this.aboveRootProvider instanceof SimpleBlockStateProvider simpleBlockStateProvider
         && simpleBlockStateProvider.get(Random.create(), new BlockPos(0, 0, 0)).isOf(Blocks.MOSS_CARPET)) {
         cir.setReturnValue(BlockStateProvider.of(ContentBlocks.MOSS_LAYER));
      }
   }
}
