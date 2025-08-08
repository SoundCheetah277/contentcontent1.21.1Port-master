package com.eightsidedsquare.contentcontent.common.item;

import com.eightsidedsquare.contentcontent.core.ContentInstruments;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Optional;

public class CopperHornItem extends GoatHornItem {
   public CopperHornItem(Settings settings) {
      super(settings, ContentInstruments.COPPER_HORNS_MELODY_TAG);
   }

   public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
      ItemStack itemStack = user.getStackInHand(hand);
      Optional<RegistryEntry.Reference<Instrument>> optional = ContentInstruments.getInstrument(itemStack);
      if (optional.isPresent()) {
         Instrument instrument = optional.get().value();
         user.setCurrentHand(hand);
         playSound(world, user, instrument);
         user.getItemCooldownManager().set(this, instrument.useDuration());
         return TypedActionResult.consume(itemStack);
      } else {
         return TypedActionResult.fail(itemStack);
      }
   }

   private static void playSound(World world, PlayerEntity player, Instrument instrument) {
      Instrument playedInstrument = instrument;
      if (ContentInstruments.getCopperHornInstrument(instrument).isPresent()) {
         ContentInstruments.CopperHornInstrument copperHornInstrument = ContentInstruments.getCopperHornInstrument(instrument).get();
         if (player.isSneaking()) {
            playedInstrument = copperHornInstrument.bass;
         } else if (player.getPitch() < -80.0F) {
            playedInstrument = copperHornInstrument.harmony;
         }
      }

      SoundEvent soundEvent = playedInstrument.soundEvent().value();
      float f = playedInstrument.range() / 16.0F;
      world.playSoundFromEntity(player, player, soundEvent, SoundCategory.RECORDS, f, 1.0F);
      world.emitGameEvent(GameEvent.INSTRUMENT_PLAY, player.getPos(), GameEvent.Emitter.of(player));
   }
}
