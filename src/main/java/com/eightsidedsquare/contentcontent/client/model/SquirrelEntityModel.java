package com.eightsidedsquare.contentcontent.client.model;

import com.eightsidedsquare.contentcontent.common.entity.squirrel.SquirrelEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class SquirrelEntityModel extends GeoModel<SquirrelEntity> {

   @Override
   public Identifier getModelResource(SquirrelEntity object) {
      return Identifier.of("contentcontent:geo/squirrel.geo.json");
   }

   @Override
   public Identifier getTextureResource(SquirrelEntity entity) {
      String texture = switch (entity.getVariant()) {
         case 1 -> "fox";
         case 2 -> "red";
         case 3 -> "black";
         case 4 -> "abert";
         default -> "gray";
      };
      return Identifier.of("contentcontent", "textures/entity/squirrel/" + texture + ".png");
   }

   @Override
   public Identifier getAnimationResource(SquirrelEntity animatable) {
      return Identifier.of("contentcontent:animations/squirrel.animation.json");
   }

   @Override
   public void setCustomAnimations(SquirrelEntity entity, long instanceId, AnimationState<SquirrelEntity> animationState) {
      super.setCustomAnimations(entity, instanceId, animationState);

      GeoBone head = this.getAnimationProcessor().getBone("head");
      GeoBone root = this.getAnimationProcessor().getBone("root");

      EntityModelData modelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

      float headScale = 1.5F;
      float babyScale = 0.55F;
      float scale = 0.95F;

      if (root != null && head != null) {
         if (!entity.isDigging() && !entity.isEating()) {
            head.setRotX(modelData.headPitch() * ((float) Math.PI / 180F));
            head.setRotY(modelData.netHeadYaw() * ((float) Math.PI / 180F));
         }

         if (entity.isBaby() && !MinecraftClient.getInstance().isPaused()) {
            head.setScaleX(headScale);
            head.setScaleY(headScale);
            head.setScaleZ(headScale);

            root.setScaleX(babyScale);
            root.setScaleY(babyScale);
            root.setScaleZ(babyScale);

            root.setPosY(root.getPosY() - 1.8F);
         } else {
            root.setScaleX(scale);
            root.setScaleY(scale);
            root.setScaleZ(scale);
         }
      }
   }
}
