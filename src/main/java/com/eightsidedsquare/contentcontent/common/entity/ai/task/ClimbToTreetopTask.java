package com.eightsidedsquare.contentcontent.common.entity.ai.task;

import com.eightsidedsquare.contentcontent.common.entity.ai.CMemoryModuleType;
import com.eightsidedsquare.contentcontent.common.entity.squirrel.SquirrelEntity;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.BlockPosLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;

public class ClimbToTreetopTask extends MultiTickTask<SquirrelEntity> {
   public ClimbToTreetopTask() {
      super(ImmutableMap.of(
              CMemoryModuleType.NEAREST_TREETOP, MemoryModuleState.VALUE_PRESENT,
              CMemoryModuleType.IS_WAITING, MemoryModuleState.VALUE_ABSENT,
              CMemoryModuleType.CLIMBED_TREE_RECENTLY, MemoryModuleState.VALUE_ABSENT
      ), 200);
   }

   @Override
   protected boolean shouldRun(ServerWorld world, SquirrelEntity entity) {
      return entity.getBrain().getOptionalMemory(CMemoryModuleType.NEAREST_TREETOP).isPresent()
              && entity.getBrain().isMemoryInState(CMemoryModuleType.IS_WAITING, MemoryModuleState.VALUE_ABSENT)
              && !entity.getBrain().getOptionalMemory(CMemoryModuleType.NEAREST_TREETOP).get().isWithinDistance(entity.getPos(), 1.0)
              && entity.getClimbTime() >= 0;
   }

   @Override
   protected boolean shouldKeepRunning(ServerWorld world, SquirrelEntity entity, long time) {
      return this.shouldRun(world, entity);
   }

   @Override
   protected void run(ServerWorld world, SquirrelEntity entity, long time) {
      entity.getBrain().getOptionalMemory(CMemoryModuleType.NEAREST_TREETOP).ifPresent(blockPos -> {
         entity.getBrain().remember(MemoryModuleType.WALK_TARGET, new WalkTarget(blockPos, 2.0F, 0));
         entity.getBrain().remember(MemoryModuleType.LOOK_TARGET, new BlockPosLookTarget(blockPos));
      });
   }

   @Override
   protected void keepRunning(ServerWorld world, SquirrelEntity entity, long time) {
   }

   @Override
   protected void finishRunning(ServerWorld world, SquirrelEntity entity, long time) {
      entity.getBrain().forget(MemoryModuleType.WALK_TARGET);
      entity.getBrain().forget(MemoryModuleType.LOOK_TARGET);

      entity.getBrain().getOptionalMemory(CMemoryModuleType.NEAREST_TREETOP).ifPresent(blockPos -> {
         if (blockPos.isWithinDistance(entity.getPos(), 1.0)) {
            Random random = entity.getRandom();
            int waitTime = UniformIntProvider.create(200, 1200).get(random);
            entity.getBrain().remember(CMemoryModuleType.IS_WAITING, true, waitTime);
            entity.getBrain().remember(
                    CMemoryModuleType.CLIMBED_TREE_RECENTLY,
                    true,
                    UniformIntProvider.create(400, 800).get(random) + waitTime
            );
         }
      });
   }
}