package com.siepert.createlegacy.util.compat.jei.recipe;

import com.siepert.createlegacy.ModData;
import com.siepert.createlegacy.util.compat.jei.RecipeCategories;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;

public class MillingRecipeCategory extends AbstractMillingRecipeCategory<MillingRecipe> {
    private final IDrawable background;
    private final String name;


    @Override
    public String getUid() {
        return RecipeCategories.MILLING;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getModName() {
        return ModData.NAME;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, MillingRecipe recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        stacks.init(input, true, 1, 7);
        stacks.init(output, false, 26, 7);
        stacks.init(outputOptional, false, 45, 7);
        stacks.set(ingredients);
    }

    public MillingRecipeCategory(IGuiHelper helper) {
        super(helper);
        background = helper.createDrawable(TEXTURES, 0, 0, 64, 32);
        name = "Milling";
    }
}
