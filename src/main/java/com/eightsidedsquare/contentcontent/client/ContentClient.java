package com.eightsidedsquare.contentcontent.client;
import com.eightsidedsquare.contentcontent.client.particle.EmberParticle;
import com.eightsidedsquare.contentcontent.client.particle.SoulEmberParticle;
import com.eightsidedsquare.contentcontent.client.renderer.DisplayCaseBlockEntityRenderer;
import com.eightsidedsquare.contentcontent.client.renderer.SquirrelEntityRenderer;
import com.eightsidedsquare.contentcontent.client.renderer.WrappedBundleBlockEntityRenderer;
import com.eightsidedsquare.contentcontent.common.entity.PotionMugBlockEntity;
import com.eightsidedsquare.contentcontent.common.item.BoxingGloveItem;
import com.eightsidedsquare.contentcontent.common.network.MagnetTargetClearS2CPacket;
import com.eightsidedsquare.contentcontent.common.network.MagnetTargetSetS2CPacket;
import com.eightsidedsquare.contentcontent.core.ContentBlocks;
import com.eightsidedsquare.contentcontent.core.ContentEntities;
import com.eightsidedsquare.contentcontent.core.ContentItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.item.CompassAnglePredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.GlobalPos;
import org.jetbrains.annotations.Nullable;

public class ContentClient implements ClientModInitializer {
   public static final SimpleParticleType EMBER = Registry.register(
           Registries.PARTICLE_TYPE, Identifier.of("contentcontent:ember"), FabricParticleTypes.simple(true)
   );
   public static final SimpleParticleType SOUL_EMBER = Registry.register(
           Registries.PARTICLE_TYPE, Identifier.of("contentcontent:soul_ember"), FabricParticleTypes.simple(true)
   );
   @Nullable
   public static GlobalPos magnetTarget = null;

   @Override
   public void onInitializeClient() {
      ParticleFactoryRegistry.getInstance().register(EMBER, EmberParticle.EmberFactory::new);
      ParticleFactoryRegistry.getInstance().register(SOUL_EMBER, SoulEmberParticle.SoulEmberFactory::new);

      BlockRenderLayerMap.INSTANCE.putBlocks(
              RenderLayer.getCutout(),
              ContentBlocks.MOSS_LAYER,
              ContentBlocks.SNOW_LAYER,
              ContentBlocks.IRON_SCAFFOLDING,
              ContentBlocks.STICKSHIFT,
              ContentBlocks.STEEL_BAR,
              ContentBlocks.BRAZIER,
              ContentBlocks.SOUL_BRAZIER,
              ContentBlocks.CHOCOLATE_EGG,
              ContentBlocks.CHISELED_CHOCOLATE_EGG,
              ContentBlocks.CHOCOLATE_FROG,
              ContentBlocks.CHOCOLATE_RAT,
              ContentBlocks.CHOCOLATE_RAVEN,
              ContentBlocks.CHOCOLATE_SQUIRREL,
              ContentBlocks.CHOCOLATE_CREWMATE,
              ContentBlocks.MUG,
              ContentBlocks.LAVA_MUG,
              ContentBlocks.MILK_MUG,
              ContentBlocks.PILK_MUG,
              ContentBlocks.POWDER_SNOW_MUG,
              ContentBlocks.AQUARIUM_GLASS,
              ContentBlocks.DISPLAY_CASE,
              ContentBlocks.FOLLY_LEAVES,
              ContentBlocks.FOLLY_VINES,
              ContentBlocks.FOLLY_VINES_PLANT,
              ContentBlocks.FOLLY_FENCE,
              ContentBlocks.FOLLY_FENCE_GATE,
              ContentBlocks.FOLLY_BUTTON,
              ContentBlocks.FOLLY_PRESSURE_PLATE,
              ContentBlocks.FOLLY_DOOR,
              ContentBlocks.FOLLY_TRAPDOOR,
              ContentBlocks.FOLLY_TRUNK,
              ContentBlocks.FOLLY_SAPLING,
              ContentBlocks.POTTED_FOLLY_SAPLING,
              ContentBlocks.POTTED_FOLLY_SEED
      );

      BlockRenderLayerMap.INSTANCE.putBlocks(
              RenderLayer.getTranslucent(),
              ContentBlocks.POTION_MUG, ContentBlocks.HONEY_MUG
      );

      ColorProviderRegistry.BLOCK.register(PotionMugBlockEntity::getColor, ContentBlocks.POTION_MUG, ContentBlocks.PILK_MUG);
      ColorProviderRegistry.ITEM.register(ContentClient::getWrappedBundleColor, ContentItems.WRAPPED_BUNDLE);
      ColorProviderRegistry.ITEM.register(ContentClient::getBoxingGloveColor, ContentItems.BOXING_GLOVE);

      ModelPredicateProviderRegistry.register(
              ContentItems.COPPER_HORN,
              Identifier.of("tooting"),
              (itemStack, clientWorld, livingEntity, i) ->
                      (livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack) ? 1.0F : 0.0F
      );

      ModelPredicateProviderRegistry.register(
              ContentItems.MAGNETIZED_TRACKER,
              Identifier.of("angle"),
              new CompassAnglePredicateProvider((world, stack, entity) -> magnetTarget)
      );

      EntityRendererRegistry.register(ContentEntities.SQUIRREL, SquirrelEntityRenderer::new);
      BlockEntityRendererRegistry.register(ContentEntities.DISPLAY_CASE, DisplayCaseBlockEntityRenderer::new);
      BlockEntityRendererRegistry.register(ContentEntities.WRAPPED_BUNDLE, WrappedBundleBlockEntityRenderer::new);
      //ClientPlayNetworking.registerGlobalReceiver(MagnetTargetSetS2CPacket.ID, MagnetTargetSetS2CPacket::handler);
      //ClientPlayNetworking.registerGlobalReceiver(MagnetTargetClearS2CPacket.ID, MagnetTargetClearS2CPacket::handler);
   }

   private static int getWrappedBundleColor(ItemStack stack, int tintIndex) {
      if (tintIndex > 0) {
         Integer color = stack.get(BoxingGloveItem.ContentContentComponents.COLOR);
         if (color != null) {
            return color;
         }
      }
      return -1; // No tint if no color tag or tintIndex <= 0
   }


   private static int getBoxingGloveColor(ItemStack stack, int tintIndex) {
      BoxingGloveItem item = (BoxingGloveItem) stack.getItem();
      return item.getColor(stack);
   }
}