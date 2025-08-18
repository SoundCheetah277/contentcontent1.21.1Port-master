package com.eightsidedsquare.contentcontent.common.entity.ai.sensor;

import com.eightsidedsquare.contentcontent.common.entity.ai.CMemoryModuleType;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class FoodItemEntitySensor extends Sensor<AnimalEntity> {
   protected void sense(ServerWorld world, AnimalEntity entity) {
      Brain<?> brain = entity.getBrain();
      List<ItemEntity> list = world.getEntitiesByClass(
              ItemEntity.class,
              entity.getBoundingBox().expand(8.0, 4.0, 8.0),
              itemEntity -> itemEntity.isInRange(entity, 9.0) && entity.canGather(itemEntity.getStack())
      );
      list.sort(Comparator.comparingDouble(entity::squaredDistanceTo));
      brain.remember(CMemoryModuleType.NEAREST_FOOD_ITEM_ENTITY, list.stream().filter(entity::canSee).findFirst());
      if (!brain.getOptionalMemory(CMemoryModuleType.ATE_RECENTLY).orElse(false) && FoodComponent !=null) {
         brain.remember(CMemoryModuleType.IS_EATING, true);
      }
   }

   public Set<MemoryModuleType<?>> getOutputMemoryModules() {
      return ImmutableSet.of(CMemoryModuleType.NEAREST_FOOD_ITEM_ENTITY, CMemoryModuleType.IS_EATING);
   }
}