package com.siepert.createlegacy.util.compat.jei.recipe;

import com.google.common.collect.Lists;
import com.siepert.createlegacy.blocks.kinetic.BlockBlazeBurner;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.handlers.recipes.CompactingRecipes;
import com.siepert.createlegacy.util.handlers.recipes.MillingRecipes;
import com.siepert.createlegacy.util.handlers.recipes.PressingRecipes;
import com.siepert.createlegacy.util.handlers.recipes.WashingRecipes;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
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

    public static List<MillingRecipe> getMillingRecipes(IJeiHelpers helpers) {
        IStackHelper stackHelper = helpers.getStackHelper();
        MillingRecipes instance = MillingRecipes.instance();

        Map<ItemStack, ItemStack> recipeList = instance.getResultList();
        Map<ItemStack, ItemStack> recipeListOptional = instance.getOptionalResultList();
        Map<ItemStack, Integer> timingsList = instance.getTimingsList();
        Map<ItemStack, Integer> chancesList = instance.getPercentageList();

        List<MillingRecipe> jeiRecipes = Lists.newArrayList();

        for (Map.Entry<ItemStack, ItemStack> entry : recipeList.entrySet()) {
            ItemStack input = entry.getKey();

            ItemStack output = entry.getValue();
            ItemStack outputOptional = recipeListOptional.get(input);

            List<ItemStack> outputs = Lists.newArrayList(output, outputOptional);

            MillingRecipe recipe = new MillingRecipe(input, outputs, timingsList.get(input), chancesList.get(input));
            jeiRecipes.add(recipe);
        }

        return jeiRecipes;
    }

    public static List<PressingRecipe> getPressingRecipes(IJeiHelpers helpers) {
        IStackHelper stackHelper = helpers.getStackHelper();
        PressingRecipes instance = PressingRecipes.instance();

        Map<ItemStack, ItemStack> recipeList = instance.getPressingList();

        List<PressingRecipe> jeiRecipes = Lists.newArrayList();

        for (Map.Entry<ItemStack, ItemStack> entry : recipeList.entrySet()) {
            ItemStack input = entry.getKey();

            ItemStack output = entry.getValue();

            PressingRecipe recipe = new PressingRecipe(input, output);
            jeiRecipes.add(recipe);
        }

        return jeiRecipes;
    }

    public static List<CompactingRecipe> getCompactingRecipes(IJeiHelpers helpers) {
        IStackHelper stackHelper = helpers.getStackHelper();
        CompactingRecipes instance = CompactingRecipes.instance();

        Map<ItemStack, ItemStack> recipeList = instance.getCompactingList();
        Map<ItemStack, BlockBlazeBurner.State> heatingList = instance.getHeatRequirementsList();

        List<CompactingRecipe> jeiRecipes = Lists.newArrayList();

        for (Map.Entry<ItemStack, ItemStack> entry : recipeList.entrySet()) {
            ItemStack input = entry.getKey();

            ItemStack output = entry.getValue();

            CompactingRecipe recipe = new CompactingRecipe(input, output, heatingList.get(entry.getKey()));
            jeiRecipes.add(recipe);
        }

        return jeiRecipes;
    }

    public static List<MysteriousConversionRecipe> getMysteryRecipes(IJeiHelpers helpers) {

        List<MysteriousConversionRecipe> recipes = Lists.newArrayList();

        recipes.add(new MysteriousConversionRecipe(
                new ItemStack(ModItems.INGREDIENT, 1, 19),
                new ItemStack(ModItems.INGREDIENT, 1, 20)
        ));

        recipes.add(new MysteriousConversionRecipe(
                new ItemStack(ModBlocks.BLAZE_BURNER, 1, 0),
                new ItemStack(ModBlocks.BLAZE_BURNER, 1, 1)
        ));

        return recipes;
    }
}
