package com.melonstudios.createlegacy.recipe.jei;

import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.recipe.*;
import com.melonstudios.createlegacy.util.RecipeEntry;
import com.melonstudios.createlegacy.util.SimpleTuple;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

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

    public static List<SandingRecipe> getSandingRecipes(IJeiHelpers helpers) {
        IStackHelper stackHelper = helpers.getStackHelper();
        SandingRecipes instance = SandingRecipes.getInstance();

        Map<ItemStack, ItemStack> recipeMap = SandingRecipes.getRecipesMap();
        List<SandingRecipe> recipeList = new ArrayList<>();

        for (Map.Entry<ItemStack, ItemStack> entry : recipeMap.entrySet()) {
            recipeList.add(new SandingRecipe(entry.getKey(), entry.getValue()));
        }

        return recipeList;
    }

    public static List<MysteriousRecipe> getMysteriousRecipes(IJeiHelpers helpers) {
        List<MysteriousRecipe> recipeList = new ArrayList<>();

        recipeList.add(new MysteriousRecipe(
                new ItemStack(ModBlocks.BLAZE_BURNER, 1, 0),
                new ItemStack(ModBlocks.BLAZE_BURNER, 1, 1)
        ));

        return recipeList;
    }

    public static List<SawingRecipe> getSawingRecipes(IJeiHelpers helpers) {
        IStackHelper stackHelper = helpers.getStackHelper();
        SawingRecipes instance = SawingRecipes.instance();

        Map<ItemStack, List<ItemStack>> recipeMap = SawingRecipes.getRecipesMap();
        List<SawingRecipe> recipeList = new ArrayList<>();

        for (Map.Entry<ItemStack, List<ItemStack>> entry : recipeMap.entrySet()) {
            for (ItemStack stack : entry.getValue()) {
                recipeList.add(new SawingRecipe(entry.getKey(), stack));
            }
        }

        return recipeList;
    }

    public static List<WashingRecipe> getWashingRecipes(IJeiHelpers helpers) {
        Map<ItemStack, SimpleTuple<ItemStack, Float>[]> recipeMap = WashingRecipes.getRecipesMap();
        List<WashingRecipe> recipeList = new ArrayList<>();

        for (Map.Entry<ItemStack, SimpleTuple<ItemStack, Float>[]> entry : recipeMap.entrySet()) {
            recipeList.add(new WashingRecipe(entry.getKey(), entry.getValue()));
        }

        return recipeList;
    }
    public static List<SmeltingFanRecipe> getSmeltingRecipes(IJeiHelpers helpers) {
        Map<ItemStack, ItemStack> recipeMap = FurnaceRecipes.instance().getSmeltingList();
        List<SmeltingFanRecipe> recipeList = new ArrayList<>();

        for (Map.Entry<ItemStack, ItemStack> entry : recipeMap.entrySet()) {
            recipeList.add(new SmeltingFanRecipe(entry.getKey(), entry.getValue()));
        }

        return recipeList;
    }

    public static List<MillingRecipe> getMillingRecipes(IJeiHelpers helpers) {
        Map<ItemStack, RecipeEntry[]> recipeMap = MillingRecipes.getRecipesMap();
        List<MillingRecipe> recipeList = new ArrayList<>();

        for (Map.Entry<ItemStack, RecipeEntry[]> entry : recipeMap.entrySet()) {
            recipeList.add(new MillingRecipe(entry.getKey(), entry.getValue()));
        }
        for (Map.Entry<ItemStack, RecipeEntry[]> entry : CrushingRecipes.getRecipesMap().entrySet()) {
            recipeList.add(MillingRecipe.test(entry.getKey(), entry.getValue()));
        }

        return recipeList;
    }
}
