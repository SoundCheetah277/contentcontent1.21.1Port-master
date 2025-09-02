package com.eightsidedsquare.contentcontent.common.item;

import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public class WrappedBundleItem extends AliasedBlockItem {

   private static final int DEFAULT_COLOR = 0xA06540; // Default color or whatever you want

   public WrappedBundleItem(Block block, Item.Settings settings) {
      super(block, settings);
   }

   // Copy and use these static methods in any dyeable item class!
   public static int getColor(ItemStack stack) {
      NbtCompound display = stack.get("display");
      return (display != null && display.contains("color", NbtElement.INT_TYPE))
              ? display.getInt("color")
              : DEFAULT_COLOR;
   }

   public static void setColor(ItemStack stack, int color) {
      stack.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(color, false));
   }

   public static boolean hasColor(ItemStack stack) {
      return stack.contains(DataComponentTypes.DYED_COLOR);
   }

   public static void removeColor(ItemStack stack) {
      NbtCompound display = stack.get("display");
      if (display != null && display.contains("color")) {
         display.remove("color");
      }
   }

   // For color rendering, use this in ColorProviderRegistry
   public int getColorForRender(ItemStack stack, int tintIndex) {
      return tintIndex == 0 ? getColor(stack) : 0xFFFFFF;
   }
}
