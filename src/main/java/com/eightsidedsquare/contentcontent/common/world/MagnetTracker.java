package com.eightsidedsquare.contentcontent.common.world;

import com.eightsidedsquare.contentcontent.common.network.MagnetTargetClearS2CPacket;
import com.eightsidedsquare.contentcontent.common.network.MagnetTargetSetS2CPacket;
import com.eightsidedsquare.contentcontent.core.tags.ContentItemTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;

import java.util.ArrayList;
import java.util.List;

public class MagnetTracker {
   private int cooldown = 40;

   public void tick(ServerWorld world) {
      if (--this.cooldown <= 0) {
         this.cooldown = 40;
         List<Entity> magneticEntities = new ArrayList<>();

          for (Entity entity1 : world.iterateEntities()) {
              if (entity1 instanceof LivingEntity living && living.isAlive()) {
                  for (ItemStack stack : living.getArmorItems()) {
                      if (stack.isIn(ContentItemTags.MAGNETIC_ARMOR)) {
                          magneticEntities.add(entity1);
                          break;
                      }
                  }
              }
          }

          if (magneticEntities.isEmpty()) {
            for (ServerPlayerEntity player : world.getPlayers()) {
               MagnetTargetClearS2CPacket.send(player);
            }
         } else {
            for (ServerPlayerEntity player : world.getPlayers()) {
               if (player != null && player.isAlive()) {
                  double dist = Double.MAX_VALUE;
                  BlockPos pos = null;

                  for (Entity entity : magneticEntities) {
                     if (!entity.equals(player)) {
                        double entityDist = entity.distanceTo(player);
                        if (entityDist < dist) {
                           dist = entityDist;
                           pos = entity.getBlockPos();
                        }
                     }
                  }

                  if (pos == null) {
                     MagnetTargetClearS2CPacket.send(player);
                  } else {
                      MagnetTargetSetS2CPacket.send(player, GlobalPos.create(world.getRegistryKey(), pos));
                  }
               }
            }
         }
      }
   }
}