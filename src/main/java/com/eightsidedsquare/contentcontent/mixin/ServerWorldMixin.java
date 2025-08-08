package com.eightsidedsquare.contentcontent.mixin;

import com.eightsidedsquare.contentcontent.common.world.MagnetTracker;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {
   @Unique
   private final MagnetTracker tracker = new MagnetTracker();

   protected ServerWorldMixin(
           MutableWorldProperties properties,
           RegistryKey<World> registryRef,
           DynamicRegistryManager registryManager,
           RegistryEntry<DimensionType> dimension,
           Supplier<Profiler> profiler,
           boolean isClient,
           boolean debugWorld,
           long seed,
           int maxChainedNeighborUpdates
   ) {
      super(properties, registryRef, registryManager, dimension, profiler, isClient, debugWorld, seed, maxChainedNeighborUpdates);
   }

   @Inject(
           method = "tick",
           at = @At("TAIL")
   )
   private void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
      this.tracker.tick((ServerWorld)(Object)this);
   }
}