package com.eightsidedsquare.contentcontent.mixin;

import com.eightsidedsquare.contentcontent.core.ContentItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
   @Inject(method = "attack", at = @At("HEAD"))
   private void addBoxingGloveKnockback(Entity target, CallbackInfo ci) {
      PlayerEntity player = (PlayerEntity)(Object)this;

      if (player.getMainHandStack().isOf(ContentItems.BOXING_GLOVE) && target instanceof LivingEntity livingTarget) {
         // Apply additional knockback through velocity modification
         double knockbackStrength = 1.0;
         double dx = -Math.sin(Math.toRadians(player.getYaw())) * knockbackStrength;
         double dz = Math.cos(Math.toRadians(player.getYaw())) * knockbackStrength;
         livingTarget.addVelocity(dx, 0.1, dz);
         livingTarget.velocityModified = true;
      }
   }
}
