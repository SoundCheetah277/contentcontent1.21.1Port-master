package com.eightsidedsquare.contentcontent.common.item;

import com.eightsidedsquare.contentcontent.core.ContentMod;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BoxingGloveItem extends Item {
   private static final int DEFAULT_COLOR = 0xA06540;

   public BoxingGloveItem(Settings settings) {
      super(settings);
   }

   public int getColor(ItemStack stack) {
      Integer color = stack.get(ContentMod.COLOR_COMPONENT); // Use the main mod's component
      return color != null ? color : DEFAULT_COLOR;
   }

   @Override
   public AttributeModifiersComponent getAttributeModifiers() {
      return AttributeModifiersComponent.builder()
              .add(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(
                      BASE_ATTACK_SPEED_MODIFIER_ID,
                      1.0,
                      EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
              ), AttributeModifierSlot.MAINHAND)
              .build();
   }

   public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
      if (hand.equals(Hand.OFF_HAND) && user.getAttackCooldownProgress(0.0F) == 1.0F && entity.hurtTime == 0 && entity.deathTime == 0) {
         user.resetLastAttackedTicks();
         entity.damage(user.getDamageSources().playerAttack(user), 2.0F);
         user.getWorld()
                 .playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, user.getSoundCategory(), 1.0F, 1.0F);
         if (!user.getWorld().isClient) {
            ((ServerWorld)user.getWorld())
                    .spawnParticles(ParticleTypes.DAMAGE_INDICATOR, entity.getX(), entity.getBodyY(0.5), entity.getZ(), 1, 0.1, 0.0, 0.1, 0.2);
         }
         return ActionResult.success(user.getWorld().isClient);
      } else {
         return super.useOnEntity(stack, user, entity, hand);
      }
   }

   public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
      if (hand.equals(Hand.OFF_HAND) && user.getAttackCooldownProgress(0.0F) == 1.0F) {
         user.resetLastAttackedTicks();
         user.getWorld()
                 .playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, user.getSoundCategory(), 1.0F, 1.0F);
         return TypedActionResult.success(user.getStackInHand(hand));
      } else {
         return TypedActionResult.pass(user.getStackInHand(hand));
      }
   }
}
