package com.siepert.createlegacy.util.compat.jei.recipe;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

public class MysteriousConversionRecipe implements IRecipeWrapper {
    private final ItemStack input;
    private final ItemStack output;

    public MysteriousConversionRecipe(ItemStack input, ItemStack output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(ItemStack.class, input);
        ingredients.setOutput(ItemStack.class, output);
    }


}
