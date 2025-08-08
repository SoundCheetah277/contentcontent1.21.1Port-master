package com.eightsidedsquare.contentcontent.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class EmberParticle extends SpriteBillboardParticle {
   protected EmberParticle(ClientWorld clientWorld, double x, double y, double z) {
      super(clientWorld, x, y, z);
      this.maxAge = this.random.nextInt(50) + 80;
      this.setBoundingBoxSpacing(0.025F, 0.025F);
      this.setColor(1.0F, 1.0F, 1.0F);
      this.velocityY = this.velocityY + this.random.nextFloat() / 20.0F + 0.1;
      this.gravityStrength = 0.0025F;
   }

   public int getBrightness(float tint) {
      return 15728880;
   }

   public void tick() {
      this.prevPosX = this.x;
      this.prevPosY = this.y;
      this.prevPosZ = this.z;
      if (this.age++ < this.maxAge && !(this.alpha <= 0.0F)) {
         float delta = (float)this.age / this.maxAge;
         this.velocityX = this.velocityX + this.random.nextFloat() / 75.0F * (1.0F - delta) * (this.random.nextBoolean() ? 1 : -1);
         this.velocityZ = this.velocityZ + this.random.nextFloat() / 75.0F * (1.0F - delta) * (this.random.nextBoolean() ? 1 : -1);
         this.velocityY = this.velocityY - this.gravityStrength;
         this.move(this.velocityX, this.velocityY, this.velocityZ);
         if (this.age >= this.maxAge - 60 && this.alpha > 0.01F) {
            this.alpha -= 0.015F;
         }

         this.updateColor();
      } else {
         this.markDead();
      }
   }

   public void updateColor() {
      float delta = (float)this.age / this.maxAge;
      float delta3 = (float)Math.pow(delta - 1.0F, 3.0) + 1.0F;
      float f = Math.max(0.0F, this.random.nextFloat() * this.random.nextFloat() * 0.5F - 0.25F);
      float g = Math.min(MathHelper.lerp(delta, 1.0F, 0.05F) + f, 1.0F);
      float b = Math.min(MathHelper.lerp(delta3, 1.0F, 0.0F) + f, 1.0F);
      this.setColor(1.0F, g, b);
   }

   public ParticleTextureSheet getType() {
      return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
   }

   @Environment(EnvType.CLIENT)
   public static class EmberFactory implements ParticleFactory<SimpleParticleType> {
      private final SpriteProvider spriteProvider;

      public EmberFactory(SpriteProvider spriteProvider) {
         this.spriteProvider = spriteProvider;
      }

      public Particle createParticle(ParticleType defaultParticleType, ClientWorld clientWorld, double x, double y, double z, double g, double h, double i) {
         EmberParticle particle = new EmberParticle(clientWorld, x, y, z);
         particle.setSprite(this.spriteProvider);
         return particle;
      }

      @Override
      public @Nullable Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
         return null;
      }
   }
}
