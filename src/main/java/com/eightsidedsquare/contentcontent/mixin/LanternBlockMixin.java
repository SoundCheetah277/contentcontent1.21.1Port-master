package com.eightsidedsquare.contentcontent.mixin;

import com.eightsidedsquare.contentcontent.common.block.SteelBarBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.LanternBlock;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LanternBlock.class})
public class LanternBlockMixin {
   @Inject(
      method = {"canPlaceAt"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void canPlaceAt(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
      if ((Boolean)state.get(Properties.HANGING) && world.getBlockState(pos.up()).getBlock() instanceof SteelBarBlock) {
         cir.setReturnValue(true);
      }
   }
}
