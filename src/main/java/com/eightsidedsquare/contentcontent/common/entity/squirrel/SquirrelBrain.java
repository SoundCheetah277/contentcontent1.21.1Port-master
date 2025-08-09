package com.eightsidedsquare.contentcontent.common.entity.squirrel;


import com.eightsidedsquare.contentcontent.common.entity.ai.CMemoryModuleType;
import com.eightsidedsquare.contentcontent.common.entity.ai.task.*;
import com.eightsidedsquare.contentcontent.core.ContentEntities;
import com.eightsidedsquare.contentcontent.core.ContentItems;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class SquirrelBrain {
   public static Brain<SquirrelEntity> create(Brain<SquirrelEntity> brain) {
      addCoreTasks(brain);
      addIdleTasks(brain);
      addAvoidTasks(brain);
      addEatTasks(brain);
      brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
      brain.setDefaultActivity(Activity.IDLE);
      brain.resetPossibleActivities();
      return brain;
   }

   private static void addCoreTasks(Brain<SquirrelEntity> brain) {
      brain.setTaskList(
              Activity.CORE,
              0,
              ImmutableList.of(
                      new StayAboveWaterTask(0.8F),
                      new LookAroundTask(45, 90),
                      new MoveToTargetTask(),
                      new TemptationCooldownTask(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS),
                      new TemptationCooldownTask(CMemoryModuleType.CALM_TOWARDS_PLAYERS_COOLDOWN),
                      makeRandomFollowTask(),
                      makeRandomWanderTask()
              )
      );
   }

   private static void addIdleTasks(Brain<SquirrelEntity> brain) {
      brain.setTaskList(
              Activity.IDLE,
              10,
              ImmutableList.of(
                      new PickUpFoodItemTask(),
                      new SitTask(),
                      new ClimbToTreetopTask(),
                      new BreedTask(ContentEntities.SQUIRREL),
                      new TemptTask(e -> 1.25F),
                      new SquirrelDigTask(),
                      makeRandomFollowTask(),
                      makeRandomWanderTask()
              )
      );
   }

   private static void addAvoidTasks(Brain<SquirrelEntity> brain) {
      brain.setTaskList(
              Activity.AVOID,
              10,
              ImmutableList.of(GoToRememberedPositionTask.createEntityBased(MemoryModuleType.AVOID_TARGET, 3.0F, 12, true), makeRandomFollowTask(), makeRandomWanderTask()),
              MemoryModuleType.AVOID_TARGET
      );
   }

   private static void addEatTasks(Brain<SquirrelEntity> brain) {
      brain.setTaskList(Activity.ADMIRE_ITEM, 10, ImmutableList.of(new SquirrelEatTask()), CMemoryModuleType.IS_EATING);
   }

   public static void updateActivities(SquirrelEntity entity) {
      Brain<?> brain = entity.getBrain();
      brain.resetPossibleActivities(ImmutableList.of(Activity.ADMIRE_ITEM, Activity.AVOID, Activity.IDLE));
   }

   private static RandomTask<SquirrelEntity> makeRandomFollowTask() {
      return new RandomTask(
              ImmutableList.of(
//                      Pair.of(new WalkTowardClosestAdultTask(UniformIntProvider.create(5, 16), 1.5F), 4),
//                      Pair.of(new FollowMobTask(EntityType.PLAYER, 8.0F), 1),
//                      Pair.of(new FollowMobTask(ContentEntities.SQUIRREL, 8.0F), 1),
//                      Pair.of(new FollowMobTask(8.0F), 1),
                      Pair.of(new WalkTowardPlayerWhenCalmTask(UniformIntProvider.create(5, 16), 1.5F), 1),
                      Pair.of(new WaitTask(30, 60), 1)
              )
      );
   }

   private static RandomTask<SquirrelEntity> makeRandomWanderTask() {
      return new RandomTask(ImmutableList.of(Pair.of(StrollTask.create(1.5F), 2), Pair.of(GoTowardsLookTargetTask.create(1.5F, 3), 2), Pair.of(new WaitTask(30, 60), 1)));
   }

   public static Ingredient getTemptItems() {
      return Ingredient.ofItems(new ItemConvertible[]{ContentItems.ACORN});
   }
}
