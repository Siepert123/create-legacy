package com.melonstudios.createlegacy.recipe.jei;

import com.melonstudios.createlegacy.block.ModBlocks;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@JEIPlugin
@ParametersAreNonnullByDefault
public final class JEICompat implements IModPlugin {
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        final IJeiHelpers helpers = registry.getJeiHelpers();

        final IGuiHelper gui = helpers.getGuiHelper();

        registry.addRecipeCategories(new PressingRecipeCategory.Implementation(gui));
        registry.addRecipeCategories(new SawingRecipeCategory.Implementation(gui));
        registry.addRecipeCategories(new WashingRecipeCategory.Implementation(gui));
    }

    @Override
    public void register(IModRegistry registry) {
        final IIngredientRegistry ingredientRegistry = registry.getIngredientRegistry();
        final IJeiHelpers jeiHelpers = registry.getJeiHelpers();

        IRecipeTransferRegistry recipeTransfer = registry.getRecipeTransferRegistry();


        registry.addRecipes(RecipeMaker.getPressingRecipes(jeiHelpers), "create.pressing");
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.PRESS), "create.pressing");

        registry.addRecipes(RecipeMaker.getSawingRecipes(jeiHelpers), "create.sawing");
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.SAW), "create.sawing");

        registry.addRecipes(RecipeMaker.getWashingRecipes(jeiHelpers), "create.washing");
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.FAN), "create.washing");
    }
}
