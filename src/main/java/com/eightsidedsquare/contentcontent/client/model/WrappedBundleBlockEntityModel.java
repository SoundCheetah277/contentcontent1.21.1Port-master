package com.eightsidedsquare.contentcontent.client.model;

import com.eightsidedsquare.contentcontent.common.entity.WrappedBundleBlockEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class WrappedBundleBlockEntityModel extends GeoModel<WrappedBundleBlockEntity> {
   public Identifier getModelResource(WrappedBundleBlockEntity object) {
      return Identifier.of("contentcontent", "geo/wrapped_bundle.geo.json");
   }

   public Identifier getTextureResource(WrappedBundleBlockEntity object) {
      return Identifier.of("contentcontent", "textures/entity/wrapped_bundle/wrapped_bundle.png");
   }

   public Identifier getAnimationResource(WrappedBundleBlockEntity animatable) {
      return Identifier.of("contentcontent", "animations/wrapped_bundle.animation.json");
   }
}
