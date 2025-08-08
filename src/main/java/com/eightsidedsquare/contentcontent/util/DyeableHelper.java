package com.eightsidedsquare.contentcontent.util;

import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.List;

public final class DyeableHelper {
    private static final int DEFAULT_COLOR = 0xA06540; // vanilla leather default

    private DyeableHelper() {} // prevent instantiation

    public static int getColor(ItemStack stack) {
        NbtCompound display = stack.getSubNbt("display");
        return (display != null && display.contains("color", NbtElement.INT_TYPE))
                ? display.getInt("color")
                : DEFAULT_COLOR;
    }

    public static void setColor(ItemStack stack, int color) {
        stack.getOrCreateSubNbt("display").putInt("color", color);
    }

    public static boolean hasColor(ItemStack stack) {
        NbtCompound display = stack.getSubNbt("display");
        return display != null && display.contains("color", NbtElement.INT_TYPE);
    }

    public static void removeColor(ItemStack stack) {
        NbtCompound display = stack.getSubNbt("display");
        if (display != null && display.contains("color")) {
            display.remove("color");
        }
    }

    public static ItemStack blendAndSetColor(ItemStack result, List<DyeItem> list) {
        return result;
    }
}
