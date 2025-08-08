package com.eightsidedsquare.contentcontent.common.entity.ai.task;

import com.eightsidedsquare.contentcontent.common.entity.ai.CMemoryModuleType;
import com.eightsidedsquare.contentcontent.common.entity.squirrel.SquirrelEntity;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;

import java.util.List;

public class SquirrelDigTask extends MultiTickTask<SquirrelEntity> {
   private int runTime;

   public SquirrelDigTask() {
      super(ImmutableMap.of(
              CMemoryModuleType.DUG_RECENTLY, MemoryModuleState.VALUE_ABSENT,
              CMemoryModuleType.ATE_RECENTLY, MemoryModuleState.VALUE_ABSENT,
              MemoryModuleType.AVOID_TARGET, MemoryModuleState.VALUE_ABSENT,
              CMemoryModuleType.IS_WAITING, MemoryModuleState.VALUE_ABSENT
      ), 100);
   }

   @Override
   protected boolean shouldRun(ServerWorld world, SquirrelEntity entity) {
      return !entity.isEating()
              && !entity.isClimbing()
              && entity.isOnGround()
              && !entity.isDigging()
              && isOnDigBlock(world, entity)
              && !entity.isBaby();
   }

   @Override
   protected boolean shouldKeepRunning(ServerWorld world, SquirrelEntity entity, long time) {
      return !entity.isEating() && !entity.isClimbing() && entity.isOnGround() && isOnDigBlock(world, entity) && entity.isDigging();
   }

   private boolean isOnDigBlock(ServerWorld world, SquirrelEntity entity) {
      return world.getBlockState(entity.getBlockPos().down()).isIn(BlockTags.DIRT);
   }

   @Override
   protected void run(ServerWorld world, SquirrelEntity entity, long time) {
      this.runTime = 0;
      entity.getBrain().remember(CMemoryModuleType.IS_DIGGING, true);
      entity.setDigging(true);
   }

   @Override
   protected void keepRunning(ServerWorld world, SquirrelEntity entity, long time) {
      this.runTime++;
      world.spawnParticles(
              new BlockStateParticleEffect(ParticleTypes.BLOCK, world.getBlockState(entity.getBlockPos().down())),
              entity.getX(),
              entity.getY(),
              entity.getZ(),
              1,
              0.1,
              0.1,
              0.1,
              0.0
      );
      if (entity.getRandom().nextInt(3) == 1) {
         entity.playDigSound();
      }
   }

   @Override
   protected void finishRunning(ServerWorld world, SquirrelEntity entity, long time) {
      entity.getBrain().remember(CMemoryModuleType.DUG_RECENTLY, true, UniformIntProvider.create(600, 2400).get(entity.getRandom()));
      entity.getBrain().forget(CMemoryModuleType.IS_DIGGING);

      int lootDigsLeft = entity.getBrain().getOptionalMemory(CMemoryModuleType.LOOT_DIGS_LEFT).orElse(0);
      if (this.runTime >= 90 && lootDigsLeft > 0) {
         LootTable lootTable = world.getServer().getLootManager().getLootTable(
                 Identifier.of("contentcontent", "gameplay/squirrel_dig"));

         LootContextParameterSet.Builder builder = new LootContextParameterSet.Builder(world)
                 .add(LootContextParameters.ORIGIN, entity.getPos())
                 .add(LootContextParameters.THIS_ENTITY, entity);

         List<ItemStack> loot = lootTable.generateLoot(builder.build(LootContextTypes.GIFT), entity.getRandom().nextLong());

         entity.getBrain().remember(CMemoryModuleType.LOOT_DIGS_LEFT, lootDigsLeft - 1);

         for (ItemStack itemStack : loot) {
            ItemEntity itemEntity = new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), itemStack);
            itemEntity.setVelocity(
                    world.getRandom().nextDouble() * 0.1 - 0.05,
                    0.2,
                    world.getRandom().nextDouble() * 0.1 - 0.05
            );
            world.spawnEntity(itemEntity);
         }
      }
   }
}