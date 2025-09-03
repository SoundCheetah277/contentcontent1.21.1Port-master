package com.eightsidedsquare.contentcontent.common.item;

import com.eightsidedsquare.contentcontent.core.ContentItems;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.*;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;
import com.eightsidedsquare.contentcontent.util.DyeableHelper;

import java.util.ArrayList;
import java.util.List;

public class WrappedBundleRecipe extends SpecialCraftingRecipe {
   public WrappedBundleRecipe(CraftingRecipeCategory craftingRecipeCategory) {
      super(CraftingRecipeCategory.MISC);
   }

   public boolean matches(RecipeInputInventory inventory, World world) {
      boolean dye = false;
      boolean bundle = false;
      boolean string = false;

      for (int i = 0; i < inventory.size(); i++) {
         ItemStack stack = inventory.getStack(i);
         if (!stack.isEmpty()) {
            if (stack.isOf(Items.BUNDLE) && BundleItem.getAmountFilled(stack) > 0.0F && !bundle) {
               bundle = true;
            } else if (stack.getItem() instanceof DyeItem) {
               dye = true;
            } else {
               if (!stack.isOf(Items.STRING) || string) {
                  return false;
               }

               string = true;
            }
         }
      }

      return dye && bundle && string;
   }

   public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
      List<DyeItem> list = new ArrayList<>();
      ItemStack result = new ItemStack(ContentItems.WRAPPED_BUNDLE);

      for (int i = 0; i < inventory.size(); i++) {
         ItemStack stack = inventory.getStack(i);
         if (stack.getItem() instanceof DyeItem dyeItem) {
            list.add(dyeItem);
         }

         if (stack.isOf(Items.BUNDLE)) {
            if (stack.contains(DataComponentTypes.BUNDLE_CONTENTS)) {
               BundleContentsComponent bundleContents = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
               if (bundleContents != null) {
                  result.set(DataComponentTypes.BUNDLE_CONTENTS, bundleContents);
               }
            }
         }
      }

      return !list.isEmpty() ? DyeableHelper.blendAndSetColor(result, list) : this.getOutput();
   }

   @Override
   public boolean matches(CraftingRecipeInput input, World world) {
      return false;
   }

   @Override
   public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
      return null;
   }

   @Override
   public boolean fits(int width, int height) {
      return width * height >= 3;
   }

   @Override
   public RecipeSerializer<?> getSerializer() {
      return ContentItems.WRAPPED_BUNDLE_RECIPE_SERIALIZER;
   }

   public ItemStack getOutput() {
      return new ItemStack(ContentItems.WRAPPED_BUNDLE);
   }
}
