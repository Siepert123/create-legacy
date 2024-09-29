package com.melonstudios.createlegacy.recipe.jei;

import com.melonstudios.createlegacy.util.SimpleTuple;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class WashingRecipe implements IRecipeWrapper {
    public final ItemStack input;
    public final SimpleTuple<ItemStack, Float>[] results;

    public WashingRecipe(ItemStack input, SimpleTuple<ItemStack, Float>[] results) {
        this.input = input;
        this.results = results;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(ItemStack.class, input);
        List<ItemStack> stacks = new ArrayList<>();
        for (SimpleTuple<ItemStack, Float> tuple : results) {
            stacks.add(tuple.getValue1());
        }
        ingredients.setOutputs(ItemStack.class, Arrays.asList(stacks));
    }
}
