package com.eightsidedsquare.contentcontent.common.item;

import com.eightsidedsquare.contentcontent.core.ContentBlocks;
import com.eightsidedsquare.contentcontent.core.ContentEntities;
import com.eightsidedsquare.contentcontent.core.ContentItems;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

import java.util.stream.Stream;

public class SuspiciousDirtRecipe extends SpecialCraftingRecipe {
   public SuspiciousDirtRecipe(CraftingRecipeCategory craftingRecipeCategory) {
      super(identifier, CraftingRecipeCategory.MISC);
   }

   private boolean matchesRecipe(RecipeInputInventory inventory) {
      return Stream.of(inventory.getStack(0), inventory.getStack(2), inventory.getStack(6), inventory.getStack(8))
              .allMatch(ItemStack::isEmpty)
              && Stream.of(inventory.getStack(1), inventory.getStack(3), inventory.getStack(5), inventory.getStack(7))
              .allMatch(stack -> stack.isOf(Items.DIRT));
   }

   public boolean matches(RecipeInputInventory inventory, World world) {
      return this.matchesRecipe(inventory);
   }

   public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
      ItemStack output = this.getOutput();
      ItemStack loot = inventory.getStack(4).copy();
      if (this.matchesRecipe(inventory) && !loot.isEmpty()) {
         loot.setCount(1);
         loot.getOrCreateNbt().put("Enchantments", new NbtList());
         NbtCompound nbt = new NbtCompound();
         nbt.put("item", loot.writeNbt(new NbtCompound()));
         BlockItem.setBlockEntityNbt(output, ContentEntities.BRUSHABLE_BLOCK, nbt);
      }

      return output;
   }

   public ItemStack getOutput() {
      return new ItemStack(ContentBlocks.SUSPICIOUS_DIRT);
   }

   @Override
   public boolean matches(CraftingRecipeInput input, World world) {
      return false;
   }

   @Override
   public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
      return null;
   }

   public boolean fits(int width, int height) {
      return width == 3 && height == 3;
   }

   public RecipeSerializer<?> getSerializer() {
      return ContentItems.SUSPICIOUS_DIRT_RECIPE_SERIALIZER;
   }
}
