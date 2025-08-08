package com.eightsidedsquare.contentcontent.common.entity.ai.sensor;

import com.eightsidedsquare.contentcontent.common.entity.ai.CMemoryModuleType;
import com.eightsidedsquare.contentcontent.common.entity.squirrel.SquirrelEntity;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.server.world.ServerWorld;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SquirrelAvoidSensor extends Sensor<SquirrelEntity> {
   protected void sense(ServerWorld world, SquirrelEntity entity) {
      Brain<?> brain = entity.getBrain();
      if (brain.getOptionalMemory(CMemoryModuleType.NEAREST_FOOD_ITEM_ENTITY).isPresent()) {
         brain.forget(MemoryModuleType.AVOID_TARGET);
      } else {
         List<LivingEntity> list = world.getEntitiesByClass(
                 LivingEntity.class, entity.getBoundingBox().expand(12.0, 6.0, 12.0), e -> e.isInRange(entity, 12.0) && entity.shouldFlee(e)
         );
         list.sort(Comparator.comparingDouble(entity::squaredDistanceTo));
         Optional<LivingEntity> optional = list.stream().filter(entity::canSee).findFirst();
         if (optional.isPresent()) {
            brain.remember(MemoryModuleType.AVOID_TARGET, optional.get());
            brain.forget(CMemoryModuleType.IS_EATING);
            brain.forget(CMemoryModuleType.IS_DIGGING);
         } else {
            brain.forget(MemoryModuleType.AVOID_TARGET);
         }
      }
   }

   public Set<MemoryModuleType<?>> getOutputMemoryModules() {
      return ImmutableSet.of(MemoryModuleType.AVOID_TARGET, CMemoryModuleType.IS_EATING, CMemoryModuleType.IS_DIGGING);
   }
}
