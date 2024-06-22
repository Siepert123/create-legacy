package com.siepert.createlegacy.util.compat.jei.recipe;

import com.siepert.createlegacy.util.Reference;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public abstract class AbstractWashingRecipeCategory<T extends IRecipeWrapper> implements IRecipeCategory<T> {
    protected static final ResourceLocation TEXTURES = new ResourceLocation(Reference.MOD_ID + ":textures/gui/singleton_with_optional.png");

    protected static final int input = 0;
    protected static final int output = 1;
    protected static final int outputOptional = 2;

    public AbstractWashingRecipeCategory(IGuiHelper helper) {

    }
}
