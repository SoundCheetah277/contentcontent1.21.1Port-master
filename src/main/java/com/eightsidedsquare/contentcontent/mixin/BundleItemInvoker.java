package com.eightsidedsquare.contentcontent.mixin;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(targets = "net.minecraft.component.type.BundleContentsComponent$Builder")
public interface BundleItemInvoker {

    @Invoker("removeFirst")
    ItemStack invokeRemoveFirst();
}