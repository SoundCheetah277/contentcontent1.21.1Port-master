package com.eightsidedsquare.contentcontent.core;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Instrument;
import net.minecraft.item.Instruments;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.InstrumentTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ContentInstruments {
   private static final int GOAT_HORN_DURATION = 140;
   private static final float GOAT_HORN_RANGE = 256.0F;
   private static final int COPPER_HORN_DURATION = 80;
   private static final float COPPER_HORN_RANGE = 64.0F;
   private static final Map<Instrument, Identifier> INSTRUMENTS = new LinkedHashMap<>();
   private static final List<ContentInstruments.CopperHornInstrument> COPPER_HORN_INSTRUMENTS = new LinkedList<>();
   public static final Instrument FLY_GOAT_HORN = create("fly_goat_horn", new Instrument(RegistryEntry.of(ContentSounds.ITEM_GOAT_HORN_FLY), 140, 256.0F));
   public static final Instrument RESIST_GOAT_HORN = create("resist_goat_horn", new Instrument(RegistryEntry.of(ContentSounds.ITEM_GOAT_HORN_RESIST), 140, 256.0F));
   public static final ContentInstruments.CopperHornInstrument CLEAR_TEMPER_JOURNEY = createCopperHornInstrument(
           "clear_temper_journey",
           ContentSounds.CLEAR_TEMPER_JOURNEY_BASS,
           ContentSounds.CLEAR_TEMPER_JOURNEY_MELODY,
           ContentSounds.CLEAR_TEMPER_JOURNEY_HARMONY,
           Instruments.CALL_GOAT_HORN
   );
   public static final ContentInstruments.CopperHornInstrument DRY_URGE_ANGER = createCopperHornInstrument(
           "dry_urge_anger", ContentSounds.DRY_URGE_ANGER_BASS, ContentSounds.DRY_URGE_ANGER_MELODY, ContentSounds.DRY_URGE_ANGER_HARMONY, Instruments.ADMIRE_GOAT_HORN
   );
   public static final ContentInstruments.CopperHornInstrument FEARLESS_RIVER_GIFT = createCopperHornInstrument(
           "fearless_river_gift",
           ContentSounds.FEARLESS_RIVER_GIFT_BASS,
           ContentSounds.FEARLESS_RIVER_GIFT_MELODY,
           ContentSounds.FEARLESS_RIVER_GIFT_HARMONY,
           FLY_GOAT_HORN
   );
   public static final ContentInstruments.CopperHornInstrument FRESH_NEST_THOUGHT = createCopperHornInstrument(
           "fresh_nest_thought",
           ContentSounds.FRESH_NEST_THOUGHT_BASS,
           ContentSounds.FRESH_NEST_THOUGHT_MELODY,
           ContentSounds.FRESH_NEST_THOUGHT_HARMONY,
           Instruments.YEARN_GOAT_HORN
   );
   public static final ContentInstruments.CopperHornInstrument GREAT_SKY_FALLING = createCopperHornInstrument(
           "great_sky_falling",
           ContentSounds.GREAT_SKY_FALLING_BASS,
           ContentSounds.GREAT_SKY_FALLING_MELODY,
           ContentSounds.GREAT_SKY_FALLING_HARMONY,
           Instruments.PONDER_GOAT_HORN
   );
   public static final ContentInstruments.CopperHornInstrument MUMBLE_FIRE_MEMORY = createCopperHornInstrument(
           "mumble_fire_memory",
           ContentSounds.MUMBLE_FIRE_MEMORY_BASS,
           ContentSounds.MUMBLE_FIRE_MEMORY_MELODY,
           ContentSounds.MUMBLE_FIRE_MEMORY_HARMONY,
           Instruments.FEEL_GOAT_HORN
   );
   public static final ContentInstruments.CopperHornInstrument OLD_HYMN_RESTING = createCopperHornInstrument(
           "old_hymn_resting",
           ContentSounds.OLD_HYMN_RESTING_BASS,
           ContentSounds.OLD_HYMN_RESTING_MELODY,
           ContentSounds.OLD_HYMN_RESTING_HARMONY,
           Instruments.SING_GOAT_HORN
   );
   public static final ContentInstruments.CopperHornInstrument PURE_WATER_DESIRE = createCopperHornInstrument(
           "pure_water_desire",
           ContentSounds.PURE_WATER_DESIRE_BASS,
           ContentSounds.PURE_WATER_DESIRE_MELODY,
           ContentSounds.PURE_WATER_DESIRE_HARMONY,
           Instruments.SEEK_GOAT_HORN
   );
   public static final ContentInstruments.CopperHornInstrument SECRET_LAKE_TEAR = createCopperHornInstrument(
           "secret_lake_tear", ContentSounds.SECRET_LAKE_TEAR_BASS, ContentSounds.SECRET_LAKE_TEAR_MELODY, ContentSounds.SECRET_LAKE_TEAR_HARMONY, RESIST_GOAT_HORN
   );
   public static final ContentInstruments.CopperHornInstrument SWEET_MOON_LOVE = createCopperHornInstrument(
           "sweet_moon_love",
           ContentSounds.SWEET_MOON_LOVE_BASS,
           ContentSounds.SWEET_MOON_LOVE_MELODY,
           ContentSounds.SWEET_MOON_LOVE_HARMONY,
           Instruments.DREAM_GOAT_HORN
   );
   public static final TagKey<Instrument> COPPER_HORNS_TAG = tagOf("copper_horns");
   public static final TagKey<Instrument> COPPER_HORNS_BASS_TAG = tagOf("copper_horns_bass");
   public static final TagKey<Instrument> COPPER_HORNS_MELODY_TAG = tagOf("copper_horns_melody");
   public static final TagKey<Instrument> COPPER_HORNS_HARMONY_TAG = tagOf("copper_horns_harmony");

   public static Optional<ContentInstruments.CopperHornInstrument> getCopperHornInstrument(Instrument melody) {
      return COPPER_HORN_INSTRUMENTS.stream().filter(c -> c.melody.equals(melody)).findFirst();
   }

   public static ContentInstruments.CopperHornInstrument getCraftedCopperHorn(Instrument goatHorn) {
      return COPPER_HORN_INSTRUMENTS.stream()
              .filter(copperHornInstrument -> copperHornInstrument.baseGoatHorn.equals(goatHorn))
              .findFirst()
              .orElse(FEARLESS_RIVER_GIFT);
   }

   public static Optional<RegistryEntry.Reference<Instrument>> getInstrument(ItemStack stack) {
      var instrumentEntry = stack.get(DataComponentTypes.INSTRUMENT);
      Identifier identifier;
      if (instrumentEntry != null) {
         return Optional.of((RegistryEntry.Reference<Instrument>) instrumentEntry);
      } else {
         Iterator<RegistryEntry<Instrument>> iterator = Registries.INSTRUMENT.iterateEntries(InstrumentTags.GOAT_HORNS).iterator();
         return iterator.hasNext() ? Optional.of((RegistryEntry.Reference<Instrument>) iterator.next()) : Optional.empty();
      }
   }

   public static void init() {
      INSTRUMENTS.keySet().forEach(instrument -> Registry.register(Registries.INSTRUMENT, INSTRUMENTS.get(instrument), instrument));
   }

   private static TagKey<Instrument> tagOf(String id) {
      return TagKey.of(RegistryKeys.INSTRUMENT, Identifier.of("contentcontent", id));
   }

   private static ContentInstruments.CopperHornInstrument createCopperHornInstrument(
           String id, SoundEvent bass, SoundEvent melody, SoundEvent harmony, RegistryKey<Instrument> entry
   ) {
      Instrument baseGoatHorn = (Instrument)Registries.INSTRUMENT.get(entry);
      return createCopperHornInstrument(id, bass, melody, harmony, baseGoatHorn);
   }

   private static ContentInstruments.CopperHornInstrument createCopperHornInstrument(
           String id, SoundEvent bass, SoundEvent melody, SoundEvent harmony, Instrument baseGoatHorn
   ) {
      ContentInstruments.CopperHornInstrument copperHornInstrument = new ContentInstruments.CopperHornInstrument(id, bass, melody, harmony, baseGoatHorn);
      COPPER_HORN_INSTRUMENTS.add(copperHornInstrument);
      return copperHornInstrument;
   }

   private static Instrument create(String id, Instrument instrument) {
      INSTRUMENTS.put(instrument, Identifier.of("contentcontent", id));
      return instrument;
   }

   public static class CopperHornInstrument {
      public final Instrument bass;
      public final Instrument melody;
      public final Instrument harmony;
      public final Instrument baseGoatHorn;

      private CopperHornInstrument(String id, SoundEvent bass, SoundEvent melody, SoundEvent harmony, Instrument baseGoatHorn) {
         this.bass = this.createHorn(id, bass, "bass");
         this.melody = this.createHorn(id, melody, "melody");
         this.harmony = this.createHorn(id, harmony, "harmony");
         this.baseGoatHorn = baseGoatHorn;
      }

      private Instrument createHorn(String id, SoundEvent sound, String type) {
         return ContentInstruments.create(id + "_" + type + "_copper_horn", new Instrument(RegistryEntry.of(sound), 80, 64.0F));
      }
   }
}
