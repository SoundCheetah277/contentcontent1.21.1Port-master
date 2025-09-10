package com.eightsidedsquare.contentcontent.common.status_effect;

import com.mojang.datafixers.util.Either;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryOwner;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class RustlingStatusEffect extends StatusEffect implements RegistryEntry<StatusEffect> {
   public RustlingStatusEffect() {
      super(StatusEffectCategory.NEUTRAL, 2261063);
   }

   public boolean canApplyUpdateEffect(int duration, int amplifier) {
      return true;
   }

   @Override
   public StatusEffect value() {
      return null;
   }

   @Override
   public boolean hasKeyAndValue() {
      return false;
   }

   @Override
   public boolean matchesId(Identifier id) {
      return false;
   }

   @Override
   public boolean matchesKey(RegistryKey<StatusEffect> key) {
      return false;
   }

   @Override
   public boolean matches(Predicate<RegistryKey<StatusEffect>> predicate) {
      return false;
   }

   @Override
   public boolean isIn(TagKey<StatusEffect> tag) {
      return false;
   }

   @Override
   public boolean matches(RegistryEntry<StatusEffect> entry) {
      return false;
   }

   @Override
   public Stream<TagKey<StatusEffect>> streamTags() {
      return Stream.empty();
   }

   @Override
   public Either<RegistryKey<StatusEffect>, StatusEffect> getKeyOrValue() {
      return null;
   }

   @Override
   public Optional<RegistryKey<StatusEffect>> getKey() {
      return Optional.empty();
   }

   @Override
   public Type getType() {
      return null;
   }

   @Override
   public boolean ownerEquals(RegistryEntryOwner<StatusEffect> owner) {
      return false;
   }
}
