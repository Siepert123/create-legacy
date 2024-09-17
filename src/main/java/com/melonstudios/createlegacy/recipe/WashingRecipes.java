package com.melonstudios.createlegacy.recipe;

import com.google.common.collect.Maps;
import com.melonstudios.createlegacy.util.DisplayLink;
import com.melonstudios.createlegacy.util.SimpleTuple;
import net.minecraft.item.ItemStack;

import java.util.Map;

public final class WashingRecipes {
    private static final WashingRecipes INSTANCE = new WashingRecipes();
    public static WashingRecipes getInstance() {
        return INSTANCE;
    }

    private WashingRecipes() {}

    private final Map<ItemStack, ItemStack[]> recipes = Maps.newHashMap();
    public static Map<ItemStack, ItemStack[]> getRecipesMap() {
        return getInstance().recipes;
    }
    private final Map<ItemStack, float[]> chances = Maps.newHashMap();
    public static Map<ItemStack, float[]> getChancesMap() {
        return getInstance().chances;
    }
    public static void addRecipe(ItemStack input, ItemStack[] results, float[] chances) {
        addRecipe(input, results, chances, false);
    }
    public static void addRecipe(ItemStack input, ItemStack[] results, float[] chances, boolean overwrite) {
        if (results.length != chances.length) {
            DisplayLink.error("Washing recipe needs equal amount of results and chances, currently mismatches:" +
                    "%s results, %s chances", results.length, chances.length);
            return;
        }

        if (overwrite) {
            getRecipesMap().remove(input);
            getChancesMap().remove(input);
            getRecipesMap().put(input, results);
            getChancesMap().put(input, chances);
        } else {
            if (!getRecipesMap().containsKey(input)) {
                getRecipesMap().put(input, results);
                getChancesMap().put(input, chances);
            } else {
                DisplayLink.warn("Ignored washing recipe with input %s because it already existed", input.getDisplayName());
            }
        }
    }
    public static void removeRecipe(ItemStack input) {
        getRecipesMap().remove(input);
        getChancesMap().remove(input);
    }

    public static ItemStack[] getResults(ItemStack input) {
        for (Map.Entry<ItemStack, ItemStack[]> entry : getRecipesMap().entrySet()) {
            if (entry.getKey().isItemEqual(input)) return entry.getValue();
        }

        return new ItemStack[]{};
    }
    public static float[] getChances(ItemStack input) {
        for (Map.Entry<ItemStack, float[]> entry : getChancesMap().entrySet()) {
            if (entry.getKey().isItemEqual(input)) return entry.getValue();
        }

        return new float[]{};
    }
    public static boolean hasResult(ItemStack input) {
        for (Map.Entry<ItemStack, ItemStack[]> entry : getRecipesMap().entrySet()) {
            if (entry.getKey().isItemEqual(input)) return true;
        }
        return false;
    }
    public static SimpleTuple<ItemStack[], float[]> getResultsTuple(ItemStack input) {
        return new SimpleTuple<>(getResults(input), getChances(input));
    }
}
