package com.siepert.createlegacy.util.compat.jei.recipe;

import com.google.common.collect.Lists;
import com.siepert.createlegacy.util.handlers.recipes.WashingRecipes;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Map;

public class RecipeMaker {
    public static List<WashingRecipe> getWashingRecipes(IJeiHelpers helpers) {
        IStackHelper stackHelper = helpers.getStackHelper();
        WashingRecipes instance = WashingRecipes.instance();

        Map<ItemStack, ItemStack> recipeList = instance.getWashingList();
        Map<ItemStack, ItemStack> recipeListOptional = instance.getWashingListOptional();

        List<WashingRecipe> jeiRecipes = Lists.newArrayList();

        for (Map.Entry<ItemStack, ItemStack> entry : recipeList.entrySet()) {
            ItemStack input = entry.getKey();

            ItemStack output = entry.getValue();
            ItemStack outputOptional = recipeListOptional.get(input);

            List<ItemStack> outputs = Lists.newArrayList(output, outputOptional);

            WashingRecipe recipe = new WashingRecipe(input, outputs);
            jeiRecipes.add(recipe);
        }

        return jeiRecipes;
    }
}
