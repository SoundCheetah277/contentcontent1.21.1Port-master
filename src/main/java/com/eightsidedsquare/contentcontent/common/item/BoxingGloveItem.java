package com.eightsidedsquare.contentcontent.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BoxingGloveItem extends Item {
   private static final int DEFAULT_COLOR = 0xA06540;
   private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

   public BoxingGloveItem(Item.Settings settings) {
      super(settings);
      Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
      builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID,
              "Weapon modifier",
              1.0,
              EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
      this.attributeModifiers = builder.build();
   }
   public int getColor(ItemStack stack) {
      NbtCompound display = stack.getSubNbt("display");
      return (display) != null && nbtCompound.contains("color", NbtElement.INT_TYPE) ? display.getInt("color") : 14294558;
   }

   public Object getAttributeModifiers(EquipmentSlot slot) {
      return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers();
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
