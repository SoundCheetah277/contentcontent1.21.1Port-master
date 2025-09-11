package com.eightsidedsquare.contentcontent.core;

import com.eightsidedsquare.contentcontent.common.item.BoxingGloveItem;
import com.eightsidedsquare.contentcontent.common.item.CopperHornItem;
import com.eightsidedsquare.contentcontent.common.item.CopperHornRecipe;
import com.eightsidedsquare.contentcontent.common.item.FollySeedItem;
import com.eightsidedsquare.contentcontent.common.item.SuspiciousDirtRecipe;
import com.eightsidedsquare.contentcontent.common.item.WrappedBundleItem;
import com.eightsidedsquare.contentcontent.common.item.WrappedBundleRecipe;
import com.eightsidedsquare.contentcontent.core.tags.ContentItemTags;
import java.util.LinkedHashMap;
import java.util.Map;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents.Modify;
import net.fabricmc.fabric.api.loot.v3.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v3.FabricLootTableBuilder;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.Entity;
import net.minecraft.item.*;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Direction;

public class ContentItems {
   private static final Map<Item, Identifier> ITEMS = new LinkedHashMap<>();
   public static final Item SQUIRREL_SPAWN_EGG = create(
           "squirrel_spawn_egg", new SpawnEggItem(ContentEntities.SQUIRREL, 9011576, 8409922, new Item.Settings())
   );
   public static final Item ACORN = create("acorn", new Item(new Item.Settings().food(new FoodComponent.Builder().saturationModifier(.3f).nutrition(4).build())));
   public static final Item IRON_SCAFFOLDING = create(
           "iron_scaffolding", new ScaffoldingItem(ContentBlocks.IRON_SCAFFOLDING, new Item.Settings())
   );
   private static final FoodComponent CHOCOLATE = new FoodComponent.Builder().nutrition(3).saturationModifier(0.1F).build();
   public static final Item CHOCOLATE_EGG = create(
           "chocolate_egg", new AliasedBlockItem(ContentBlocks.CHOCOLATE_EGG, new Item.Settings().food(CHOCOLATE))
   );
   public static final Item CHISELED_CHOCOLATE_EGG = create(
           "chiseled_chocolate_egg",
           new AliasedBlockItem(ContentBlocks.CHISELED_CHOCOLATE_EGG, new Item.Settings().food(CHOCOLATE))
   );
   public static final Item CHOCOLATE_RAT = create(
           "chocolate_rat", new AliasedBlockItem(ContentBlocks.CHOCOLATE_RAT, new Item.Settings().food(CHOCOLATE))
   );
   public static final Item CHOCOLATE_RAVEN = create(
           "chocolate_raven", new AliasedBlockItem(ContentBlocks.CHOCOLATE_RAVEN, new Item.Settings().food(CHOCOLATE))
   );
   public static final Item CHOCOLATE_FROG = create(
           "chocolate_frog", new AliasedBlockItem(ContentBlocks.CHOCOLATE_FROG, new Item.Settings().food(CHOCOLATE))
   );
   public static final Item CHOCOLATE_SQUIRREL = create(
           "chocolate_squirrel", new AliasedBlockItem(ContentBlocks.CHOCOLATE_SQUIRREL, new Item.Settings().food(CHOCOLATE))
   );
   public static final Item CHOCOLATE_CREWMATE = create(
           "chocolate_crewmate", new AliasedBlockItem(ContentBlocks.CHOCOLATE_CREWMATE, new Item.Settings().food(CHOCOLATE))
   );
   public static final Item COPPER_HORN = create("copper_horn", new CopperHornItem(new Item.Settings().maxCount(1)));
   public static final Item WRAPPED_BUNDLE = create(
           "wrapped_bundle", new WrappedBundleItem(ContentBlocks.WRAPPED_BUNDLE, new Item.Settings().maxCount(1))
   );
   public static final Item BOXING_GLOVE = create("boxing_glove", new BoxingGloveItem(new Item.Settings().maxCount(1)));
   public static final Item FOLLY_SIGN = create(
           "folly_sign",
           new SignItem(new Item.Settings().maxCount(16), ContentBlocks.FOLLY_SIGN, ContentBlocks.FOLLY_WALL_SIGN)
   );
   public static final Item FOLLY_SAPLING = create(
           "folly_sapling",
           new BlockItem(ContentBlocks.FOLLY_SAPLING, new Item.Settings().maxCount(1).rarity(Rarity.EPIC))
   );
   public static final Item FOLLY_SEED = create(
           "folly_seed", new FollySeedItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC))
   );
   public static final Item MAGNETIZED_TRACKER = create("magnetized_tracker", new Item(new Item.Settings()));
   public static final SpecialRecipeSerializer<CopperHornRecipe> COPPER_HORN_RECIPE_SERIALIZER =
           RecipeSerializer.register(
                   "contentcontent:crafting_special_copper_horn",
                   new SpecialRecipeSerializer<>((CraftingRecipeCategory identifier) -> new CopperHornRecipe(identifier))
           );
   public static final SpecialRecipeSerializer<WrappedBundleRecipe> WRAPPED_BUNDLE_RECIPE_SERIALIZER =
           RecipeSerializer.register(
                   "contentcontent:crafting_special_wrapped_bundle",
                   new SpecialRecipeSerializer<>((CraftingRecipeCategory identifier) -> new WrappedBundleRecipe(identifier))
           );
   public static final SpecialRecipeSerializer<SuspiciousDirtRecipe> SUSPICIOUS_DIRT_RECIPE_SERIALIZER =
           RecipeSerializer.register(
                   "contentcontent:crafting_special_suspicious_dirt",
                   new SpecialRecipeSerializer<>((CraftingRecipeCategory identifier) -> new SuspiciousDirtRecipe(identifier))
           );

   public static void init() {
      ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
         entries.add(ContentBlocks.STEEL_BAR);
         entries.add(ContentBlocks.CALCITE_SLAB);
         entries.add(ContentBlocks.CALCITE_STAIRS);
         entries.add(ContentBlocks.CALCITE_WALL);
         entries.add(ContentBlocks.CALCITE_BRICKS);
         entries.add(ContentBlocks.CALCITE_BRICK_SLAB);
         entries.add(ContentBlocks.CALCITE_BRICK_STAIRS);
         entries.add(ContentBlocks.CALCITE_BRICK_WALL);
         entries.add(ContentBlocks.POLISHED_CALCITE);
         entries.add(ContentBlocks.POLISHED_CALCITE_SLAB);
         entries.add(ContentBlocks.POLISHED_CALCITE_STAIRS);
         entries.add(ContentBlocks.POLISHED_CALCITE_WALL);
         entries.add(ContentBlocks.TUFF_BRICK_SLAB);
         entries.add(ContentBlocks.TUFF_BRICK_STAIRS);
         entries.add(ContentBlocks.TUFF_BRICKS);
         entries.add(ContentBlocks.TUFF_BRICK_WALL);
         entries.add(ContentBlocks.TUFF_SLAB);
         entries.add(ContentBlocks.TUFF_STAIRS);
         entries.add(ContentBlocks.TUFF_WALL);
         entries.add(ContentBlocks.POLISHED_TUFF);
         entries.add(ContentBlocks.POLISHED_TUFF_SLAB);
         entries.add(ContentBlocks.POLISHED_TUFF_STAIRS);
         entries.add(ContentBlocks.POLISHED_TUFF_WALL);
         entries.add(ContentBlocks.SMOOTH_BASALT_SLAB);
         entries.add(ContentBlocks.SMOOTH_BASALT_STAIRS);
         entries.add(ContentBlocks.SMOOTH_BASALT_WALL);
         entries.add(ContentBlocks.SMOOTH_SANDSTONE_WALL);
         entries.add(ContentBlocks.SMOOTH_RED_SANDSTONE_WALL);
         entries.add(ContentBlocks.POLISHED_ANDESITE_WALL);
         entries.add(ContentBlocks.POLISHED_DIORITE_WALL);
         entries.add(ContentBlocks.POLISHED_GRANITE_WALL);
         entries.add(ContentBlocks.QUARTZ_WALL);
         entries.add(ContentBlocks.SMOOTH_QUARTZ_WALL);
         entries.add(ContentBlocks.STRIPPED_ACACIA_BRANCH);
         entries.add(ContentBlocks.STRIPPED_BIRCH_BRANCH);
         entries.add(ContentBlocks.STRIPPED_CRIMSON_BRANCH);
         entries.add(ContentBlocks.STRIPPED_DARK_OAK_BRANCH);
         entries.add(ContentBlocks.STRIPPED_FOLLY_LOG);
         entries.add(ContentBlocks.STRIPPED_FOLLY_BRANCH);
         entries.add(ContentBlocks.STRIPPED_FOLLY_WOOD);
         entries.add(ContentBlocks.STRIPPED_JUNGLE_BRANCH);
         entries.add(ContentBlocks.STRIPPED_WARPED_BRANCH);
         entries.add(ContentBlocks.STRIPPED_MANGROVE_BRANCH);
         entries.add(ContentBlocks.STRIPPED_SPRUCE_BRANCH);
         entries.add(ContentBlocks.STRIPPED_OAK_BRANCH);
         entries.add(ContentBlocks.ACACIA_BRANCH);
         entries.add(ContentBlocks.BIRCH_BRANCH);
         entries.add(ContentBlocks.CRIMSON_BRANCH);
         entries.add(ContentBlocks.DARK_OAK_BRANCH);
         entries.add(ContentBlocks.FOLLY_LOG);
         entries.add(ContentBlocks.FOLLY_BRANCH);
         entries.add(ContentBlocks.FOLLY_WOOD);
         entries.add(ContentBlocks.JUNGLE_BRANCH);
         entries.add(ContentBlocks.WARPED_BRANCH);
         entries.add(ContentBlocks.MANGROVE_BRANCH);
         entries.add(ContentBlocks.SPRUCE_BRANCH);
         entries.add(ContentBlocks.OAK_BRANCH);
         entries.add(ContentBlocks.FOLLY_PLANKS);
         entries.add(ContentBlocks.FOLLY_STAIRS);
         entries.add(ContentBlocks.FOLLY_SLAB);
         entries.add(ContentBlocks.FOLLY_FENCE);
         entries.add(ContentBlocks.FOLLY_FENCE_GATE);
         entries.add(ContentBlocks.FOLLY_BUTTON);
         entries.add(ContentBlocks.FOLLY_PRESSURE_PLATE);
         entries.add(ContentBlocks.FOLLY_DOOR);
         entries.add(ContentBlocks.FOLLY_TRAPDOOR);
      });
      ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
         entries.add(ACORN);
      });
      ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
         entries.add(MAGNETIZED_TRACKER);
      });
      ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
         entries.add(IRON_SCAFFOLDING);
         entries.add(ContentBlocks.SUSPICIOUS_DIRT);
         entries.add(FOLLY_SIGN);
         entries.add(WRAPPED_BUNDLE);
         entries.add(ContentBlocks.STICKSHIFT);
         entries.add(ContentBlocks.BRAZIER);
         entries.add(ContentBlocks.SOUL_BRAZIER);
         entries.add(ContentBlocks.MUG);
         entries.add(ContentBlocks.AQUARIUM_GLASS);
         entries.add(ContentBlocks.DISPLAY_CASE);
      });
      ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
         entries.add(CHISELED_CHOCOLATE_EGG);
         entries.add(CHOCOLATE_CREWMATE);
         entries.add(CHOCOLATE_EGG);
         entries.add(CHOCOLATE_SQUIRREL);
         entries.add(CHOCOLATE_FROG);
         entries.add(CHOCOLATE_RAVEN);
         entries.add(CHOCOLATE_RAT);
      });
      ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
         entries.add(BOXING_GLOVE);
      });
      ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> {
         entries.add(SQUIRREL_SPAWN_EGG);
      });
      ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(entries -> {
         entries.add(ContentBlocks.EQUIVALENCE_DETECTOR);
      });
      ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
         entries.add(FOLLY_SAPLING);
         entries.add(FOLLY_SEED);
         entries.add(ContentBlocks.MOSS_LAYER);
         entries.add(ContentBlocks.SNOW_LAYER);
         entries.add(ContentBlocks.GIANT_ACORN_BLOCK);
         entries.add(ContentBlocks.ACACIA_BRANCH);
         entries.add(ContentBlocks.BIRCH_BRANCH);
         entries.add(ContentBlocks.CRIMSON_BRANCH);
         entries.add(ContentBlocks.DARK_OAK_BRANCH);
         entries.add(ContentBlocks.FOLLY_LOG);
         entries.add(ContentBlocks.FOLLY_BRANCH);
         entries.add(ContentBlocks.JUNGLE_BRANCH);
         entries.add(ContentBlocks.WARPED_BRANCH);
         entries.add(ContentBlocks.MANGROVE_BRANCH);
         entries.add(ContentBlocks.SPRUCE_BRANCH);
         entries.add(ContentBlocks.OAK_BRANCH);
         entries.add(ContentBlocks.FOLLY_LEAVES);
         entries.add(ContentBlocks.FOLLY_VINES);
      });
      ITEMS.keySet().forEach(item -> Registry.register(Registries.ITEM, ITEMS.get(item), item));
      CompostingChanceRegistry.INSTANCE.add(ACORN, 0.3F);
      CompostingChanceRegistry.INSTANCE.add(ContentBlocks.GIANT_ACORN_BLOCK.asItem(), 1.0F);
      CompostingChanceRegistry.INSTANCE.add(ContentBlocks.MOSS_LAYER.asItem(), 0.3F);
      CompostingChanceRegistry.INSTANCE.add(CHOCOLATE_EGG, 0.3F);
      CompostingChanceRegistry.INSTANCE.add(CHISELED_CHOCOLATE_EGG, 0.3F);
      CompostingChanceRegistry.INSTANCE.add(CHOCOLATE_RAT, 0.3F);
      CompostingChanceRegistry.INSTANCE.add(CHOCOLATE_RAVEN, 0.3F);
      CompostingChanceRegistry.INSTANCE.add(CHOCOLATE_FROG, 0.3F);
      CompostingChanceRegistry.INSTANCE.add(CHOCOLATE_SQUIRREL, 0.3F);
      CompostingChanceRegistry.INSTANCE.add(CHOCOLATE_CREWMATE, 0.3F);
      CompostingChanceRegistry.INSTANCE.add(FOLLY_SAPLING, 0.3F);
      CompostingChanceRegistry.INSTANCE.add(FOLLY_SEED, 0.3F);
      CompostingChanceRegistry.INSTANCE.add(ContentBlocks.FOLLY_LEAVES.asItem(), 0.3F);
      CompostingChanceRegistry.INSTANCE.add(ContentBlocks.FOLLY_VINES.asItem(), 0.5F);
      FuelRegistry.INSTANCE.add(ContentItemTags.BRANCHES_THAT_BURN, 150);
      UniformLootNumberProvider lootTableRange = UniformLootNumberProvider.create(1.0F, 1.0F);
      LootCondition chanceLootCondition = RandomChanceLootCondition.builder(0.05F).build();
      LootTableEvents.MODIFY.register(

              (key, tableBuilder, source) -> {
                         if (LootTables.ABANDONED_MINESHAFT_CHEST.equals(key.getValue()) || LootTables.BURIED_TREASURE_CHEST.equals(key.getValue()) || LootTables.SIMPLE_DUNGEON_CHEST.equals(key.getValue()) || LootTables.ANCIENT_CITY_CHEST.equals(key.getValue())) {
                            LootPool lootPool = LootPool.builder()
                                    .rolls(lootTableRange)
                                    .conditionally(chanceLootCondition)
                                    .with(ItemEntry.builder(FOLLY_SEED).build())
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))).build();
                            tableBuilder.pool(lootPool);
                         }
                      }
              );
      FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
         builder.registerPotionRecipe(
                 Potions.AWKWARD,
                 Ingredient.ofItems(ContentItems.ACORN),
                 Registries.POTION.getEntry(ContentPotions.RUSTLING)
         );
         builder.registerPotionRecipe(
                 Registries.POTION.getEntry(ContentPotions.RUSTLING),
                 Ingredient.ofItems(Items.REDSTONE),
                 Registries.POTION.getEntry(ContentPotions.LONG_RUSTLING)
         );
      });
      registerDispenserBehaviors();
   }

   private static <T extends Item> T create(String name, T item) {
      ITEMS.put(item, Identifier.of("contentcontent", name));
      return item;
   }

   private static void registerDispenserBehaviors() {
      DispenserBlock.registerBehavior(
              Items.GOAT_HORN,
              new FallibleItemDispenserBehavior() {
                 protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                    ServerWorld world = pointer.world();
                    Direction d = (Direction)world.getBlockState(pointer.pos()).get(DispenserBlock.FACING);
                    BlockState state = world.getBlockState(pointer.pos().offset(d));
                    if (state.isAir() || state.isOf(Blocks.NOTE_BLOCK)) {
                       float pitch = state.isOf(Blocks.NOTE_BLOCK)
                               ? (float)Math.pow(2.0, ((Integer)state.get(Properties.NOTE) - 12) / 12.0)
                               : 1.0F;
                       ContentInstruments.getInstrument(stack).ifPresent(entry -> {
                          Instrument instrument = (Instrument)entry.value();
                          world.playSound((Entity) null, pointer.pos(), (SoundEvent) instrument.soundEvent(), SoundCategory.BLOCKS, instrument.range() / 16.0F, pitch);
                       });
                    }

                    return stack;
                 }

                 protected void playSound(BlockPointer pointer) {
                 }
              }
      );
      DispenserBlock.registerBehavior(
              COPPER_HORN,
              new FallibleItemDispenserBehavior() {
                 protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                    Direction d = (Direction)pointer.state().get(DispenserBlock.FACING);
                    ServerWorld world = pointer.world();
                    BlockState state = world.getBlockState(pointer.pos().offset(d));
                    if (state.isAir() || state.isOf(Blocks.NOTE_BLOCK)) {
                       float pitch = state.isOf(Blocks.NOTE_BLOCK)
                               ? (float)Math.pow(2.0, ((Integer)state.get(Properties.NOTE) - 12) / 12.0)
                               : 1.0F;
                       ContentInstruments.getInstrument(stack)
                               .flatMap(entry -> ContentInstruments.getCopperHornInstrument((Instrument)entry.value()))
                               .ifPresent(
                                       copperHornInstrument -> {
                                          Instrument instrument = d.getAxis().isHorizontal()
                                                  ? copperHornInstrument.melody
                                                  : (d.equals(Direction.UP) ? copperHornInstrument.harmony : copperHornInstrument.bass);
                                          world.playSound((Entity) null, pointer.pos(), (SoundEvent) instrument.soundEvent(), SoundCategory.BLOCKS, instrument.range() / 16.0F, pitch);
                                       }
                               );
                    }

                    return stack;
                 }

                 protected void playSound(BlockPointer pointer) {
                 }
              }
      );
   }
}
