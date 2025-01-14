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

import javax.annotation.Nullable;

public abstract class MillingRecipeCategory<T extends IRecipeWrapper> implements IRecipeCategory<T> {
    protected static final ResourceLocation TEXTURES = new ResourceLocation(CreateLegacy.MOD_ID + ":textures/gui/bulk_boring.png");

    protected static final int input = 0;
    protected static final int output_1 = 1;

    protected MillingRecipeCategory(IGuiHelper helper) {}

    public static class Implementation extends MillingRecipeCategory<MillingRecipe> {
        private final IDrawable background;

        protected Implementation(IGuiHelper helper) {
            super(helper);
            background = helper.createDrawable(TEXTURES, 0, 0, 124, 32);
        }

        @Override
        public String getUid() {
            return "create.milling";
        }

        @Override
        public String getTitle() {
            return "Milling";
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
        public void setRecipe(IRecipeLayout layout, MillingRecipe recipe, IIngredients ingredients) {
            IGuiItemStackGroup stacks = layout.getItemStacks();
            stacks.init(input, true, 1, 7);
            int counter = 1;
            for (int i = 0; i < recipe.results.length; i ++) {
                stacks.init(counter, false, 45 + i * 20, 7);
                counter++;
            }
            stacks.set(ingredients);
        }
    }
}
