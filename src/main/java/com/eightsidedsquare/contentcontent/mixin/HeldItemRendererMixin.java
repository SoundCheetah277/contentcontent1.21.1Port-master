package com.eightsidedsquare.contentcontent.mixin;

import com.eightsidedsquare.contentcontent.core.ContentItems;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({HeldItemRenderer.class})
public abstract class HeldItemRendererMixin {
   @Shadow
   @Final
   private MinecraftClient client;

   @Shadow
   protected abstract void applyEquipOffset(MatrixStack var1, Arm var2, float var3);

   @WrapWithCondition(
           method = "renderFirstPersonItem",
           at = @At(
                   value = "INVOKE",
                   target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applySwingOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V",
                   ordinal = 1
           )
   )
   private boolean contentcontent$stopBoxingGloveRotation(HeldItemRenderer instance, MatrixStack matrices, Arm arm, float swingProgress, AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress2, ItemStack item, float equipProgress, MatrixStack matrices2, VertexConsumerProvider vertexConsumers, int light) {
      if (player.getStackInHand(hand).isOf(ContentItems.BOXING_GLOVE)) {
         matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)((arm == Arm.RIGHT ? 1 : -1) * 90) * (-6.0F * swingProgress * swingProgress + 6.0F * swingProgress)));
         return false;
      } else {
         return true;
      }
   }

   @WrapWithCondition(
           method = "renderFirstPersonItem",
           at = @At(
                   value = "INVOKE",
                   target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V",
                   ordinal = 8
           )
   )
   private boolean contentcontent$stopBoxingGloveOffset(HeldItemRenderer instance, MatrixStack matrices, Arm arm, float swingProgress, AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress2, ItemStack item, float equipProgress, MatrixStack matrices2, VertexConsumerProvider vertexConsumers, int light) {
      if (player.getStackInHand(hand).isOf(ContentItems.BOXING_GLOVE)) {
         matrices.translate((float)(arm == Arm.RIGHT ? 1 : -1) * 0.56F, -0.52F, -0.72F);
         return false;
      } else {
         return true;
      }
   }

   @WrapWithCondition(
           method = "renderFirstPersonItem",
           at = @At(
                   value = "INVOKE",
                   target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V"
           )
   )
   private boolean contentcontent$boxingGloveOffset(MatrixStack matrices, float x, float y, float z, AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices2, VertexConsumerProvider vertexConsumers, int light) {
      if (player.getStackInHand(hand).isOf(ContentItems.BOXING_GLOVE)) {
         double i = (-4.0F * swingProgress * swingProgress + 4.0F * swingProgress);
         matrices.translate(0.0D, -0.25D * i, -0.75D * i);
         return false;
      } else {
         return true;
      }
   }
}