package com.eightsidedsquare.contentcontent.common.entity.ai.task;

import com.eightsidedsquare.contentcontent.common.entity.ai.CMemoryModuleType;
import com.eightsidedsquare.contentcontent.common.entity.squirrel.SquirrelEntity;
import com.google.common.collect.ImmutableMap;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class SquirrelEatTask extends MultiTickTask<SquirrelEntity> {
   public SquirrelEatTask() {
      super(ImmutableMap.of(
              CMemoryModuleType.IS_WAITING, MemoryModuleState.VALUE_ABSENT,
              MemoryModuleType.AVOID_TARGET, MemoryModuleState.VALUE_ABSENT,
              CMemoryModuleType.ATE_RECENTLY, MemoryModuleState.VALUE_ABSENT
      ), 60);
   }

   @Override
   protected boolean shouldRun(ServerWorld world, SquirrelEntity entity) {
      ItemStack mainHand = entity.getMainHandStack();
      FoodComponent food = mainHand.getItem().getFoodProperties(mainHand, entity);
      return !entity.isDigging()
              && !entity.isClimbing()
              && (food != null || entity.isBreedingItem(mainHand));   }

   @Override
   protected boolean shouldKeepRunning(ServerWorld world, SquirrelEntity entity, long time) {
      ItemStack mainHand = entity.getMainHandStack();
      FoodComponent food = mainHand.getItem().getFoodProperties(mainHand, entity);
      return !entity.isDigging()
              && !entity.isClimbing()
              && entity.isEating()
              && (food != null || entity.isBreedingItem(mainHand));   }

   @Override
   protected void run(ServerWorld world, SquirrelEntity entity, long time) {
      entity.getBrain().remember(CMemoryModuleType.IS_EATING, true);
      entity.setEating(true);
   }

   @Override
   protected void keepRunning(ServerWorld world, SquirrelEntity entity, long time) {
      if (entity.getRandom().nextInt(3) == 1) {
         entity.playEatSound(entity.getMainHandStack());
      }
   }

   @Override
   protected void finishRunning(ServerWorld world, SquirrelEntity entity, long time) {
      var brain = entity.getBrain();
      brain.remember(CMemoryModuleType.ATE_RECENTLY, true, UniformIntProvider.create(400, 800).get(entity.getRandom()));
      brain.forget(CMemoryModuleType.IS_EATING);
      entity.setEating(false);

      ItemStack mainHandStack = entity.getMainHandStack();
      FoodComponent food = mainHandStack.getItem().getFoodComponent();
      int lootDigsLeft = brain.getOptionalMemory(CMemoryModuleType.LOOT_DIGS_LEFT).orElse(0);

      if (food != null) {
         int hunger = food.nutrition();
         entity.heal(hunger);
         brain.remember(CMemoryModuleType.LOOT_DIGS_LEFT, Math.min(lootDigsLeft + hunger, 10));
      }

      ItemStack stack = mainHandStack.finishUsing(world, entity);
      entity.equipStack(EquipmentSlot.MAINHAND, stack.isEmpty() ? ItemStack.EMPTY : stack);
   }
}