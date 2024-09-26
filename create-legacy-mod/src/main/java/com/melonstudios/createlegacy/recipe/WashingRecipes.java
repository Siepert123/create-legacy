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

    private final Map<ItemStack, SimpleTuple<ItemStack, Float>[]> recipes = Maps.newHashMap();
    public static Map<ItemStack, SimpleTuple<ItemStack, Float>[]> getRecipesMap() {
        return getInstance().recipes;
    }
    @SafeVarargs
    public static void addRecipe(ItemStack input, SimpleTuple<ItemStack, Float>... results) {
        addRecipe(input, false, results);
    }
    @SafeVarargs
    public static void addRecipe(ItemStack input, boolean overwrite, SimpleTuple<ItemStack, Float>... results) {
        if (overwrite) {
            removeRecipe(input);
            getRecipesMap().put(input, results);
        } else {
            if (!getRecipesMap().containsKey(input)) {
                getRecipesMap().put(input, results);
            } else {
                DisplayLink.warn("Ignored washing recipe with input %s because it already existed", input.getDisplayName());
            }
        }
    }
    public static void removeRecipe(ItemStack input) {
        getRecipesMap().entrySet().removeIf(entry -> entry.getKey().isItemEqual(input));
    }

    public static SimpleTuple<ItemStack, Float>[] getResults(ItemStack input) {
        for (Map.Entry<ItemStack, SimpleTuple<ItemStack, Float>[]> entry : getRecipesMap().entrySet()) {
            if (entry.getKey().isItemEqual(input)) return entry.getValue();
        }

        return new SimpleTuple[]{};
    }
    public static boolean hasResult(ItemStack input) {
        for (Map.Entry<ItemStack, SimpleTuple<ItemStack, Float>[]> entry : getRecipesMap().entrySet()) {
            if (entry.getKey().isItemEqual(input)) return true;
        }
        return false;
    }
}
