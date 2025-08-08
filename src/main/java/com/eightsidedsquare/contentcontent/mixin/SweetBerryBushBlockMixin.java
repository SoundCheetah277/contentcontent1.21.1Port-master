package com.eightsidedsquare.contentcontent.mixin;

import com.eightsidedsquare.contentcontent.common.entity.squirrel.SquirrelEntity;
import com.eightsidedsquare.contentcontent.core.ContentPotions;
import net.minecraft.block.BlockState;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({SweetBerryBushBlock.class})
public class SweetBerryBushBlockMixin {
   @Inject(
      method = {"onEntityCollision"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
      if (entity instanceof SquirrelEntity || entity instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect((RegistryEntry<StatusEffect>) ContentPotions.RUSTLING_STATUS_EFFECT)) {
         ci.cancel();
      }
   }
}
