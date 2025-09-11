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

   public static StatusEffect RUSTLING_STATUS_EFFECT;
   public static RegistryEntry<StatusEffect> RUSTLING_EFFECT_ENTRY;
   public static Potion RUSTLING;
   public static Potion LONG_RUSTLING;

   public static void init() {
      // Register the status effect first
      Identifier rustlingId = Identifier.of("contentcontent", "rustling");
      RUSTLING_STATUS_EFFECT = Registry.register(Registries.STATUS_EFFECT, rustlingId, new RustlingStatusEffect());

      // Then obtain the RegistryEntry<StatusEffect> from the registry
      RUSTLING_EFFECT_ENTRY = Registries.STATUS_EFFECT.getEntry(rustlingId).orElseThrow();

      // Register potions using StatusEffectInstance with RegistryEntry reference
      RUSTLING = Registry.register(Registries.POTION, Identifier.of("contentcontent", "rustling"),
              new Potion(new StatusEffectInstance(RUSTLING_EFFECT_ENTRY, 3600)));

      LONG_RUSTLING = Registry.register(Registries.POTION, Identifier.of("contentcontent", "long_rustling"),
              new Potion(new StatusEffectInstance(RUSTLING_EFFECT_ENTRY, 9600)));
   }
}
