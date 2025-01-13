package com.melonstudios.createlegacy.recipe.jei;

import com.melonstudios.createlegacy.util.SimpleTuple;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
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
        ingredients.setInput(VanillaTypes.ITEM, input);
        List<ItemStack> stacks = new ArrayList<>();
        for (SimpleTuple<ItemStack, Float> tuple : results) {
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
    }
}
