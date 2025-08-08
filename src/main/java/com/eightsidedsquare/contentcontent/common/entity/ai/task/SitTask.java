package com.eightsidedsquare.contentcontent.common.entity.ai.task;

import com.eightsidedsquare.contentcontent.common.entity.ai.CMemoryModuleType;
import com.eightsidedsquare.contentcontent.common.entity.squirrel.SquirrelEntity;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.server.world.ServerWorld;

public class SitTask extends MultiTickTask<SquirrelEntity> {
   public SitTask() {
      super(ImmutableMap.of(
              CMemoryModuleType.IS_WAITING, MemoryModuleState.VALUE_PRESENT,
              MemoryModuleType.AVOID_TARGET, MemoryModuleState.VALUE_ABSENT
      ), 1000);
   }

   @Override
   protected boolean shouldRun(ServerWorld world, SquirrelEntity entity) {
      return entity.getBrain().isMemoryInState(CMemoryModuleType.IS_WAITING, MemoryModuleState.VALUE_PRESENT)
              && entity.getBrain().isMemoryInState(MemoryModuleType.AVOID_TARGET, MemoryModuleState.VALUE_ABSENT);
   }

   @Override
   protected boolean shouldKeepRunning(ServerWorld world, SquirrelEntity entity, long time) {
      return entity.getBrain().isMemoryInState(CMemoryModuleType.IS_WAITING, MemoryModuleState.VALUE_PRESENT)
              && entity.getBrain().isMemoryInState(MemoryModuleType.AVOID_TARGET, MemoryModuleState.VALUE_ABSENT);
   }

   @Override
   protected void run(ServerWorld world, SquirrelEntity entity, long time) {
      Brain<?> brain = entity.getBrain();
      brain.forget(MemoryModuleType.WALK_TARGET);
      brain.forget(MemoryModuleType.LOOK_TARGET);
      brain.forget(MemoryModuleType.PATH);
   }

   @Override
   protected void keepRunning(ServerWorld world, SquirrelEntity entity, long time) {
   }

   @Override
   protected void finishRunning(ServerWorld world, SquirrelEntity entity, long time) {
      Brain<?> brain = entity.getBrain();
      brain.resetPossibleActivities();
      brain.forget(CMemoryModuleType.NEAREST_TREETOP);
   }
}
