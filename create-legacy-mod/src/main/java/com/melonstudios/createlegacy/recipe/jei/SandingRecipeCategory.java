package com.melonstudios.createlegacy.recipe.jei;

import com.melonstudios.createlegacy.CreateLegacy;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.util.ResourceLocation;

public abstract class SandingRecipeCategory<T extends IRecipeWrapper> implements IRecipeCategory<T> {
    protected static final ResourceLocation TEXTURES = new ResourceLocation(CreateLegacy.MOD_ID + ":textures/gui/sandpaper_jei.png");

    protected static final int input = 0;
    protected static final int output = 1;

    protected SandingRecipeCategory(IGuiHelper helper) {}

    public static final class Implementation extends SandingRecipeCategory<SandingRecipe> {
        private final IDrawable background;

        @Override
        public String getUid() {
            return "create.sanding";
        }

        @Override
        public String getTitle() {
            return "Sandpaper Polishing";
        }

        @Override
        public String getModName() {
            return "Create Legacy";
        }

        @Override
        public IDrawable getBackground() {
            return background;
        }

        @Override
        public void setRecipe(IRecipeLayout layout, SandingRecipe recipe, IIngredients ingredients) {
            IGuiItemStackGroup stacks = layout.getItemStacks();
            stacks.init(input, true, 1, 7);
            stacks.init(output, false, 45, 7);
            stacks.set(ingredients);
        }

        public Implementation(IGuiHelper helper) {
            super(helper);
            background = helper.createDrawable(TEXTURES, 0, 0, 64, 32);
        }
    }
}
