package com.eightsidedsquare.contentcontent.util;

import com.eightsidedsquare.contentcontent.core.ContentMod;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import java.util.List;

public class DyeableHelper {
    private static final int DEFAULT_COLOR = 0xA06540;

    public static int getColorValue(ItemStack stack) {
        Integer col = stack.get(ContentMod.COLOR_COMPONENT);
        return col == null ? DEFAULT_COLOR : col;
    }

    // Add this method that was missing
    public static int getColor(ItemStack stack) {
        return getColorValue(stack);
    }

    public static void setColor(ItemStack stack, int color) {
        stack.set(ContentMod.COLOR_COMPONENT, color);
    }

    public static boolean hasCustomColor(ItemStack stack) {
        return stack.get(ContentMod.COLOR_COMPONENT) != null;
    }

    public static void removeColor(ItemStack stack) {
        stack.remove(ContentMod.COLOR_COMPONENT);
    }

    // Add this method that was missing
    public static ItemStack blendAndSetColor(ItemStack stack, List<DyeItem> dyes) {
        if (dyes.isEmpty()) {
            return stack;
        }

        int[] colorComponents = new int[3];
        int maxColorSum = 0;
        int colorCount = 0;

        // If the item already has a color, include it in the blend
        if (hasCustomColor(stack)) {
            int existingColor = getColor(stack);
            colorComponents[0] += (existingColor >> 16) & 255; // Red
            colorComponents[1] += (existingColor >> 8) & 255;  // Green
            colorComponents[2] += existingColor & 255;         // Blue
            maxColorSum += Math.max(colorComponents[0], Math.max(colorComponents[1], colorComponents[2]));
            colorCount++;
        }

        // Add each dye color to the blend
        for (DyeItem dyeItem : dyes) {
            DyeColor dyeColor = dyeItem.getColor();
            int color = dyeColor.getSignColor(); // Get the dye color value

            colorComponents[0] += (color >> 16) & 255; // Red
            colorComponents[1] += (color >> 8) & 255;  // Green
            colorComponents[2] += color & 255;         // Blue

            maxColorSum += Math.max((color >> 16) & 255, Math.max((color >> 8) & 255, color & 255));
            colorCount++;
        }

        if (colorCount == 0) {
            return stack;
        }

        // Calculate average color
        int avgRed = colorComponents[0] / colorCount;
        int avgGreen = colorComponents[1] / colorCount;
        int avgBlue = colorComponents[2] / colorCount;

        // Apply brightness adjustment
        int avgMaxComponent = maxColorSum / colorCount;
        int maxComponent = Math.max(avgRed, Math.max(avgGreen, avgBlue));

        if (maxComponent > 0) {
            avgRed = avgRed * avgMaxComponent / maxComponent;
            avgGreen = avgGreen * avgMaxComponent / maxComponent;
            avgBlue = avgBlue * avgMaxComponent / maxComponent;
        }

        // Combine into final color and set it
        int finalColor = (avgRed << 16) | (avgGreen << 8) | avgBlue;
        setColor(stack, finalColor);

        return stack;
    }
}