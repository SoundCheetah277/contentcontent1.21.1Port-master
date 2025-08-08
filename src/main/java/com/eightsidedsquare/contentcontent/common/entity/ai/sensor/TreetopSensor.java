package com.eightsidedsquare.contentcontent.common.entity.ai.sensor;

import com.eightsidedsquare.contentcontent.common.entity.ai.CMemoryModuleType;
import com.eightsidedsquare.contentcontent.common.entity.squirrel.SquirrelEntity;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;
import java.util.Set;

public class TreetopSensor extends Sensor<SquirrelEntity> {
   public TreetopSensor() {
      super(100);
   }

   protected void sense(ServerWorld world, SquirrelEntity entity) {
      if (!entity.isEating() && !entity.isPanicking()) {
         Optional<BlockPos> testPos = BlockPos.findClosest(entity.getBlockPos(), 16, 12, pos -> {
            boolean isTree = world.getBlockState(pos).isIn(BlockTags.LEAVES) || world.getBlockState(pos).isAir();

            for (int i = 1; i < 5; i++) {
               isTree &= world.getBlockState(pos.down(i)).isIn(BlockTags.LOGS);
            }

            return isTree;
         });
         if (testPos.isPresent()) {
            BlockPos treePos = testPos.get();
            Path path = entity.getNavigation().findPathTo(treePos, 16);
            if (path != null && path.reachesTarget()) {
               entity.getBrain().remember(CMemoryModuleType.NEAREST_TREETOP, treePos);
            }
         } else {
            entity.getBrain().forget(CMemoryModuleType.NEAREST_TREETOP);
         }
      }
   }

   public Set<MemoryModuleType<?>> getOutputMemoryModules() {
      return ImmutableSet.of(CMemoryModuleType.NEAREST_TREETOP);
   }
}
