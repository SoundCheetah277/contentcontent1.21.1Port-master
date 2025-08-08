package com.eightsidedsquare.contentcontent.mixin;

import com.eightsidedsquare.contentcontent.common.block.AquariumGlassBlock;
import com.eightsidedsquare.contentcontent.core.ContentMod;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidRenderer.class)
public class FluidRendererMixin {

   @Inject(
           method = "isSideCovered",
           at = @At("RETURN"),
           cancellable = true
   )
   private static void isSideCovered(
           BlockView world,
           Direction direction,
           float maxDeviation,
           BlockPos pos,
           BlockState state,
           CallbackInfoReturnable<Boolean> cir
   ) {
      FluidState fluidState = state.getFluidState();

      if(ContentMod.CONFIG.hideWaterBehindGlass && fluidState.isIn(FluidTags.WATER)) {
         BlockState sideState = world.getBlockState(pos.offset(direction));
         if (state.getBlock() instanceof AquariumGlassBlock &&
                 state.contains(Properties.FACING) &&
                 state.get(Properties.FACING) == direction.getOpposite()) {
            cir.setReturnValue(false);
         }
         if (sideState.getBlock() instanceof AquariumGlassBlock &&
                 sideState.contains(Properties.FACING) &&
                 sideState.get(Properties.FACING) == direction) {
            cir.setReturnValue(false);
         }
      }
   }
}
