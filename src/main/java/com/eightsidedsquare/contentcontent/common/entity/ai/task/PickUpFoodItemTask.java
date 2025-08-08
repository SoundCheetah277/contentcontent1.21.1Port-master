package com.eightsidedsquare.contentcontent.common.entity.ai.task;

import com.eightsidedsquare.contentcontent.common.entity.ai.CMemoryModuleType;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;

public class PickUpFoodItemTask extends MultiTickTask<MobEntity> {
   public PickUpFoodItemTask() {
      super(ImmutableMap.of(
              CMemoryModuleType.NEAREST_FOOD_ITEM_ENTITY, MemoryModuleState.VALUE_PRESENT,
              CMemoryModuleType.IS_WAITING, MemoryModuleState.VALUE_ABSENT,
              CMemoryModuleType.ATE_RECENTLY, MemoryModuleState.VALUE_ABSENT
      ));
   }

   @Override
   protected boolean shouldRun(ServerWorld world, MobEntity entity) {
      return entity.getBrain().getOptionalMemory(CMemoryModuleType.NEAREST_FOOD_ITEM_ENTITY).isPresent();
   }

   @Override
   protected void run(ServerWorld world, MobEntity entity, long time) {
      Brain<?> brain = entity.getBrain();
      brain.getOptionalMemory(CMemoryModuleType.NEAREST_FOOD_ITEM_ENTITY).ifPresent(foodEntity -> {
         LookTargetUtil.walkTowards(entity, foodEntity, 1.5F, 0);
      });
   }

   @Override
   protected boolean shouldKeepRunning(ServerWorld world, MobEntity entity, long time) {
      return entity.getBrain().getOptionalMemory(CMemoryModuleType.NEAREST_FOOD_ITEM_ENTITY).isPresent();
   }

   @Override
   protected void finishRunning(ServerWorld world, MobEntity entity, long time) {
      entity.getBrain().forget(MemoryModuleType.WALK_TARGET);
   }
}