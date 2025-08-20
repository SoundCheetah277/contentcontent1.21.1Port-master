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
   public void actuallyRender(
           MatrixStack matrices,
           WrappedBundleBlockEntity animatable,
           BakedGeoModel model,
           RenderLayer renderType,
           VertexConsumerProvider bufferSource,
           VertexConsumer buffer,
           boolean isReRender,
           float partialTick,
           int packedLight,
           int packedOverlay,
           int modelData // <- NEW PARAMETER!
   ) {
      matrices.push();
      matrices.translate(0.5, 0, 0.5);
      matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(animatable.getYaw()));
      matrices.translate(-0.5, 0, -0.5);

      super.actuallyRender(matrices, animatable, model, renderType, bufferSource, buffer,
              isReRender, partialTick, packedLight, packedOverlay, modelData);

      matrices.pop();
   }

   @Override
   public void renderRecursively(
           MatrixStack matrices,
           WrappedBundleBlockEntity animatable,
           GeoBone bone,
           RenderLayer renderType,
           VertexConsumerProvider bufferSource,
           VertexConsumer buffer,
           boolean isReRender,
           float partialTick,
           int packedLight,
           int packedOverlay,
           int modelData // <- NEW PARAMETER!
   ) {
      // If you want per-bone coloring, implement it here – you can't pass color tints directly anymore.
      super.renderRecursively(matrices, animatable, bone, renderType, bufferSource, buffer,
              isReRender, partialTick, packedLight, packedOverlay, modelData);
   }

   @Override
   public RenderLayer getRenderType(WrappedBundleBlockEntity animatable, Identifier texture,
                                    VertexConsumerProvider bufferSource, float partialTick) {
      return RenderLayer.getEntityTranslucent(texture);
   }
}
