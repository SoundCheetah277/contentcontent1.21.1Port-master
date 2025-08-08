package com.eightsidedsquare.contentcontent.common.entity.ai;

import com.mojang.serialization.Codec;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class CMemoryModuleType {
   public static final MemoryModuleType<BlockPos> NEAREST_TREETOP = register("nearest_treetop");
   public static final MemoryModuleType<Boolean> IS_WAITING = register("is_waiting", Codec.BOOL);
   public static final MemoryModuleType<Boolean> IS_EATING = register("is_eating", Codec.BOOL);
   public static final MemoryModuleType<Boolean> IS_DIGGING = register("is_digging", Codec.BOOL);
   public static final MemoryModuleType<Boolean> DUG_RECENTLY = register("dug_recently", Codec.BOOL);
   public static final MemoryModuleType<Boolean> CLIMBED_TREE_RECENTLY = register("climbed_tree_recently", Codec.BOOL);
   public static final MemoryModuleType<Boolean> ATE_RECENTLY = register("ate_recently", Codec.BOOL);
   public static final MemoryModuleType<Integer> CALM_TOWARDS_PLAYERS_COOLDOWN = register("calm_towards_players_cooldown", Codec.INT);
   public static final MemoryModuleType<Integer> LOOT_DIGS_LEFT = register("loot_digs_left", Codec.INT);
   public static final MemoryModuleType<ItemEntity> NEAREST_FOOD_ITEM_ENTITY = register("nearest_food_item_entity");

   private static <U> MemoryModuleType<U> register(String id, Codec<U> codec) {
      return Registry.register(
              Registries.MEMORY_MODULE_TYPE,
               Identifier.of("contentcontent", id),
              new MemoryModuleType<>(Optional.of(codec))
      );
   }

   private static <U> MemoryModuleType<U> register(String id) {
      return Registry.register(
              Registries.MEMORY_MODULE_TYPE,
              Identifier.of("contentcontent", id),
              new MemoryModuleType<>(Optional.empty())
      );
   }
}
