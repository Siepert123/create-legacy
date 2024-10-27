package com.melonstudios.createlegacy.recipe;

import com.melonstudios.createlegacy.util.RecipeEntry;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class CrushingRecipes {
    private CrushingRecipes() {}

    private static final CrushingRecipes INSTANCE = new CrushingRecipes();
    public static CrushingRecipes getInstance() {
        return INSTANCE;
    }

    private final Map<ItemStack, RecipeEntry[]> recipesMap = new HashMap<>();
    private final Map<ItemStack, Integer> workMap = new HashMap<>();
    public static Map<ItemStack, RecipeEntry[]> getRecipesMap() {
        return getInstance().recipesMap;
    }
    public static Map<ItemStack, Integer> getWorkMap() {
        return getInstance().workMap;
    }

    public static void addRecipe(ItemStack input, float seconds, RecipeEntry... results) {
        addRecipe(input, Math.round(seconds * 20f * 64f), results);
    }
    public static void addRecipe(ItemStack input, int work, RecipeEntry... results) {
        if (results.length > 3) return;
        if (work <= 0) return;
        if (input.isEmpty()) return;
        if (results.length == 0) return;
        if (results.length == 1) {
            results = new RecipeEntry[] {
                    results[0], RecipeEntry.EMPTY, RecipeEntry.EMPTY
            };
        }
        if (results.length == 2) {
            results = new RecipeEntry[] {
                    results[0], results[1], RecipeEntry.EMPTY
            };
        }
        getRecipesMap().put(input, results);
        getWorkMap().put(input, work);
    }

    public static boolean hasResult(ItemStack input) {
        for (Map.Entry<ItemStack, RecipeEntry[]> entry : getRecipesMap().entrySet()) {
            if (entry.getKey().isItemEqual(input)) return true;
        }

        return false;
    }
    public static int getWork(ItemStack input) {
        for (Map.Entry<ItemStack, Integer> entry : getWorkMap().entrySet()) {
            if (entry.getKey().isItemEqual(input)) return entry.getValue();
        }
        return 0;
    }
    public static RecipeEntry[] getResults(ItemStack input) {
        for (Map.Entry<ItemStack, RecipeEntry[]> entry : getRecipesMap().entrySet()) {
            if (entry.getKey().isItemEqual(input)) return entry.getValue();
        }
        return new RecipeEntry[]{
                RecipeEntry.get(ItemStack.EMPTY, 0.0f),
                RecipeEntry.get(ItemStack.EMPTY, 0.0f),
                RecipeEntry.get(ItemStack.EMPTY, 0.0f)
        };
    }
}
