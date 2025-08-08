package com.eightsidedsquare.contentcontent.mixin;

import com.eightsidedsquare.contentcontent.common.entity.squirrel.SquirrelEntity;
import com.eightsidedsquare.contentcontent.core.ContentPotions;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({AbstractBlock.class})
public class AbstractBlockMixin {
   @Inject(
      method = {"getCollisionShape"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
      if (state.isIn(BlockTags.LEAVES)
         && context instanceof EntityShapeContext entityShapeContext
         && (
            entityShapeContext.getEntity() instanceof SquirrelEntity
               || entityShapeContext.getEntity() instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect((RegistryEntry<StatusEffect>) ContentPotions.RUSTLING_STATUS_EFFECT)
         )) {
         cir.setReturnValue(VoxelShapes.empty());
      }
   }
}
