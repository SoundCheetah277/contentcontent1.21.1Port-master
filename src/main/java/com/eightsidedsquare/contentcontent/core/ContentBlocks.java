package com.eightsidedsquare.contentcontent.core;

import com.eightsidedsquare.contentcontent.common.block.AquariumGlassBlock;
import com.eightsidedsquare.contentcontent.common.block.BranchBlock;
import com.eightsidedsquare.contentcontent.common.block.BrazierBlock;
import com.eightsidedsquare.contentcontent.common.block.ChocolateBlock;
import com.eightsidedsquare.contentcontent.common.block.DisplayCaseBlock;
import com.eightsidedsquare.contentcontent.common.block.EquivalenceDetectorBlock;
import com.eightsidedsquare.contentcontent.common.block.FollyLogBlock;
import com.eightsidedsquare.contentcontent.common.block.FollySaplingBlock;
import com.eightsidedsquare.contentcontent.common.block.FollyTrunkBlock;
import com.eightsidedsquare.contentcontent.common.block.FollyVinesBlock;
import com.eightsidedsquare.contentcontent.common.block.FollyVinesPlantBlock;
import com.eightsidedsquare.contentcontent.common.block.GiantAcornBlock;
import com.eightsidedsquare.contentcontent.common.block.IronScaffoldingBlock;
import com.eightsidedsquare.contentcontent.common.block.LayerBlock;
import com.eightsidedsquare.contentcontent.common.block.PottedFollySeedBlock;
import com.eightsidedsquare.contentcontent.common.block.SoulBrazierBlock;
import com.eightsidedsquare.contentcontent.common.block.SteelBarBlock;
import com.eightsidedsquare.contentcontent.common.block.StickshiftBlock;
import com.eightsidedsquare.contentcontent.common.block.WrappedBundleBlock;
import com.eightsidedsquare.contentcontent.common.block.mug.HoneyMugBlock;
import com.eightsidedsquare.contentcontent.common.block.mug.LavaMugBlock;
import com.eightsidedsquare.contentcontent.common.block.mug.MilkMugBlock;
import com.eightsidedsquare.contentcontent.common.block.mug.MugBlock;
import com.eightsidedsquare.contentcontent.common.block.mug.PotionMugBlock;
import com.eightsidedsquare.contentcontent.common.block.mug.PowderSnowMugBlock;
import java.util.LinkedHashMap;
import java.util.Map;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.block.AbstractBlock;
import net.minecraft.data.family.BlockFamilies;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class ContentBlocks {
   private static final Map<Block, Identifier> BLOCKS = new LinkedHashMap<>();
   private static final Map<Item, Identifier> ITEMS = new LinkedHashMap<>();
   public static final Block MOSS_LAYER = create(
           "moss_layer",
           new LayerBlock(FabricBlockSettings.create().mapColor(MapColor.GREEN).sounds(BlockSoundGroup.MOSS_CARPET).noCollision().breakInstantly()),
           ItemGroups.FUNCTIONAL
   );
   public static final Block SNOW_LAYER = create(
           "snow_layer",
           new LayerBlock(FabricBlockSettings.create().mapColor(MapColor.WHITE).sounds(BlockSoundGroup.POWDER_SNOW).noCollision().breakInstantly()),
           ItemGroups.FUNCTIONAL
   );
   public static final Block STICKSHIFT = create("stickshift", new StickshiftBlock(AbstractBlock.Settings.copy(Blocks.LEVER)), ItemGroups.REDSTONE);
   public static final Block GIANT_ACORN_BLOCK = create(
           "giant_acorn_block", new GiantAcornBlock(AbstractBlock.Settings.copy(Blocks.CRAFTING_TABLE)), ItemGroups.FUNCTIONAL
   );
   public static final Block IRON_SCAFFOLDING = create(
           "iron_scaffolding",
           new IronScaffoldingBlock(
                   FabricBlockSettings.create()
                           .mapColor(MapColor.IRON_GRAY)
                           .strength(1.0F)
                           .noCollision()
                           .sounds(BlockSoundGroup.LANTERN)
                           .dynamicBounds()
           ),
           null
   );
   public static final Block STEEL_BAR = create("steel_bar", new SteelBarBlock(AbstractBlock.Settings.copy(Blocks.CHAIN)), ItemGroups.FUNCTIONAL);
   public static final Block BRAZIER = create(
           "brazier", new BrazierBlock(AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK).luminance(BrazierBlock::getLuminance)), ItemGroups.FUNCTIONAL
   );
   public static final Block SOUL_BRAZIER = create(
           "soul_brazier", new SoulBrazierBlock(AbstractBlock.Settings.copy(BRAZIER).luminance(SoulBrazierBlock::getLuminance)), ItemGroups.FUNCTIONAL
   );
   public static final Block CHOCOLATE_EGG = create(
           "chocolate_egg",
           new ChocolateBlock(FabricBlockSettings.of().strength(0.2F, 3.0F).nonOpaque().sounds(BlockSoundGroup.CANDLE), 3, 9),
           null
   );
   public static final Block CHISELED_CHOCOLATE_EGG = create(
           "chiseled_chocolate_egg", new ChocolateBlock(FabricBlockSettings.copy(CHOCOLATE_EGG), 4, 8), null
   );
   public static final Block CHOCOLATE_FROG = create("chocolate_frog", new ChocolateBlock(FabricBlockSettings.copy(CHOCOLATE_EGG), 4, 5), null);
   public static final Block CHOCOLATE_RAT = create("chocolate_rat", new ChocolateBlock(FabricBlockSettings.copy(CHOCOLATE_EGG), 4, 6), null);
   public static final Block CHOCOLATE_RAVEN = create("chocolate_raven", new ChocolateBlock(FabricBlockSettings.copy(CHOCOLATE_EGG), 3, 5), null);
   public static final Block CHOCOLATE_SQUIRREL = create(
           "chocolate_squirrel", new ChocolateBlock(FabricBlockSettings.copy(CHOCOLATE_EGG), 4, 10), null
   );
   public static final Block CHOCOLATE_CREWMATE = create(
           "chocolate_crewmate", new ChocolateBlock(FabricBlockSettings.copy(CHOCOLATE_EGG), 3, 8), null
   );
   public static final Block CALCITE_STAIRS = create(
           "calcite_stairs", new StairsBlock(Blocks.CALCITE.getDefaultState(), AbstractBlock.Settings.copy(Blocks.CALCITE)), ItemGroups.BUILDING_BLOCKS
   );
   public static final Block CALCITE_SLAB = create("calcite_slab", new SlabBlock(AbstractBlock.Settings.copy(Blocks.CALCITE)), ItemGroups.BUILDING_BLOCKS);
   public static final Block CALCITE_WALL = create("calcite_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.CALCITE)), ItemGroups.FUNCTIONAL);
   public static final Block POLISHED_CALCITE = create(
           "polished_calcite", new Block(AbstractBlock.Settings.copy(Blocks.CALCITE)), ItemGroups.BUILDING_BLOCKS
   );
   public static final Block POLISHED_CALCITE_STAIRS = create(
           "polished_calcite_stairs", new StairsBlock(POLISHED_CALCITE.getDefaultState(), AbstractBlock.Settings.copy(POLISHED_CALCITE)), ItemGroups.BUILDING_BLOCKS
   );
   public static final Block POLISHED_CALCITE_SLAB = create(
           "polished_calcite_slab", new SlabBlock(AbstractBlock.Settings.copy(POLISHED_CALCITE)), ItemGroups.BUILDING_BLOCKS
   );
   public static final Block POLISHED_CALCITE_WALL = create(
           "polished_calcite_wall", new WallBlock(AbstractBlock.Settings.copy(POLISHED_CALCITE)), ItemGroups.FUNCTIONAL
   );
   public static final Block CALCITE_BRICKS = create(
           "calcite_bricks", new Block(AbstractBlock.Settings.copy(Blocks.CALCITE)), ItemGroups.BUILDING_BLOCKS
   );
   public static final Block CALCITE_BRICK_STAIRS = create(
           "calcite_brick_stairs", new StairsBlock(CALCITE_BRICKS.getDefaultState(), AbstractBlock.Settings.copy(CALCITE_BRICKS)), ItemGroups.BUILDING_BLOCKS
   );
   public static final Block CALCITE_BRICK_SLAB = create(
           "calcite_brick_slab", new SlabBlock(AbstractBlock.Settings.copy(CALCITE_BRICKS)), ItemGroups.BUILDING_BLOCKS
   );
   public static final Block CALCITE_BRICK_WALL = create(
           "calcite_brick_wall", new WallBlock(AbstractBlock.Settings.copy(CALCITE_BRICKS)), ItemGroups.FUNCTIONAL
   );
   public static final Block TUFF_STAIRS = create(
           "tuff_stairs", new StairsBlock(Blocks.TUFF.getDefaultState(), AbstractBlock.Settings.copy(Blocks.TUFF)), ItemGroups.BUILDING_BLOCKS
   );
   public static final Block TUFF_SLAB = create("tuff_slab", new SlabBlock(AbstractBlock.Settings.copy(Blocks.TUFF)), ItemGroups.BUILDING_BLOCKS);
   public static final Block TUFF_WALL = create("tuff_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.TUFF)), ItemGroups.FUNCTIONAL);
   public static final Block POLISHED_TUFF = create("polished_tuff", new Block(AbstractBlock.Settings.copy(Blocks.TUFF)), ItemGroups.BUILDING_BLOCKS);
   public static final Block POLISHED_TUFF_STAIRS = create(
           "polished_tuff_stairs", new StairsBlock(POLISHED_TUFF.getDefaultState(), AbstractBlock.Settings.copy(Blocks.TUFF)), ItemGroups.BUILDING_BLOCKS
   );
   public static final Block POLISHED_TUFF_SLAB = create(
           "polished_tuff_slab", new SlabBlock(AbstractBlock.Settings.copy(Blocks.TUFF)), ItemGroups.BUILDING_BLOCKS
   );
   public static final Block POLISHED_TUFF_WALL = create(
           "polished_tuff_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.TUFF)), ItemGroups.FUNCTIONAL
   );
   public static final Block TUFF_BRICKS = create("tuff_bricks", new Block(AbstractBlock.Settings.copy(Blocks.TUFF)), ItemGroups.BUILDING_BLOCKS);
   public static final Block TUFF_BRICK_STAIRS = create(
           "tuff_brick_stairs", new StairsBlock(TUFF_BRICKS.getDefaultState(), AbstractBlock.Settings.copy(Blocks.TUFF)), ItemGroups.BUILDING_BLOCKS
   );
   public static final Block TUFF_BRICK_SLAB = create(
           "tuff_brick_slab", new SlabBlock(AbstractBlock.Settings.copy(Blocks.TUFF)), ItemGroups.BUILDING_BLOCKS
   );
   public static final Block TUFF_BRICK_WALL = create(
           "tuff_brick_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.TUFF)), ItemGroups.FUNCTIONAL
   );
   public static final Block SMOOTH_BASALT_STAIRS = create(
           "smooth_basalt_stairs", new StairsBlock(Blocks.SMOOTH_BASALT.getDefaultState(), AbstractBlock.Settings.copy(Blocks.SMOOTH_BASALT)), ItemGroups.BUILDING_BLOCKS
   );
   public static final Block SMOOTH_BASALT_SLAB = create(
           "smooth_basalt_slab", new SlabBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_BASALT)), ItemGroups.BUILDING_BLOCKS
   );
   public static final Block SMOOTH_BASALT_WALL = create(
           "smooth_basalt_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_BASALT)), ItemGroups.FUNCTIONAL
   );
   public static final Block SMOOTH_SANDSTONE_WALL = create(
           "smooth_sandstone_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_SANDSTONE)), ItemGroups.FUNCTIONAL
   );
   public static final Block SMOOTH_RED_SANDSTONE_WALL = create(
           "smooth_red_sandstone_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_RED_SANDSTONE)), ItemGroups.FUNCTIONAL
   );
   public static final Block POLISHED_ANDESITE_WALL = create(
           "polished_andesite_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE)), ItemGroups.FUNCTIONAL
   );
   public static final Block POLISHED_DIORITE_WALL = create(
           "polished_diorite_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_DIORITE)), ItemGroups.FUNCTIONAL
   );
   public static final Block POLISHED_GRANITE_WALL = create(
           "polished_granite_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_GRANITE)), ItemGroups.FUNCTIONAL
   );
   public static final Block QUARTZ_WALL = create("quartz_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.QUARTZ_BLOCK)), ItemGroups.FUNCTIONAL);
   public static final Block SMOOTH_QUARTZ_WALL = create(
           "smooth_quartz_wall", new WallBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_QUARTZ)), ItemGroups.FUNCTIONAL
   );
   public static final Block MUG = create(
           "mug",
           new MugBlock(FabricBlockSettings.create().mapColor(MapColor.BROWN).nonOpaque().sounds(BlockSoundGroup.WOOD).strength(0.5F)),
           ItemGroups.FUNCTIONAL
   );
   public static final Block LAVA_MUG = create("lava_mug", new LavaMugBlock(AbstractBlock.Settings.copy(MUG).luminance(LavaMugBlock::getLight)), null);
   public static final Block MILK_MUG = create("milk_mug", new MilkMugBlock(AbstractBlock.Settings.copy(MUG)), null);
   public static final Block POTION_MUG = create("potion_mug", new PotionMugBlock(AbstractBlock.Settings.copy(MUG), false), null);
   public static final Block PILK_MUG = create("pilk_mug", new PotionMugBlock(AbstractBlock.Settings.copy(MUG), true), null);
   public static final Block POWDER_SNOW_MUG = create("powder_snow_mug", new PowderSnowMugBlock(AbstractBlock.Settings.copy(MUG)), null);
   public static final Block HONEY_MUG = create("honey_mug", new HoneyMugBlock(AbstractBlock.Settings.copy(MUG)), null);
   public static final Block STRIPPED_OAK_BRANCH = create(
           "stripped_oak_branch", new BranchBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_OAK_WOOD).strength(1.5F), null), ItemGroups.FUNCTIONAL
   );
   public static final Block STRIPPED_SPRUCE_BRANCH = create(
           "stripped_spruce_branch", new BranchBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_SPRUCE_WOOD).strength(1.5F), null), ItemGroups.FUNCTIONAL
   );
   public static final Block STRIPPED_BIRCH_BRANCH = create(
           "stripped_birch_branch", new BranchBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_BIRCH_WOOD).strength(1.5F), null), ItemGroups.FUNCTIONAL
   );
   public static final Block STRIPPED_JUNGLE_BRANCH = create(
           "stripped_jungle_branch", new BranchBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_JUNGLE_WOOD).strength(1.5F), null), ItemGroups.FUNCTIONAL
   );
   public static final Block STRIPPED_ACACIA_BRANCH = create(
           "stripped_acacia_branch", new BranchBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_ACACIA_WOOD).strength(1.5F), null), ItemGroups.FUNCTIONAL
   );
   public static final Block STRIPPED_DARK_OAK_BRANCH = create(
           "stripped_dark_oak_branch", new BranchBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_DARK_OAK_WOOD).strength(1.5F), null), ItemGroups.FUNCTIONAL
   );
   public static final Block STRIPPED_CRIMSON_BRANCH = create(
           "stripped_crimson_branch", new BranchBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_CRIMSON_HYPHAE).strength(1.5F), null), ItemGroups.FUNCTIONAL
   );
   public static final Block STRIPPED_WARPED_BRANCH = create(
           "stripped_warped_branch", new BranchBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_WARPED_HYPHAE).strength(1.5F), null), ItemGroups.FUNCTIONAL
   );
   public static final Block STRIPPED_MANGROVE_BRANCH = create(
           "stripped_mangrove_branch",
           new BranchBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_OAK_WOOD).mapColor(MapColor.RED).strength(1.5F), null),
           ItemGroups.FUNCTIONAL
   );
   public static final Block STRIPPED_FOLLY_BRANCH = create(
           "stripped_folly_branch",
           new BranchBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_ACACIA_WOOD).mapColor(MapColor.PINK).strength(1.5F), null),
           ItemGroups.FUNCTIONAL
   );
   public static final Block OAK_BRANCH = create(
           "oak_branch", new BranchBlock(AbstractBlock.Settings.copy(Blocks.OAK_WOOD).strength(1.5F), (BranchBlock)STRIPPED_OAK_BRANCH), ItemGroups.FUNCTIONAL
   );
   public static final Block SPRUCE_BRANCH = create(
           "spruce_branch",
           new BranchBlock(AbstractBlock.Settings.copy(Blocks.SPRUCE_WOOD).strength(1.5F), (BranchBlock)STRIPPED_SPRUCE_BRANCH),
           ItemGroups.FUNCTIONAL
   );
   public static final Block BIRCH_BRANCH = create(
           "birch_branch",
           new BranchBlock(AbstractBlock.Settings.copy(Blocks.BIRCH_WOOD).strength(1.5F), (BranchBlock)STRIPPED_BIRCH_BRANCH),
           ItemGroups.FUNCTIONAL
   );
   public static final Block JUNGLE_BRANCH = create(
           "jungle_branch",
           new BranchBlock(AbstractBlock.Settings.copy(Blocks.JUNGLE_WOOD).strength(1.5F), (BranchBlock)STRIPPED_JUNGLE_BRANCH),
           ItemGroups.FUNCTIONAL
   );
   public static final Block ACACIA_BRANCH = create(
           "acacia_branch",
           new BranchBlock(AbstractBlock.Settings.copy(Blocks.ACACIA_WOOD).strength(1.5F), (BranchBlock)STRIPPED_ACACIA_BRANCH),
           ItemGroups.FUNCTIONAL
   );
   public static final Block DARK_OAK_BRANCH = create(
           "dark_oak_branch",
           new BranchBlock(AbstractBlock.Settings.copy(Blocks.DARK_OAK_WOOD).strength(1.5F), (BranchBlock)STRIPPED_DARK_OAK_BRANCH),
           ItemGroups.FUNCTIONAL
   );
   public static final Block CRIMSON_BRANCH = create(
           "crimson_branch",
           new BranchBlock(AbstractBlock.Settings.copy(Blocks.CRIMSON_HYPHAE).strength(1.5F), (BranchBlock)STRIPPED_CRIMSON_BRANCH),
           ItemGroups.FUNCTIONAL
   );
   public static final Block WARPED_BRANCH = create(
           "warped_branch",
           new BranchBlock(AbstractBlock.Settings.copy(Blocks.WARPED_HYPHAE).strength(1.5F), (BranchBlock)STRIPPED_WARPED_BRANCH),
           ItemGroups.FUNCTIONAL
   );
   public static final Block MANGROVE_BRANCH = create(
           "mangrove_branch",
           new BranchBlock(AbstractBlock.Settings.copy(Blocks.MANGROVE_WOOD).strength(1.5F), (BranchBlock)STRIPPED_MANGROVE_BRANCH),
           ItemGroups.FUNCTIONAL
   );
   public static final Block FOLLY_BRANCH = create(
           "folly_branch",
           new BranchBlock(AbstractBlock.Settings.copy(Blocks.ACACIA_WOOD).strength(1.5F).mapColor(MapColor.PINK), (BranchBlock)STRIPPED_FOLLY_BRANCH),
           ItemGroups.FUNCTIONAL
   );
   public static final Block AQUARIUM_GLASS = create(
           "aquarium_glass", new AquariumGlassBlock(AbstractBlock.Settings.copy(Blocks.GLASS_PANE)), ItemGroups.FUNCTIONAL
   );
   public static final Block EQUIVALENCE_DETECTOR = create(
           "equivalence_detector", new EquivalenceDetectorBlock(AbstractBlock.Settings.copy(Blocks.OBSERVER)), ItemGroups.REDSTONE
   );
   public static final Block DISPLAY_CASE = create(
           "display_case", new DisplayCaseBlock(AbstractBlock.Settings.copy(Blocks.MANGROVE_FENCE)), ItemGroups.FUNCTIONAL
   );
   public static final Block WRAPPED_BUNDLE = create(
           "wrapped_bundle",
           new WrappedBundleBlock(
                   FabricBlockSettings.create().mapColor(MapColor.OAK_TAN).strength(0.1F).sounds(BlockSoundGroup.WOOL).nonOpaque()
           ),
           null
   );
   public static final Block FOLLY_LEAVES = create(
           "folly_leaves",
           new LeavesBlock(
                   AbstractBlock.Settings.copy(Blocks.FLOWERING_AZALEA_LEAVES)
                           .nonOpaque()
                           .allowsSpawning(Blocks::canSpawnOnLeaves)
                           .suffocates(Blocks::never)
                           .blockVision(Blocks::never)
                           .mapColor(MapColor.PINK)
           ),
           ItemGroups.FUNCTIONAL
   );
   public static final Block FOLLY_VINES = create(
           "folly_vines",
           new FollyVinesBlock(
                   FabricBlockSettings.create()
                           .ticksRandomly()
                           .noCollision()
                           .breakInstantly()
                           .sounds(BlockSoundGroup.CAVE_VINES)
                           .mapColor(MapColor.PINK)
           ),
           ItemGroups.FUNCTIONAL
   );
   public static final Block FOLLY_VINES_PLANT = create(
           "folly_vines_plant",
           new FollyVinesPlantBlock(
                   FabricBlockSettings.create().noCollision().breakInstantly().sounds(BlockSoundGroup.CAVE_VINES).mapColor(MapColor.PINK)
           ),
           null
   );
   public static final Block FOLLY_LOG = create(
           "folly_log",
           new FollyLogBlock(FabricBlockSettings.create().mapColor(MapColor.PINK).strength(2.0F).sounds(BlockSoundGroup.WOOD)),
           ItemGroups.BUILDING_BLOCKS
   );
   public static final Block FOLLY_WOOD = create(
           "folly_wood",
           new FollyLogBlock(FabricBlockSettings.create().mapColor(MapColor.PINK).strength(2.0F).sounds(BlockSoundGroup.WOOD)),
           ItemGroups.BUILDING_BLOCKS
   );
   public static final Block STRIPPED_FOLLY_LOG = create(
           "stripped_folly_log",
           new PillarBlock(FabricBlockSettings.create().mapColor(MapColor.PINK).strength(2.0F).sounds(BlockSoundGroup.WOOD)),
           ItemGroups.BUILDING_BLOCKS
   );
   public static final Block STRIPPED_FOLLY_WOOD = create(
           "stripped_folly_wood",
           new PillarBlock(FabricBlockSettings.create().mapColor(MapColor.PINK).strength(2.0F).sounds(BlockSoundGroup.WOOD)),
           ItemGroups.BUILDING_BLOCKS
   );
   public static final Block FOLLY_PLANKS = create(
           "folly_planks", new Block(AbstractBlock.Settings.copy(Blocks.ACACIA_PLANKS).mapColor(MapColor.PINK)), ItemGroups.BUILDING_BLOCKS
   );
   public static final Block FOLLY_STAIRS = create(
           "folly_stairs",
           new StairsBlock(FOLLY_PLANKS.getDefaultState(), AbstractBlock.Settings.copy(Blocks.ACACIA_STAIRS).mapColor(MapColor.PINK)),
           ItemGroups.BUILDING_BLOCKS
   );
   public static final Block FOLLY_SLAB = create(
           "folly_slab", new SlabBlock(AbstractBlock.Settings.copy(Blocks.ACACIA_SLAB).mapColor(MapColor.PINK)), ItemGroups.BUILDING_BLOCKS
   );
   public static final Block FOLLY_FENCE = create(
           "folly_fence", new FenceBlock(AbstractBlock.Settings.copy(Blocks.ACACIA_FENCE).mapColor(MapColor.PINK)), ItemGroups.FUNCTIONAL
   );
   public static final Block FOLLY_FENCE_GATE = create(
           "folly_fence_gate",
           new FenceGateBlock(
                   WoodType.ACACIA,
                   AbstractBlock.Settings.copy(Blocks.ACACIA_FENCE_GATE).mapColor(MapColor.PINK)
                      ),
           ItemGroups.REDSTONE
   );
   public static final Block FOLLY_BUTTON = create(
           "folly_button",
           new ButtonBlock(
                   BlockSetType.ACACIA,
                   30,
                   AbstractBlock.Settings.copy(Blocks.ACACIA_BUTTON).mapColor(MapColor.PINK)
           ),
           ItemGroups.REDSTONE
   );
   public static final Block FOLLY_PRESSURE_PLATE = create(
           "folly_pressure_plate",
           new PressurePlateBlock(
                   BlockSetType.ACACIA, // Use BlockSetType instead of ActivationRule
                   AbstractBlock.Settings.copy(Blocks.ACACIA_PRESSURE_PLATE).mapColor(MapColor.PINK)
           ),
           ItemGroups.REDSTONE
   );
   public static final Block FOLLY_DOOR = create(
           "folly_door",
           new DoorBlock(
                   BlockSetType.ACACIA,
                   AbstractBlock.Settings.copy(Blocks.ACACIA_DOOR).mapColor(MapColor.PINK)
                      ),
           ItemGroups.REDSTONE
   );
   public static final Block FOLLY_TRAPDOOR = create(
           "folly_trapdoor",
           new TrapdoorBlock(
                   BlockSetType.ACACIA,
                   AbstractBlock.Settings.copy(Blocks.ACACIA_TRAPDOOR).mapColor(MapColor.PINK)
                      ),
           ItemGroups.REDSTONE
   );
   public static final Block FOLLY_TRUNK = create("folly_trunk", new FollyTrunkBlock(AbstractBlock.Settings.copy(FOLLY_LOG).ticksRandomly()), null);
   public static final Block FOLLY_SAPLING = create(
           "folly_sapling", new FollySaplingBlock(AbstractBlock.Settings.copy(Blocks.ACACIA_SAPLING).mapColor(MapColor.PINK)), null
   );
   public static final Block FOLLY_SIGN = create(
           "folly_sign", new SignBlock(ContentMod.FOLLY, AbstractBlock.Settings.copy(Blocks.ACACIA_SIGN).mapColor(MapColor.PINK)), null
   );
   public static final Block FOLLY_WALL_SIGN = create(
           "folly_wall_sign", new WallSignBlock(ContentMod.FOLLY, AbstractBlock.Settings.copy(Blocks.ACACIA_SIGN).mapColor(MapColor.PINK)), null
   );
   public static final Block POTTED_FOLLY_SAPLING = create(
           "potted_folly_sapling", new FlowerPotBlock(FOLLY_SAPLING, FabricBlockSettings.create().breakInstantly().nonOpaque()), null
   );
   public static final Block POTTED_FOLLY_SEED = create(
           "potted_folly_seed", new PottedFollySeedBlock(FabricBlockSettings.create().breakInstantly().nonOpaque().ticksRandomly()), null
   );
   public static final Block SUSPICIOUS_DIRT = create(
           "suspicious_dirt",
           new BrushableBlock(
                   Blocks.DIRT,
                   SoundEvents.ITEM_BRUSH_BRUSHING_GRAVEL,
                   SoundEvents.ITEM_BRUSH_BRUSHING_GRAVEL_COMPLETE,
                   AbstractBlock.Settings.copy(Blocks.SUSPICIOUS_GRAVEL)
                           .mapColor(MapColor.DIRT_BROWN)
                           .strength(0.25F)
                           .pistonBehavior(PistonBehavior.DESTROY)
           ),
           ItemGroups.FUNCTIONAL  // or null if you don't want a BlockItem
   );

   public static final BlockFamily FOLLY_FAMILY = BlockFamilies.register(FOLLY_PLANKS)
           .slab(FOLLY_SLAB)
           .stairs(FOLLY_STAIRS)
           .fence(FOLLY_FENCE)
           .fenceGate(FOLLY_FENCE_GATE)
           .button(FOLLY_BUTTON)
           .pressurePlate(FOLLY_PRESSURE_PLATE)
           .door(FOLLY_DOOR)
           .trapdoor(FOLLY_TRAPDOOR)
           .sign(FOLLY_SIGN, FOLLY_WALL_SIGN)
           .build();

   private static <T extends Block> T create(String name, T block, RegistryKey<ItemGroup> itemGroup) {
      BLOCKS.put(block, Identifier.of("contentcontent", name));
      if (itemGroup != null) {
         ITEMS.put(new BlockItem(block, new Item.Settings()), BLOCKS.get(block));
      }
      return block;
   }


   public static void init() {
      BLOCKS.keySet().forEach(block -> Registry.register(Registries.BLOCK, BLOCKS.get(block), block));
      ITEMS.keySet().forEach(item -> Registry.register(Registries.ITEM, ITEMS.get(item), item));
      StrippableBlockRegistry.register(FOLLY_LOG, STRIPPED_FOLLY_LOG);
      StrippableBlockRegistry.register(FOLLY_WOOD, STRIPPED_FOLLY_WOOD);
      FlammableBlockRegistry.getDefaultInstance().add(GIANT_ACORN_BLOCK, 5, 20);
      FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_OAK_BRANCH, 10, 5);
      FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_SPRUCE_BRANCH, 10, 5);
      FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_BIRCH_BRANCH, 10, 5);
      FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_JUNGLE_BRANCH, 10, 5);
      FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_ACACIA_BRANCH, 10, 5);
      FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_DARK_OAK_BRANCH, 10, 5);
      FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_MANGROVE_BRANCH, 10, 5);
      FlammableBlockRegistry.getDefaultInstance().add(OAK_BRANCH, 10, 5);
      FlammableBlockRegistry.getDefaultInstance().add(SPRUCE_BRANCH, 10, 5);
      FlammableBlockRegistry.getDefaultInstance().add(BIRCH_BRANCH, 10, 5);
      FlammableBlockRegistry.getDefaultInstance().add(JUNGLE_BRANCH, 10, 5);
      FlammableBlockRegistry.getDefaultInstance().add(ACACIA_BRANCH, 10, 5);
      FlammableBlockRegistry.getDefaultInstance().add(DARK_OAK_BRANCH, 10, 5);
      FlammableBlockRegistry.getDefaultInstance().add(MANGROVE_BRANCH, 10, 5);
   }
}
