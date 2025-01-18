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

public abstract class WashingRecipeCategory<T extends IRecipeWrapper> implements IRecipeCategory<T> {
    protected static final ResourceLocation TEXTURES = new ResourceLocation(CreateLegacy.MOD_ID + ":textures/gui/bulk.png");
    protected static final ResourceLocation ICON = new ResourceLocation(CreateLegacy.MOD_ID + ":textures/gui/bulk_washing_icon.png");

    protected static final int input = 0;
    protected static final int output_1 = 1;

    protected WashingRecipeCategory(IGuiHelper helper) {}

    public static class Implementation extends WashingRecipeCategory<WashingRecipe> {
        private final IDrawable background;
        private final IDrawable icon;

        protected Implementation(IGuiHelper helper) {
            super(helper);
            background = helper.createDrawable(TEXTURES, 0, 0, 124, 32);
            icon = helper.drawableBuilder(ICON, 0, 0, 16, 16)
                    .setTextureSize(16, 16)
                    .build();
        }

        @Override
        public String getUid() {
            return "create.washing";
        }

        @Override
        public String getTitle() {
            return "Washing by Fan";
        }

        @Override
        public String getModName() {
            return "Create Legacy";
        }

        @Override
        public IDrawable getBackground() {
            return background;
        }

        @Nullable
        @Override
        public IDrawable getIcon() {
            return icon;
        }

        @Override
        public void setRecipe(IRecipeLayout layout, WashingRecipe recipe, IIngredients ingredients) {
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
