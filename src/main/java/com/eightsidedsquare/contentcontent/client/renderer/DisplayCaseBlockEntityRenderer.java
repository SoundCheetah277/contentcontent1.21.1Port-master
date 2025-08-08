package com.eightsidedsquare.contentcontent.client.renderer;

import com.eightsidedsquare.contentcontent.common.entity.DisplayCaseBlockEntity;
import net.minecraft.block.WallMountedBlock;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

public class DisplayCaseBlockEntityRenderer implements BlockEntityRenderer<DisplayCaseBlockEntity> {
   private final ItemRenderer itemRenderer;
   private final TextRenderer textRenderer;

   public DisplayCaseBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
      this.itemRenderer = ctx.getItemRenderer();
      this.textRenderer = ctx.getTextRenderer();
   }

   public int getRenderDistance() {
      return 32;
   }

   public void render(DisplayCaseBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
      matrices.push();
      Direction d = ((Direction)entity.getCachedState().get(WallMountedBlock.FACING)).getOpposite();
      BlockFace face = (BlockFace)entity.getCachedState().get(WallMountedBlock.FACE);
      float angle = d.asRotation() + (d.getAxis().equals(Direction.Axis.Z) ? 180 : 0);
      double offset = 0.425;
      matrices.translate(0.5, 0.5, 0.5);
      if (face.equals(BlockFace.WALL)) {
         matrices.translate(d.getOffsetX() * offset, d.getOffsetY() * offset, d.getOffsetZ() * offset);
         matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(angle));
      } else if (face.equals(BlockFace.FLOOR)) {
         matrices.translate(0.0, -offset, 0.0);
         matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(90.0F));
         matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(angle + 180.0F));
      } else if (face.equals(BlockFace.CEILING)) {
         matrices.translate(0.0, offset, 0.0);
         matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
         matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(angle + 180.0F));
      }

      matrices.scale(0.5F, 0.5F, 0.01F);
      ItemStack stack = entity.getStack();
      if (!stack.isEmpty()) {
         this.itemRenderer.renderItem(
                 stack,
                 ModelTransformationMode.GUI,
                 light,
                 OverlayTexture.DEFAULT_UV,
                 matrices,
                 vertexConsumers,
                 entity.getWorld(),
                 0
         );
         if (stack.getCount() != 1) {
            String string = String.valueOf(stack.getCount());
            float scale = 0.0625F;
            matrices.scale(scale, scale, scale);
            matrices.translate(9 - this.textRenderer.getWidth(string), 0.0, 8.0);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
            this.textRenderer.draw(
                    Text.of(string),
                    1.0F,
                    2.0F,
                    4473924,
                    false,
                    matrices.peek().getPositionMatrix(),
                    vertexConsumers,
                    TextRenderer.TextLayerType.NORMAL,
                    0,
                    light
            );

            matrices.translate(0.0, 0.0, -1.0);
            this.textRenderer.draw(
                    Text.of(string),
                    0.0F,
                    1.0F,
                    16777215,
                    false,
                    matrices.peek().getPositionMatrix(),
                    vertexConsumers,
                    TextRenderer.TextLayerType.NORMAL,
                    0,
                    light
            );

         }
      }

      matrices.pop();
   }
}