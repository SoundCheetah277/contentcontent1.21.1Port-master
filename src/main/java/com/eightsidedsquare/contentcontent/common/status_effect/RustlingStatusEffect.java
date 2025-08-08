package com.eightsidedsquare.contentcontent.common.status_effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class RustlingStatusEffect extends StatusEffect {
   public RustlingStatusEffect() {
      super(StatusEffectCategory.NEUTRAL, 2261063);
   }

   public boolean canApplyUpdateEffect(int duration, int amplifier) {
      return true;
   }
}
