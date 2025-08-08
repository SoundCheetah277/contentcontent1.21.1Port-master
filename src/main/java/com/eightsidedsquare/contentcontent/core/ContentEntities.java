package com.eightsidedsquare.contentcontent.core;

import com.eightsidedsquare.contentcontent.common.entity.DisplayCaseBlockEntity;
import com.eightsidedsquare.contentcontent.common.entity.PotionMugBlockEntity;
import com.eightsidedsquare.contentcontent.common.entity.WrappedBundleBlockEntity;
import com.eightsidedsquare.contentcontent.common.entity.squirrel.SquirrelEntity;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BrushableBlockEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;

public class ContentEntities {
   public static final EntityType<SquirrelEntity> SQUIRREL = (EntityType<SquirrelEntity>)Registry.register(
           Registries.ENTITY_TYPE,
           Identifier.of("contentcontent", "squirrel"),
           FabricEntityTypeBuilder.createMob()
                   .entityFactory(SquirrelEntity::new)
                   .defaultAttributes(SquirrelEntity::createAttributes)
                   .spawnGroup(SpawnGroup.CREATURE)
                   .spawnRestriction(SpawnRestriction.SpawnPredicate.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::isValidNaturalSpawn)
                   .dimensions(EntityDimensions.changing(0.6F, 0.55F))
                   .build()
   );
   public static final BlockEntityType<PotionMugBlockEntity> POTION_MUG = (BlockEntityType<PotionMugBlockEntity>)Registry.register(
           Registries.BLOCK_ENTITY_TYPE,
           Identifier.of("contentcontent", "potion_mug"),
           FabricBlockEntityTypeBuilder.create(PotionMugBlockEntity::new, new Block[]{ContentBlocks.POTION_MUG, ContentBlocks.PILK_MUG}).build()
   );
   public static final BlockEntityType<DisplayCaseBlockEntity> DISPLAY_CASE = (BlockEntityType<DisplayCaseBlockEntity>) Registry.register(
           Registries.BLOCK_ENTITY_TYPE,
           Identifier.of("contentcontent", "display_case"),
           FabricBlockEntityTypeBuilder.create(DisplayCaseBlockEntity::new, new Block[]{ContentBlocks.DISPLAY_CASE}).build()
   );
   public static final BlockEntityType<WrappedBundleBlockEntity> WRAPPED_BUNDLE = (BlockEntityType<WrappedBundleBlockEntity>)Registry.register(
           Registries.BLOCK_ENTITY_TYPE,
           Identifier.of("contentcontent", "wrapped_bundle"),
           FabricBlockEntityTypeBuilder.create(WrappedBundleBlockEntity::new, new Block[]{ContentBlocks.WRAPPED_BUNDLE}).build()
   );
   public static final BlockEntityType<BrushableBlockEntity> BRUSHABLE_BLOCK = (BlockEntityType<BrushableBlockEntity>)Registry.register(
           Registries.BLOCK_ENTITY_TYPE,
           Identifier.of("contentcontent", "brushable_block"),
           FabricBlockEntityTypeBuilder.create(BrushableBlockEntity::new, new Block[]{ContentBlocks.SUSPICIOUS_DIRT}).build()
   );

   public static void init() {
      BiomeModifications.addSpawn(biome -> biome.getBiomeRegistryEntry().isIn(BiomeTags.IS_FOREST), SpawnGroup.CREATURE, SQUIRREL, 50, 2, 5);
   }
}
