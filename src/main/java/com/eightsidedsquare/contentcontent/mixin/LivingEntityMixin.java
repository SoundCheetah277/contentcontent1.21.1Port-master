package com.eightsidedsquare.contentcontent.mixin;

import com.eightsidedsquare.contentcontent.core.ContentItems;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @WrapOperation(
            method = "getKnockbackAgainst",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/enchantment/EnchantmentHelper;modifyKnockback(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;F)F"
            )
    )
    private float contentcontent$modifyKnockback(ServerWorld world, ItemStack stack, Entity target, DamageSource source, float base, Operation<Float> original) {
        float originalKnockback = original.call(world, stack, target, source, base);
        if (stack.isOf(ContentItems.BOXING_GLOVE)) {
            return originalKnockback + 1.0F;  // Increase knockback by 1 if boxing glove is held
        }
        return originalKnockback;
    }
}