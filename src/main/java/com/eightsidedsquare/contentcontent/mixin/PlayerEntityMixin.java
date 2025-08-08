package com.eightsidedsquare.contentcontent.mixin;

import com.eightsidedsquare.contentcontent.core.ContentItems;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({PlayerEntity.class})
public class PlayerEntityMixin {
   @WrapOperation(
      method = {"attack"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/enchantment/EnchantmentHelper;getKnockback(Lnet/minecraft/entity/LivingEntity;)I"
      )}
   )
   private int contentcontent$getBoxingGloveKnockback(LivingEntity entity, Operation<Integer> operation) {
      return (Integer)operation.call(entity) + (entity.getMainHandStack().isOf(ContentItems.BOXING_GLOVE) ? 1 : 0);
   }
}
