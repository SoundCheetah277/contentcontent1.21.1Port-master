package com.eightsidedsquare.contentcontent.core;

import com.eightsidedsquare.contentcontent.core.config.ContentConfig;
import com.eightsidedsquare.contentcontent.mixin.WoodTypeAccessor;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.WoodType;
import net.minecraft.item.GoatHornItem;
import net.minecraft.item.Instrument;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import software.bernie.geckolib.GeckoLib;

public class ContentMod implements ModInitializer {
   public static final String MOD_ID = "contentcontent";
   public static ContentConfig CONFIG;
   public static final BlockSetType FOLLYSET = new BlockSetType("folly");
   public static final WoodType FOLLY = WoodTypeAccessor.callRegister(
           new WoodType("folly", ContentMod.FOLLYSET)
   );

   @Override
   public void onInitialize() {
      //GeckoLib.initialize();
      ContentBlocks.init();
      ContentItems.init();
      ContentEntities.init();
      ContentPotions.init();
      ContentSounds.init();
      ContentInstruments.init();
      AutoConfig.register(ContentConfig.class, JanksonConfigSerializer::new);
      CONFIG = AutoConfig.getConfigHolder(ContentConfig.class).getConfig();
      ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
         for (RegistryEntry<Instrument> entry : Registries.INSTRUMENT.iterateEntries(ContentInstruments.COPPER_HORNS_MELODY_TAG)) {
            if (GoatHornItem.getStackForInstrument(ContentItems.COPPER_HORN, entry).getItem() == ContentItems.COPPER_HORN) {
               entries.add(GoatHornItem.getStackForInstrument(ContentItems.COPPER_HORN, entry));
            }
         }
      });
   }
}
