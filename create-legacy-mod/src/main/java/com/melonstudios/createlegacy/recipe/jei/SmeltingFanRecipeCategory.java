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

public abstract class SmeltingFanRecipeCategory<T extends IRecipeWrapper> implements IRecipeCategory<T> {
    protected static final ResourceLocation TEXTURES = new ResourceLocation(CreateLegacy.MOD_ID + ":textures/gui/bulk_no.png");
    protected static final ResourceLocation ICON = new ResourceLocation(CreateLegacy.MOD_ID + ":textures/gui/bulk_washing_icon.png");

    protected static final int input = 0;
    protected static final int output = 1;

    protected SmeltingFanRecipeCategory(IGuiHelper helper) {}

    public static final class Implementation extends SmeltingFanRecipeCategory<SmeltingFanRecipe> {
        private final IDrawable background;
        private final IDrawable icon;

        @Override
        public String getUid() {
            return "create.fan_smelting";
        }

        @Override
        public String getTitle() {
            return "Smelting by Fan";
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
        public void setRecipe(IRecipeLayout layout, SmeltingFanRecipe recipe, IIngredients ingredients) {
            IGuiItemStackGroup stacks = layout.getItemStacks();
            stacks.init(input, true, 1, 7);
            stacks.init(output, false, 45, 7);
            stacks.set(ingredients);
        }

        public Implementation(IGuiHelper helper) {
            super(helper);
            background = helper.createDrawable(TEXTURES, 0, 0, 64, 32);
            icon = helper.drawableBuilder(ICON, 0, 0, 32, 32).setTextureSize(32, 32).build();
        }
    }
}
