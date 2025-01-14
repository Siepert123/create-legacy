package com.melonstudios.createlegacy.recipe.jei;

import com.melonstudios.createlegacy.util.RecipeEntry;
import com.melonstudios.createlegacy.util.SimpleTuple;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class MillingRecipe implements IRecipeWrapper {
    public final ItemStack input;
    public final RecipeEntry[] results;
    public boolean test = false;

    public MillingRecipe(ItemStack input, RecipeEntry[] results) {
        this.input = input;
        this.results = results;
    }
    public static MillingRecipe test(ItemStack input, RecipeEntry[] results) {
        MillingRecipe recipe = new MillingRecipe(input, results);
        recipe.test = true;
        return recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.ITEM, input);
        List<ItemStack> stacks = new ArrayList<>();
        for (RecipeEntry tuple : results) {
            stacks.add(tuple.getValue1());
        }
        ingredients.setOutputs(VanillaTypes.ITEM, stacks);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        for (int i = 0; i < results.length; i++) {
            if (results[i].getValue2() < 1) {
                minecraft.fontRenderer.drawStringWithShadow(((int)(results[i].getValue2() * 100)) + "%", 45 + i * 20, -2, 0xffffff);
            }
        }
        if (test) {
            minecraft.fontRenderer.drawStringWithShadow("temporary crushing recipe", -6, 26, 0xff0000);
        }
    }
}
