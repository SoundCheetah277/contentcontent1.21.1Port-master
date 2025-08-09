//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.eightsidedsquare.contentcontent.common.entity.ai.task;

import com.eightsidedsquare.contentcontent.common.entity.ai.CMemoryModuleType;
import com.eightsidedsquare.contentcontent.common.entity.squirrel.SquirrelEntity;
import com.google.common.collect.ImmutableMap;
import java.util.function.Function;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;

public class WalkTowardPlayerWhenCalmTask extends MultiTickTask<SquirrelEntity> {
    private final UniformIntProvider executionRange;
    private final Function<LivingEntity, Float> speed;

    public WalkTowardPlayerWhenCalmTask(UniformIntProvider executionRange, float speed) {
        this(executionRange, (entity) -> speed);
    }

    public WalkTowardPlayerWhenCalmTask(UniformIntProvider executionRange, Function<LivingEntity, Float> speed) {
        super(ImmutableMap.of(CMemoryModuleType.CALM_TOWARDS_PLAYERS_COOLDOWN, MemoryModuleState.VALUE_PRESENT, MemoryModuleType.AVOID_TARGET, MemoryModuleState.VALUE_ABSENT, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleState.VALUE_PRESENT));
        this.executionRange = executionRange;
        this.speed = speed;
    }

    protected void run(World serverWorld, SquirrelEntity entity, long l) {
        entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER).ifPresent((player) -> LookTargetUtil.walkTowards(entity, player, (Float)this.speed.apply(entity), this.executionRange.getMin() - 1));
    }
}
