package com.siepert.createlegacy.util.compat.jei.recipe;

import com.siepert.createlegacy.blocks.kinetic.BlockBlazeBurner;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class CompactingRecipe implements IRecipeWrapper {
    private final ItemStack input;
    private final ItemStack output;
    private final BlockBlazeBurner.State heat;

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if (!(mouseY > recipeHeight + 5) && mouseY >= -5) {
            String s = "Heating: " + heat.getVisualizer();
            minecraft.fontRenderer.drawStringWithShadow(
                    s, -2, -2, 16777215);
        }
    }

    public CompactingRecipe(ItemStack input, ItemStack output, BlockBlazeBurner.State heat) {
        this.input = input;
        this.output = output;
        this.heat = heat;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(ItemStack.class, input);
        ingredients.setOutput(ItemStack.class, output);
    }


}
