package com.eightsidedsquare.contentcontent.core;

import com.eightsidedsquare.contentcontent.common.status_effect.RustlingStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ContentPotions {

   public static final StatusEffect RUSTLING_STATUS_EFFECT = new RustlingStatusEffect();
   public static final Potion RUSTLING = registerPotion("rustling", new Potion(new StatusEffectInstance[]{new StatusEffectInstance((RegistryEntry<StatusEffect>) RUSTLING_STATUS_EFFECT, 3600)}));
   public static final Potion LONG_RUSTLING = registerPotion(
      "long_rustling", new Potion(new StatusEffectInstance[]{new StatusEffectInstance((RustlingStatusEffect) RUSTLING_STATUS_EFFECT, 9600)})
   );

   private static Potion registerPotion(String name, Potion potion) {
      return (Potion) Registry.register(Registries.POTION, name, potion);
   }

   public static void init() {
      Registry.register(Registries.STATUS_EFFECT, Identifier.of("contentcontent", "rustling"), RUSTLING_STATUS_EFFECT);
   }
}
