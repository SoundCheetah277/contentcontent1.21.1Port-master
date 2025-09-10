package com.eightsidedsquare.contentcontent.mixin;

import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = BundleContentsComponent.Builder.class)
public interface BundleItemInvoker {
    @Invoker("removeFirst")
    ItemStack invokeRemoveFirst();
}