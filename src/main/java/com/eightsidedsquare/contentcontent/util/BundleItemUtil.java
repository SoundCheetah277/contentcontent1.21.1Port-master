package com.eightsidedsquare.contentcontent.util;

import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Method;
import java.util.Optional;

public class BundleItemUtil {
    private static Method removeFirstStack;

    static {
        try {
            removeFirstStack = BundleItem.class.getDeclaredMethod("removeFirstStack", ItemStack.class);
            removeFirstStack.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Could not find removeFirstStack method", e);
        }
    }

    public static Optional<ItemStack> invokeRemoveFirstStack(ItemStack stack) {
        try {
            return (Optional<ItemStack>) removeFirstStack.invoke(null, stack);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke removeFirstStack", e);
        }
    }
}
