package com.siepert.createlegacy.util.compat.jei.recipe;

import com.siepert.createlegacy.util.Reference;
import com.siepert.createlegacy.util.compat.jei.RecipeCategories;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;

public class PressingRecipeCategory extends AbstractPressingRecipeCategory<PressingRecipe> {
    private final IDrawable background;
    private final String name;


    @Override
    public String getUid() {
        return RecipeCategories.PRESSING;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getModName() {
        return Reference.NAME;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, PressingRecipe recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        stacks.init(input, true, 1, 7);
        stacks.init(output, false, 45, 7);
        stacks.set(ingredients);
    }

    public PressingRecipeCategory(IGuiHelper helper) {
        super(helper);
        background = helper.createDrawable(TEXTURES, 0, 0, 64, 32);
        name = "Pressing";
    }
}
