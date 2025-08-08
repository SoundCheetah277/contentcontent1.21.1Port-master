package com.eightsidedsquare.contentcontent.core;

import com.google.common.collect.Lists;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.List;

public class ContentSounds {
   private static final List<SoundEvent> SOUNDS = Lists.newArrayList();
   public static final SoundEvent ENTITY_SQUIRREL_AMBIENT = create("entity.squirrel.ambient");
   public static final SoundEvent ENTITY_SQUIRREL_DEATH = create("entity.squirrel.death");
   public static final SoundEvent ENTITY_SQUIRREL_HURT = create("entity.squirrel.hurt");
   public static final SoundEvent ENTITY_SQUIRREL_STEP = create("entity.squirrel.step");
   public static final SoundEvent ENTITY_SQUIRREL_EAT = create("entity.squirrel.eat");
   public static final SoundEvent ENTITY_SQUIRREL_DIG = create("entity.squirrel.dig");
   public static final SoundEvent ENTITY_SQUIRREL_HAPPY = create("entity.squirrel.happy");
   public static final SoundEvent BAD_TO_THE_BONE = create("block.stickshift.bad_to_the_bone");
   public static final SoundEvent NOTE_HORN = create("block.note_block.horn");
   public static final SoundEvent NOTE_ELECTRIC_GUITAR = create("block.note_block.electric_guitar");
   public static final SoundEvent ITEM_GOAT_HORN_FLY = create("item.goat_horn.fly");
   public static final SoundEvent ITEM_GOAT_HORN_RESIST = create("item.goat_horn.resist");
   public static final SoundEvent CLEAR_TEMPER_JOURNEY_BASS = create("item.copper_horn.clear_temper_journey.bass");
   public static final SoundEvent CLEAR_TEMPER_JOURNEY_MELODY = create("item.copper_horn.clear_temper_journey.melody");
   public static final SoundEvent CLEAR_TEMPER_JOURNEY_HARMONY = create("item.copper_horn.clear_temper_journey.harmony");
   public static final SoundEvent DRY_URGE_ANGER_BASS = create("item.copper_horn.dry_urge_anger.bass");
   public static final SoundEvent DRY_URGE_ANGER_MELODY = create("item.copper_horn.dry_urge_anger.melody");
   public static final SoundEvent DRY_URGE_ANGER_HARMONY = create("item.copper_horn.dry_urge_anger.harmony");
   public static final SoundEvent FEARLESS_RIVER_GIFT_BASS = create("item.copper_horn.fearless_river_gift.bass");
   public static final SoundEvent FEARLESS_RIVER_GIFT_MELODY = create("item.copper_horn.fearless_river_gift.melody");
   public static final SoundEvent FEARLESS_RIVER_GIFT_HARMONY = create("item.copper_horn.fearless_river_gift.harmony");
   public static final SoundEvent FRESH_NEST_THOUGHT_BASS = create("item.copper_horn.fresh_nest_thought.bass");
   public static final SoundEvent FRESH_NEST_THOUGHT_MELODY = create("item.copper_horn.fresh_nest_thought.melody");
   public static final SoundEvent FRESH_NEST_THOUGHT_HARMONY = create("item.copper_horn.fresh_nest_thought.harmony");
   public static final SoundEvent GREAT_SKY_FALLING_BASS = create("item.copper_horn.great_sky_falling.bass");
   public static final SoundEvent GREAT_SKY_FALLING_MELODY = create("item.copper_horn.great_sky_falling.melody");
   public static final SoundEvent GREAT_SKY_FALLING_HARMONY = create("item.copper_horn.great_sky_falling.harmony");
   public static final SoundEvent MUMBLE_FIRE_MEMORY_BASS = create("item.copper_horn.mumble_fire_memory.bass");
   public static final SoundEvent MUMBLE_FIRE_MEMORY_MELODY = create("item.copper_horn.mumble_fire_memory.melody");
   public static final SoundEvent MUMBLE_FIRE_MEMORY_HARMONY = create("item.copper_horn.mumble_fire_memory.harmony");
   public static final SoundEvent OLD_HYMN_RESTING_BASS = create("item.copper_horn.old_hymn_resting.bass");
   public static final SoundEvent OLD_HYMN_RESTING_MELODY = create("item.copper_horn.old_hymn_resting.melody");
   public static final SoundEvent OLD_HYMN_RESTING_HARMONY = create("item.copper_horn.old_hymn_resting.harmony");
   public static final SoundEvent PURE_WATER_DESIRE_BASS = create("item.copper_horn.pure_water_desire.bass");
   public static final SoundEvent PURE_WATER_DESIRE_MELODY = create("item.copper_horn.pure_water_desire.melody");
   public static final SoundEvent PURE_WATER_DESIRE_HARMONY = create("item.copper_horn.pure_water_desire.harmony");
   public static final SoundEvent SECRET_LAKE_TEAR_BASS = create("item.copper_horn.secret_lake_tear.bass");
   public static final SoundEvent SECRET_LAKE_TEAR_MELODY = create("item.copper_horn.secret_lake_tear.melody");
   public static final SoundEvent SECRET_LAKE_TEAR_HARMONY = create("item.copper_horn.secret_lake_tear.harmony");
   public static final SoundEvent SWEET_MOON_LOVE_BASS = create("item.copper_horn.sweet_moon_love.bass");
   public static final SoundEvent SWEET_MOON_LOVE_MELODY = create("item.copper_horn.sweet_moon_love.melody");
   public static final SoundEvent SWEET_MOON_LOVE_HARMONY = create("item.copper_horn.sweet_moon_love.harmony");

   public static void init() {
      SOUNDS.forEach(sound -> Registry.register(Registries.SOUND_EVENT, sound.getId(), sound));
   }

   private static SoundEvent create(String id) {
      Identifier identifier = Identifier.of("contentcontent", id);
      SoundEvent soundEvent = SoundEvent.of(identifier);
      SOUNDS.add(soundEvent);
      return soundEvent;
   }
}