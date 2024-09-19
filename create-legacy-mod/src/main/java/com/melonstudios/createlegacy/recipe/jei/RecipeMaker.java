package com.melonstudios.createlegacy.recipe.jei;

import com.melonstudios.createlegacy.recipe.PressingRecipes;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class RecipeMaker {
    public static List<PressingRecipe> getPressingRecipes(IJeiHelpers helpers) {
        IStackHelper stackHelper = helpers.getStackHelper();
        PressingRecipes instance = PressingRecipes.getInstance();

        Map<ItemStack, ItemStack> recipeMap = PressingRecipes.getRecipesMap();
        List<PressingRecipe> recipeList = new ArrayList<>();

        for (Map.Entry<ItemStack, ItemStack> entry : recipeMap.entrySet()) {
            recipeList.add(new PressingRecipe(entry.getKey(), entry.getValue()));
        }

        return recipeList;
    }
}
