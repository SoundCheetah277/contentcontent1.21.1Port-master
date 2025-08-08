package com.eightsidedsquare.contentcontent.mixin.integration;

import com.eightsidedsquare.contentcontent.common.block.AquariumGlassBlock;
import com.eightsidedsquare.contentcontent.core.ContentMod;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.FluidRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
   value = {FluidRenderer.class},
   remap = false
)
public class SodiumFluidRendererMixin {
   @Shadow
   @Final
   private BlockPos.Mutable scratchPos;

   @Inject(
      method = {"isFluidOccluded"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void isFluidOccluded(BlockRenderView world, int x, int y, int z, Direction direction, Fluid fluid, CallbackInfoReturnable<Boolean> cir) {
      if (!(Boolean)cir.getReturnValue()) {
         BlockState state = world.getBlockState(new BlockPos(this.scratchPos).offset(direction.getOpposite()));
         if(ContentMod.CONFIG.hideWaterBehindGlass && !cir.getReturnValue()) {
            BlockState sideState = world.getBlockState(this.scratchPos);
            boolean coveredFromInside = state.getBlock() instanceof AquariumGlassBlock
               && ((Direction)state.get(Properties.FACING)).equals(direction.getOpposite());
            boolean coveredFromSide = sideState.getBlock() instanceof AquariumGlassBlock
               && ((Direction)sideState.get(Properties.FACING)).equals(direction);
            if (coveredFromInside || coveredFromSide) {
               cir.setReturnValue(true);
            }
         }
      }
   }
}
