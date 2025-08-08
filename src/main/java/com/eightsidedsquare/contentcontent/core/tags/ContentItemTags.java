package com.eightsidedsquare.contentcontent.core.tags;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ContentItemTags {
   public static final TagKey<Item> BRANCHES = register("branches");
   public static final TagKey<Item> BRANCHES_THAT_BURN = register("branches_that_burn");
   public static final TagKey<Item> MAGNETIC_ARMOR = register("magnetic_armor");

   private static TagKey<Item> register(String id) {
      return TagKey.of(RegistryKeys.ITEM, Identifier.of("contentcontent", id));
   }
}