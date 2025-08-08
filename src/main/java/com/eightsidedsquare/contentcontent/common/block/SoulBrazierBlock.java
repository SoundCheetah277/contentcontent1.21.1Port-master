package com.eightsidedsquare.contentcontent.common.block;

import com.eightsidedsquare.contentcontent.client.ContentClient;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.particle.ParticleTypes;
import oshi.util.tuples.Triplet;

public class SoulBrazierBlock extends BrazierBlock {
   public SoulBrazierBlock(AbstractBlock.Settings settings) {
      super(settings);
   }

   public static int getLuminance(BlockState state) {
      return state.get(LIT) ? 10 : 0;
   }

   @Override
   protected int getDamage() {
      return 2;
   }

   @Override
   protected ImmutableList<Triplet<SimpleParticleType, Float, Float>> getParticles(BlockState state) {
      return ImmutableList.of(new Triplet(ParticleTypes.CAMPFIRE_COSY_SMOKE, 0.4F, 0.07F), new Triplet(ContentClient.SOUL_EMBER, 1.0F, 0.0F));
   }
}
