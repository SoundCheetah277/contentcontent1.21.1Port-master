package com.eightsidedsquare.contentcontent.common.entity.squirrel;

import com.eightsidedsquare.contentcontent.common.entity.ai.CMemoryModuleType;
import com.eightsidedsquare.contentcontent.common.entity.ai.CSensorType;
import com.eightsidedsquare.contentcontent.core.ContentEntities;
import com.eightsidedsquare.contentcontent.core.ContentItems;
import com.eightsidedsquare.contentcontent.core.ContentSounds;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.InstancedAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.AnimationState;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SquirrelEntity extends AnimalEntity implements GeoEntity {
   protected static final ImmutableList<? extends SensorType<? extends Sensor<? super SquirrelEntity>>> SENSORS = ImmutableList.of(
           SensorType.NEAREST_LIVING_ENTITIES,
           SensorType.NEAREST_ADULT,
           CSensorType.TREETOP_SENSOR,
           CSensorType.SQUIRREL_TEMPTATIONS,
           CSensorType.FOOD_ITEM_ENTITY_SENSOR,
           CSensorType.SQUIRREL_AVOID_SENSOR,
           SensorType.NEAREST_PLAYERS
   );
   protected static final ImmutableList<? extends MemoryModuleType<?>> MEMORY_MODULES = ImmutableList.<MemoryModuleType<?>>builder()
           .add(CMemoryModuleType.NEAREST_TREETOP)
           .add(CMemoryModuleType.NEAREST_FOOD_ITEM_ENTITY)
           .add(CMemoryModuleType.IS_WAITING)
           .add(CMemoryModuleType.IS_EATING)
           .add(CMemoryModuleType.IS_DIGGING)
           .add(CMemoryModuleType.DUG_RECENTLY)
           .add(CMemoryModuleType.ATE_RECENTLY)
           .add(CMemoryModuleType.LOOT_DIGS_LEFT)
           .add(CMemoryModuleType.CLIMBED_TREE_RECENTLY)
           .add(CMemoryModuleType.CALM_TOWARDS_PLAYERS_COOLDOWN)
           .add(MemoryModuleType.AVOID_TARGET)
           .add(MemoryModuleType.BREED_TARGET)
           .add(MemoryModuleType.MOBS)
           .add(MemoryModuleType.VISIBLE_MOBS)
           .add(MemoryModuleType.NEAREST_PLAYERS)
           .add(MemoryModuleType.NEAREST_VISIBLE_PLAYER)
           .add(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER)
           .add(MemoryModuleType.LOOK_TARGET)
           .add(MemoryModuleType.WALK_TARGET)
           .add(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE)
           .add(MemoryModuleType.PATH)
           .add(MemoryModuleType.ATTACK_TARGET)
           .add(MemoryModuleType.ATTACK_COOLING_DOWN)
           .add(MemoryModuleType.NEAREST_VISIBLE_ADULT)
           .add(MemoryModuleType.HURT_BY_ENTITY)
           .add(MemoryModuleType.NEAREST_ATTACKABLE)
           .add(MemoryModuleType.TEMPTING_PLAYER)
           .add(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS)
           .add(MemoryModuleType.IS_TEMPTED)
           .build();

   private static final TrackedData<Boolean> IS_CLIMBING = DataTracker.registerData(SquirrelEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
   private static final TrackedData<Boolean> IS_DIGGING = DataTracker.registerData(SquirrelEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
   private static final TrackedData<Boolean> IS_EATING = DataTracker.registerData(SquirrelEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
   private static final TrackedData<Integer> VARIANT = DataTracker.registerData(SquirrelEntity.class, TrackedDataHandlerRegistry.INTEGER);
   private static final TrackedData<Integer> CLIMB_TIME = DataTracker.registerData(SquirrelEntity.class, TrackedDataHandlerRegistry.INTEGER);
   protected SquirrelEntity.SquirrelLookControl lookControl;
   private static final RawAnimation SWIM = RawAnimation.begin().thenLoop("animation.squirrel.swimming");
   private static final RawAnimation CLIMB = RawAnimation.begin().thenLoop("animation.squirrel.climbing");
   private static final RawAnimation EAT = RawAnimation.begin().thenLoop("animation.squirrel.eating");
   private static final RawAnimation DIG = RawAnimation.begin().thenLoop("animation.squirrel.digging");
   private static final RawAnimation MOVE = RawAnimation.begin().thenLoop("animation.squirrel.moving");
   private static final RawAnimation SIT = RawAnimation.begin().thenLoop("animation.squirrel.sitting");
   private static final RawAnimation BREATH = RawAnimation.begin().thenLoop("animation.squirrel.breathing");
   public SquirrelEntity(EntityType<? extends AnimalEntity> entityType, World world) {
      super(entityType, world);
      this.lookControl = new SquirrelEntity.SquirrelLookControl(this);
      this.ignoreCameraFrustum = true;
      this.getNavigation().setCanSwim(true);
   }

   AnimatableInstanceCache factory = new InstancedAnimatableInstanceCache(this);

   public static DefaultAttributeContainer.Builder createAttributes() {
      return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15);
   }

   public float getScaleFactor() {
      return this.isBaby() ? 0.6F : 1.0F;
   }

   protected void mobTick() {
      super.mobTick();
      this.getWorld().getProfiler().push("squirrelBrain");
      ((Brain<SquirrelEntity>) this.getBrain()).tick((ServerWorld) this.getWorld(), this);
      this.getWorld().getProfiler().pop();
      SquirrelBrain.updateActivities(this);
      this.setEating(this.getBrain().isMemoryInState(CMemoryModuleType.IS_EATING, MemoryModuleState.VALUE_PRESENT));
      this.setDigging(this.getBrain().isMemoryInState(CMemoryModuleType.IS_DIGGING, MemoryModuleState.VALUE_PRESENT));
      if (!this.getWorld().isClient && this.isEating() && !this.getMainHandStack().isEmpty()) {
         Vec3d vec3d = this.getRotationVector(this.getPitch(), this.bodyYaw);
         ((ServerWorld)this.getWorld())
                 .spawnParticles(
                         new ItemStackParticleEffect(ParticleTypes.ITEM, this.getMainHandStack()),
                         this.getX() + vec3d.x / 4.0,
                         this.getY() + this.getEyeHeight(this.getPose()) - 0.05,
                         this.getZ() + vec3d.z / 4.0,
                         1,
                         0.05,
                         0.05,
                         0.05,
                         0.0
                 );
      }
   }

   public boolean canBeLeashedBy(PlayerEntity player) {
      return false;
   }

   @Nullable
   public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
      SquirrelEntity child = (SquirrelEntity)ContentEntities.SQUIRREL.create(world);
      Objects.requireNonNull(child);
      if (this.random.nextBoolean()) {
         child.setVariant(this.chooseType(world));
      } else {
         child.setVariant(this.random.nextBoolean() ? this.getVariant() : ((SquirrelEntity)entity).getVariant());
      }

      return child;
   }

   protected Brain.Profile<SquirrelEntity> createBrainProfile() {
      return Brain.createProfile(MEMORY_MODULES, SENSORS);
   }

   public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
      return false;
   }

   @Nullable
   protected SoundEvent getAmbientSound() {
      return this.getWorld().isClient
              ? null
              : (
              this.getBrain().getOptionalMemory(MemoryModuleType.IS_TEMPTED).orElse(false)
                      ? ContentSounds.ENTITY_SQUIRREL_HAPPY
                      : ContentSounds.ENTITY_SQUIRREL_AMBIENT
      );
   }

   @Nullable
   protected SoundEvent getDeathSound() {
      return ContentSounds.ENTITY_SQUIRREL_DEATH;
   }

   @Nullable
   protected SoundEvent getHurtSound(DamageSource source) {
      return ContentSounds.ENTITY_SQUIRREL_HURT;
   }

   public SoundEvent getEatSound(ItemStack stack) {
      return ContentSounds.ENTITY_SQUIRREL_EAT;
   }

   protected void playStepSound(BlockPos pos, BlockState state) {
      this.playSound(ContentSounds.ENTITY_SQUIRREL_STEP, 0.15F, 1.0F);
   }

   public void playDigSound() {
      this.playSound(ContentSounds.ENTITY_SQUIRREL_DIG, 0.5F, (float)(1.0 + this.random.nextGaussian() / 10.0));
   }

   public void playEatSound(ItemStack mainHandStack) {
      this.playSound(ContentSounds.ENTITY_SQUIRREL_EAT, 0.5F, (float)(1.0 + this.random.nextGaussian() / 10.0));
   }

   protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
      return SquirrelBrain.create(this.createBrainProfile().deserialize(dynamic));
   }

   public EntityData initialize(
           ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt
   ) {
      this.getBrain().remember(CMemoryModuleType.DUG_RECENTLY, true, UniformIntProvider.create(100, 1200).get(this.random));
      this.setVariant(this.chooseType(world));
      return super.initialize(world, difficulty, spawnReason, entityData);
   }

   private int chooseType(WorldAccess world) {
      RegistryEntry<Biome> biome = world.getBiome(this.getBlockPos());
      BlockPos pos = this.getBlockPos();

      if (biome.value().getPrecipitation(pos) == Biome.Precipitation.SNOW) {
         return 2;
      } else if (biome.value().getPrecipitation(pos) == Biome.Precipitation.NONE) {
         return 4;
      } else {
         return this.random.nextFloat() < 0.05F ? 3 : this.random.nextInt(2);
      }
   }

   public void tick() {
      super.tick();
      if (!this.getWorld().isClient) {
         this.setClimbingWall(this.horizontalCollision);
         if (this.isClimbing() || this.getClimbTime() < 0) {
            this.setClimbTime(this.getClimbTime() + 1);
            if (this.getClimbTime() > 200) {
               this.setClimbTime(-200);
            }
         } else if (!this.isClimbing() && this.getClimbTime() > 0) {
            this.setClimbTime(this.getClimbTime() - 1);
         }
      }
   }

   public NbtCompound writeNbt(NbtCompound nbt) {
      nbt.putBoolean("IsClimbingWall", this.isClimbingWall());
      nbt.putBoolean("IsDigging", this.isDigging());
      nbt.putBoolean("IsEating", this.isEating());
      nbt.putInt("Variant", this.getVariant());
      nbt.putInt("ClimbTime", this.getClimbTime());
      return super.writeNbt(nbt);
   }

   public void readNbt(NbtCompound nbt) {
      super.readNbt(nbt);
      this.setClimbingWall(nbt.getBoolean("IsClimbingWall"));
      this.setDigging(nbt.getBoolean("IsDigging"));
      this.setEating(nbt.getBoolean("IsEating"));
      this.setVariant(nbt.getInt("Variant"));
      this.setClimbTime(nbt.getInt("ClimbTime"));
   }

   public boolean isClimbingWall() {
      return (Boolean)this.dataTracker.get(IS_CLIMBING);
   }

   public void setClimbingWall(boolean bl) {
      this.dataTracker.set(IS_CLIMBING, bl);
   }

   public boolean isDigging() {
      return (Boolean)this.dataTracker.get(IS_DIGGING);
   }

   public void setDigging(boolean bl) {
      this.dataTracker.set(IS_DIGGING, bl);
   }

   public boolean isEating() {
      return (Boolean)this.dataTracker.get(IS_EATING);
   }

   public void setEating(boolean bl) {
      this.dataTracker.set(IS_EATING, bl);
   }

   public int getVariant() {
      return (Integer)this.dataTracker.get(VARIANT);
   }

   public void setVariant(int variant) {
      this.dataTracker.set(VARIANT, variant);
   }

   public int getClimbTime() {
      return (Integer)this.dataTracker.get(CLIMB_TIME);
   }

   public void setClimbTime(int variant) {
      this.dataTracker.set(CLIMB_TIME, variant);
   }

   protected void initDataTracker(DataTracker.Builder builder) {
      super.initDataTracker(builder);
      this.dataTracker.set(IS_CLIMBING, false);
      this.dataTracker.set(IS_DIGGING, false);
      this.dataTracker.set(IS_EATING, false);
      this.dataTracker.set(VARIANT, 0);
      this.dataTracker.set(CLIMB_TIME, 0);
   }

   public boolean isClimbing() {
      return this.isClimbingWall() && this.getClimbTime() >= 0;
   }

   public Brain<?> getBrain() {
      return super.getBrain();
   }

   public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
      registrar.add(new AnimationController(this, "controller", 0, this::controller));
      registrar.add(new AnimationController(this, "breathing_controller", 0, this::breathingController));
   }

   private PlayState breathingController(AnimationState<SquirrelEntity> event) {
      event.getController().setAnimation(BREATH);
      return PlayState.CONTINUE;
   }

   private PlayState controller(AnimationState<SquirrelEntity> event) {
      SquirrelEntity entity = event.getAnimatable();
      //event.getController().transitionLengthTicks = 3.0;
      if (entity.touchingWater) {
         event.getController().setAnimation(SWIM);
      } else if (entity.isClimbing()) {
         event.getController().setAnimation(CLIMB);
      } else if (entity.isEating()) {
         event.getController().setAnimation(EAT);
      } else if (entity.isDigging()) {
         event.getController().setAnimation(DIG);
      } else if (event.isMoving()) {
         event.getController().setAnimation(MOVE);
      } else {
         event.getController().setAnimation(SIT);
      }

      return PlayState.CONTINUE;
   }

   public AnimatableInstanceCache getAnimatableInstanceCache() {
      return factory;
   }

   public void setJumping(boolean jumping) {
      if (!this.getWorld().getBlockState(this.getBlockPos()).isIn(BlockTags.LEAVES)) {
         super.setJumping(jumping);
      }
   }

   public Vec3d applyMovementInput(Vec3d movementInput, float slipperiness) {
      Vec3d vec3d = super.applyMovementInput(movementInput, slipperiness);
      return this.isClimbing() ? new Vec3d(vec3d.getX(), this.getClimbSpeed(), vec3d.getZ()) : vec3d;
   }

   public LookControl getLookControl() {
      return this.lookControl;
   }

   protected void loot(ItemEntity item) {
      ItemStack itemStack = item.getStack();
      if (this.canPickupItem(itemStack)) {
         Entity uuid = item.getOwner();
         Optional<List<PlayerEntity>> nearestPlayers = this.getBrain().getOptionalMemory(MemoryModuleType.NEAREST_PLAYERS);
         if (uuid != null && nearestPlayers.isPresent()) {
            boolean bl = nearestPlayers.get().stream().anyMatch(player -> player.getUuid().equals(uuid) && this.canSee(player));
            if (bl) {
               this.getBrain()
                       .remember(CMemoryModuleType.CALM_TOWARDS_PLAYERS_COOLDOWN, UniformIntProvider.create(18000, 86400).get(this.getRandom()));
            }
         }

         int i = itemStack.getCount();
         if (i > 1) {
            this.dropItem(itemStack.split(i - 1));
         }

         this.triggerItemPickedUpByEntityCriteria(item);
         this.equipStack(EquipmentSlot.MAINHAND, itemStack.split(1));
         this.handDropChances[EquipmentSlot.MAINHAND.getEntitySlotId()] = 2.0F;
         this.sendPickup(item, itemStack.getCount());
         item.discard();
      }
   }

   private void dropItem(ItemStack stack) {
      this.getWorld().spawnEntity(new ItemEntity(this.getWorld(), this.getX(), this.getY(), this.getZ(), stack));
   }

   public boolean canPickupItem(ItemStack stack) {
      return this.canPickUpLoot() && this.getMainHandStack().isEmpty() && stack.isFood();
   }

   public boolean canPickUpLoot() {
      return !this.getBrain().getOptionalMemory(CMemoryModuleType.ATE_RECENTLY).orElse(false);
   }

   public double getClimbSpeed() {
      return this.isPanicking() ? 0.45F : 0.25;
   }

   public boolean isPanicking() {
      return this.getBrain().isMemoryInState(MemoryModuleType.AVOID_TARGET, MemoryModuleState.VALUE_PRESENT);
   }

   public boolean shouldFlee(LivingEntity livingEntity) {
      return ImmutableSet.of(EntityType.PLAYER, EntityType.FOX, EntityType.WOLF, EntityType.CAT, EntityType.OCELOT)
              .contains(livingEntity.getType())
              && !(
              livingEntity instanceof PlayerEntity player
                      && (
                      player.isCreative() || player.isSpectator() || this.getBrain().getOptionalMemory(CMemoryModuleType.CALM_TOWARDS_PLAYERS_COOLDOWN).isPresent()
              )
      )
              && !livingEntity.isHolding(SquirrelBrain.getTemptItems());
   }

   public boolean isBreedingItem(ItemStack stack) {
      return stack.isOf(ContentItems.ACORN);
   }

   protected EntityNavigation createNavigation(World world) {
      return new SquirrelEntity.SquirrelNavigation(this, world);
   }

   private static class SquirrelLookControl extends LookControl {
      public SquirrelLookControl(MobEntity entity) {
         super(entity);
      }

      public void tick() {
         if (!this.entity.isClimbing()) {
            super.tick();
         }
      }
   }

   private static class SquirrelNavigation extends SpiderNavigation {
      public SquirrelNavigation(MobEntity mobEntity, World world) {
         super(mobEntity, world);
      }

      protected PathNodeNavigator createPathNodeNavigator(int range) {
         this.nodeMaker = new SquirrelEntity.SquirrelPathNodeMaker();
         return new PathNodeNavigator(this.nodeMaker, range);
      }

      public void tick() {
         if (this.entity instanceof SquirrelEntity e
                 && e.getBrain().getOptionalMemory(MemoryModuleType.WALK_TARGET).isPresent()
                 && !e.isEating()
                 && !e.isDigging()) {
            super.tick();
         }
      }
   }

   private static class SquirrelPathNodeMaker extends LandPathNodeMaker {
      private final BlockPos.Mutable pos = new BlockPos.Mutable();

      public PathNodeType getDefaultNodeType(BlockView world, int x, int y, int z) {
         this.pos.set(x, y, z);
         PathNodeType pathNodeType = getCommonNodeType(world, this.pos);
         return pathNodeType == PathNodeType.LEAVES ? PathNodeType.WALKABLE : getLandNodeType(world, this.pos);
      }
   }
}
