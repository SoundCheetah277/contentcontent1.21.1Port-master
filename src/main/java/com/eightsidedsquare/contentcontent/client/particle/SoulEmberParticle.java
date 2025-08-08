package com.eightsidedsquare.contentcontent.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public class SoulEmberParticle extends EmberParticle {
   protected SoulEmberParticle(ClientWorld clientWorld, double x, double y, double z) {
      super(clientWorld, x, y, z);
   }

   @Override
   public void updateColor() {
      float delta = (float)this.age / this.maxAge;
      float delta3 = (float)Math.pow(delta - 1.0F, 3.0) + 1.0F;
      float f = Math.max(0.0F, this.random.nextFloat() * this.random.nextFloat() * 0.5F - 0.25F);
      float g = Math.min(MathHelper.lerp(delta, 1.0F, 0.05F) + f, 1.0F);
      float b = Math.min(MathHelper.lerp(delta3, 1.0F, 0.0F) + f, 1.0F);
      this.setColor(b, g, 1.0F);
   }

   @Environment(EnvType.CLIENT)
   public static class SoulEmberFactory implements ParticleFactory<SimpleParticleType> {
      private final SpriteProvider spriteProvider;

      public SoulEmberFactory(SpriteProvider spriteProvider) {
         this.spriteProvider = spriteProvider;
      }

      public Particle createParticle(ParticleType defaultParticleType, ClientWorld clientWorld, double x, double y, double z, double g, double h, double i) {
         SoulEmberParticle particle = new SoulEmberParticle(clientWorld, x, y, z);
         particle.setSprite(this.spriteProvider);
         return particle;
      }

      @Override
      public @Nullable Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
         return null;
      }
   }
}
