package com.eightsidedsquare.contentcontent.mixin;

import com.eightsidedsquare.contentcontent.core.ContentItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public class MobEntityMixin {

   private void contentcontent$modifyKnockback(Entity target, CallbackInfoReturnable<Boolean> cir) {
      MobEntity self = (MobEntity)(Object)this;

      // Check if the mob's main hand holds the boxing glove item
      if (self.getMainHandStack().isOf(ContentItems.BOXING_GLOVE)) {
         // Apply custom knockback with increased strength
         float customKnockbackStrength = 1.0F; // Adjust as needed

         // Call the knockback method with the custom strength
         self.takeKnockback(customKnockbackStrength, target.getX() - self.getX(), target.getZ() - self.getZ());

         // Continue with attack success
         cir.setReturnValue(true);
      }
      // Otherwise, let the original method continue normally
   }
}
