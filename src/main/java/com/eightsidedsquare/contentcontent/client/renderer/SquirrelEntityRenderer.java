package com.eightsidedsquare.contentcontent.client.renderer;

import com.eightsidedsquare.contentcontent.client.model.SquirrelEntityModel;
import com.eightsidedsquare.contentcontent.common.entity.squirrel.SquirrelEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SquirrelEntityRenderer extends GeoEntityRenderer<SquirrelEntity> {
   public SquirrelEntityRenderer(EntityRendererFactory.Context ctx) {
      super(ctx, new SquirrelEntityModel());
      this.shadowRadius = 0.4F;
   }

   public RenderLayer getRenderType(
           SquirrelEntity animatable,
           float partialTicks,
           MatrixStack stack,
           VertexConsumerProvider renderTypeBuffer,
           VertexConsumer vertexBuilder,
           int packedLightIn,
           Identifier textureLocation
   ) {
      return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
   }
}