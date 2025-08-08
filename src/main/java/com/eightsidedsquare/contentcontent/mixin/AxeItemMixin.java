package com.eightsidedsquare.contentcontent.mixin;

import com.eightsidedsquare.contentcontent.core.ContentBlocks;
import java.util.Optional;

import net.minecraft.block.BlockState;
import net.minecraft.item.AxeItem;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({AxeItem.class})
public class AxeItemMixin {
   @Inject(
      method = {"getStrippedState"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getStrippedState(BlockState state, CallbackInfoReturnable<Optional<BlockState>> cir) {
      if (state.isOf(ContentBlocks.FOLLY_TRUNK)) {
         cir.setReturnValue(
            Optional.of((BlockState)ContentBlocks.STRIPPED_FOLLY_LOG.getDefaultState().with(Properties.AXIS, Direction.Axis.Y))
         );
      }
   }
}
