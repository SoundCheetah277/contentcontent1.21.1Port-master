package com.eightsidedsquare.contentcontent.common.item;

import com.eightsidedsquare.contentcontent.core.ContentInstruments;
import com.eightsidedsquare.contentcontent.core.ContentItems;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.Instrument;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;

import java.util.Optional;

public class CopperHornRecipe extends SpecialCraftingRecipe {
   public CopperHornRecipe(CraftingRecipeCategory craftingRecipeCategory) {
      super(identifier, craftingRecipeCategory);
   }

   public boolean matches(RecipeInputInventory inventory, World world) {
      boolean topEmpty = inventory.getStack(0).isEmpty() &&
              inventory.getStack(1).isEmpty() &&
              inventory.getStack(2).isEmpty();

      boolean middleCorrect = inventory.getStack(3).isOf(Items.COPPER_INGOT) &&
              inventory.getStack(4).isOf(Items.GOAT_HORN) &&
              inventory.getStack(5).isOf(Items.COPPER_INGOT);

      boolean bottomCorrect = inventory.getStack(6).isEmpty() &&
              inventory.getStack(7).isOf(Items.COPPER_INGOT) &&
              inventory.getStack(8).isEmpty();

      return topEmpty && middleCorrect && bottomCorrect;
   }

   public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
      return createCopperHorn(inventory.getStack(4));
   }

   private ItemStack createCopperHorn(ItemStack hornStack) {
      Optional<RegistryEntry.Reference<Instrument>> optional = ContentInstruments.getInstrument(hornStack);
      if (!optional.isPresent()) return getOutput();

      Instrument goatHornInstrument = optional.get().value();
      ContentInstruments.CopperHornInstrument copperHornInstrument =
              ContentInstruments.getCraftedCopperHorn(goatHornInstrument);

      ItemStack result = getOutput();
      NbtCompound nbt = new NbtCompound();
      nbt.putString("instrument",
              Registries.INSTRUMENT.getId(copperHornInstrument.melody).toString());
      result.setNbt(nbt);
      return result;
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

   public ItemStack getOutput() {
      return new ItemStack(ContentItems.COPPER_HORN);
   }

   public RecipeSerializer<?> getSerializer() {
      return ContentItems.COPPER_HORN_RECIPE_SERIALIZER;
   }
}
