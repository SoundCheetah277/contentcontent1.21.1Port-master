package com.eightsidedsquare.contentcontent.util;

import com.eightsidedsquare.contentcontent.common.item.BoxingGloveItem;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.List;

public final class DyeableHelper {
    private static final int DEFAULT_COLOR = 0xA06540; // vanilla leather default

    private DyeableHelper() {} // prevent instantiation

    public static int getColor(ItemStack stack) {
        Integer col = stack.get(BoxingGloveItem.ContentContentComponents.COLOR);
        return col != null ? col : DEFAULT_COLOR;
    }


    public static void setColor(ItemStack stack, int color) {
        stack.set(BoxingGloveItem.ContentContentComponents.COLOR, color);
    }

    public static boolean hasColor(ItemStack stack) {
        return stack.get(BoxingGloveItem.ContentContentComponents.COLOR) != null;
    }

    public static void removeColor(ItemStack stack) {
        stack.remove(BoxingGloveItem.ContentContentComponents.COLOR);
    }
    public static ItemStack blendAndSetColor(ItemStack result, List<DyeItem> list) {
        return result;
    }
}
