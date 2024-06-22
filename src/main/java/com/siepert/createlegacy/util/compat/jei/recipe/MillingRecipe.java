package com.siepert.createlegacy.util.compat.jei.recipe;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.List;

public class MillingRecipe implements IRecipeWrapper {
    private final ItemStack input;
    private final List<ItemStack> outputs;
    private final int millTime, optionalChance;

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return IRecipeWrapper.super.getTooltipStrings(mouseX, mouseY);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if (/*!(mouseX > recipeWidth) && mouseX >= 0 &&*/ !(mouseY > recipeHeight + 5) && mouseY >= -5) {
            String s = millTime + "t";
            minecraft.fontRenderer.drawStringWithShadow(
                    s, -2, -2, 16777215);

            if (!outputs.get(1).isEmpty()) {
                minecraft.fontRenderer.drawStringWithShadow(
                        optionalChance + "%", 45, -2, 16777215);
            }
        }
    }

    public MillingRecipe(ItemStack input, List<ItemStack> outputs, int millTime, int optionalChance) {
        this.input = input;
        this.outputs = outputs;
        this.millTime = millTime;
        this.optionalChance = optionalChance;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(ItemStack.class, input);
        ingredients.setOutputs(ItemStack.class, outputs);
    }


}
