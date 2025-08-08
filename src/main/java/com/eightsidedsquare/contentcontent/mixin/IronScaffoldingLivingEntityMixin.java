package com.eightsidedsquare.contentcontent.mixin;

import com.eightsidedsquare.contentcontent.core.ContentBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class IronScaffoldingLivingEntityMixin extends Entity {
   public IronScaffoldingLivingEntityMixin(EntityType<?> type, World world) {
      super(type, world);
   }

   @Shadow
   public abstract boolean isHoldingOntoLadder();

   @Inject(
           method = "applyClimbingSpeed",
           at = @At("RETURN"),
           cancellable = true
   )
   private void contentcontent$applyClimbingSpeed(Vec3d motion, CallbackInfoReturnable<Vec3d> cir) {
      LivingEntity entity = (LivingEntity)(Object)this;
      if (this.getBlockStateAtPos().isOf(ContentBlocks.IRON_SCAFFOLDING)
              && this.isHoldingOntoLadder()
              && entity instanceof PlayerEntity) {
         Vec3d ret = cir.getReturnValue();
         cir.setReturnValue(new Vec3d(ret.x, -0.15, ret.z));
      }
   }
}