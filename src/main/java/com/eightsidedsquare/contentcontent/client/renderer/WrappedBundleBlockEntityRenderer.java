package com.eightsidedsquare.contentcontent.client.renderer;

import com.eightsidedsquare.contentcontent.client.model.WrappedBundleBlockEntityModel;
import com.eightsidedsquare.contentcontent.common.entity.WrappedBundleBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class WrappedBundleBlockEntityRenderer extends GeoBlockRenderer<WrappedBundleBlockEntity> {
   private float red, green, blue;

   public WrappedBundleBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
      super(new WrappedBundleBlockEntityModel());
   }

   @Override
   public void actuallyRender(MatrixStack matrices, WrappedBundleBlockEntity animatable, BakedGeoModel model,
                              RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer,
                              boolean isReRender, float partialTick, int packedLight, int packedOverlay,
                              float red, float green, float blue, float alpha) {

      matrices.push();
      matrices.translate(0.5, 0, 0.5);
      matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(animatable.getYaw()));
      matrices.translate(-0.5, 0, -0.5);

      super.actuallyRender(matrices, animatable, model, renderType, bufferSource, buffer,
              isReRender, partialTick, packedLight, packedOverlay,
              1.0F, 1.0F, 1.0F, alpha);

      matrices.pop();
   }

   @Override
   public void renderRecursively(MatrixStack matrices, WrappedBundleBlockEntity animatable, GeoBone bone,
                                 RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer,
                                 boolean isReRender, float partialTick, int packedLight, int packedOverlay,
                                 float red, float green, float blue, float alpha) {

      if (bone.getName().equals("ribbon") || bone.getName().equals("dyed_top")) {
         int color = animatable.getColor();
         float r = (color >> 16 & 0xFF) / 255.0F;
         float g = (color >> 8 & 0xFF) / 255.0F;
         float b = (color & 0xFF) / 255.0F;

         super.renderRecursively(matrices, animatable, bone, renderType, bufferSource, buffer,
                 isReRender, partialTick, packedLight, packedOverlay,
                 r, g, b, alpha);
      } else {
         super.renderRecursively(matrices, animatable, bone, renderType, bufferSource, buffer,
                 isReRender, partialTick, packedLight, packedOverlay,
                 1.0F, 1.0F, 1.0F, alpha);
      }
   }

   @Override
   public RenderLayer getRenderType(WrappedBundleBlockEntity animatable, Identifier texture,
                                    VertexConsumerProvider bufferSource, float partialTick) {
      return RenderLayer.getEntityTranslucent(texture);
   }
}
