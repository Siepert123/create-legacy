package com.siepert.createlegacy.util.compat.jei;

import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.util.compat.jei.recipe.*;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

import java.util.IllegalFormatException;

@JEIPlugin
public class JEICompat implements IModPlugin {
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        final IJeiHelpers helpers = registry.getJeiHelpers();

        final IGuiHelper gui = helpers.getGuiHelper();

        registry.addRecipeCategories(new WashingRecipeCategory(gui));
        registry.addRecipeCategories(new MillingRecipeCategory(gui));
        registry.addRecipeCategories(new PressingRecipeCategory(gui));
        registry.addRecipeCategories(new CompactingRecipeCategory(gui));
    }

    @Override
    public void register(IModRegistry registry) {
        final IIngredientRegistry ingredientRegistry = registry.getIngredientRegistry();
        final IJeiHelpers jeiHelpers = registry.getJeiHelpers();

        IRecipeTransferRegistry recipeTransfer = registry.getRecipeTransferRegistry();

        registry.addRecipes(RecipeMaker.getWashingRecipes(jeiHelpers), RecipeCategories.WASHING_BY_FAN);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.FAN), RecipeCategories.WASHING_BY_FAN);

        registry.addRecipes(RecipeMaker.getMillingRecipes(jeiHelpers), RecipeCategories.MILLING);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.MILLSTONE), RecipeCategories.MILLING);

        registry.addRecipes(RecipeMaker.getPressingRecipes(jeiHelpers), RecipeCategories.PRESSING);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.PRESS), RecipeCategories.PRESSING);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.ITEM_HOLDER, 1, 0), RecipeCategories.PRESSING);

        registry.addRecipes(RecipeMaker.getCompactingRecipes(jeiHelpers), RecipeCategories.COMPACTING);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.PRESS), RecipeCategories.COMPACTING);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.ITEM_HOLDER, 1, 1), RecipeCategories.COMPACTING);


    }

    public static String translateToLocal(String key) {
        if (I18n.canTranslate(key)) return I18n.translateToLocal(key);
        else return I18n.translateToFallback(key);
    }

    public static String translateToLocalFormatted(String key, Object... format) {
        String s = translateToLocal(key);
        try {
            return String.format(s, format);
        } catch (IllegalFormatException e) {
            return "Format Error: " + s;
        }
    }
}
