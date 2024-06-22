package com.siepert.createlegacy.util.compat.jei.recipe;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.List;

public class WashingRecipe implements IRecipeWrapper {
    private final ItemStack input;
    private final List<ItemStack> outputs;

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

        if (/*!(mouseX > recipeWidth) && mouseX >= 0 &&*/ !(mouseY > recipeHeight + 5) && mouseY >= -5) {
            String s;
            if (!outputs.get(1).isEmpty()) {
                s = "25%";
                minecraft.fontRenderer.drawStringWithShadow(
                        s, 45, -2, 16777215);
            }
        }
    }

    public WashingRecipe(ItemStack input, List<ItemStack> outputs) {
        this.input = input;
        this.outputs = outputs;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(ItemStack.class, input);
        ingredients.setOutputs(ItemStack.class, outputs);
    }


}
