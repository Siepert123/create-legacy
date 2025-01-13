package com.melonstudios.createlegacy.recipe.jei;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public final class MysteriousRecipe implements IRecipeWrapper {
    public final ItemStack input;
    public final ItemStack result;

    public MysteriousRecipe(ItemStack input, ItemStack result) {
        this.input = input;
        this.result = result;
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInput(ItemStack.class, input);
        ingredients.setOutput(ItemStack.class, result);
    }
}
