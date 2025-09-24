package com.eightsidedsquare.contentcontent.core.tags;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ContentBlockTags {
   public static final TagKey<Block> BRANCHES = register("branches");
   public static final TagKey<Block> BRANCHES_THAT_BURN = register("branches_that_burn");
   public static final TagKey<Block> FOLLY_VINES = register("folly_vines");

   private static TagKey<Block> register(String id) {
      return TagKey.of(RegistryKeys.BLOCK, Identifier.of("contentcontent", id));
   }

}
