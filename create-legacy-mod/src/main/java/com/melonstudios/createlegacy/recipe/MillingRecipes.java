package com.melonstudios.createlegacy.recipe;

import com.melonstudios.createlegacy.util.SimpleTuple;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class MillingRecipes {
    private MillingRecipes() {}

    private static final MillingRecipes INSTANCE = new MillingRecipes();
    public static MillingRecipes getInstance() {
        return INSTANCE;
    }

    private final Map<ItemStack, ItemStack> recipeMap = new HashMap<>();
    private final Map<ItemStack, Integer> workMap = new HashMap<>();
    public static Map<ItemStack, ItemStack> getRecipesMap() {
        return getInstance().recipeMap;
    }
    public static Map<ItemStack, Integer> getWorkMap() {
        return getInstance().workMap;
    }

    public static void addRecipe(ItemStack input, ItemStack result, float seconds) {
        addRecipe(input, result, Math.round(seconds * 20 * 64));
    }
    public static void addRecipe(ItemStack input, ItemStack result, int work) {
        getRecipesMap().put(input, result);
        getWorkMap().put(input, work);
    }

    public static boolean hasRecipe(ItemStack input) {
        for (Map.Entry<ItemStack, ItemStack> entry : getRecipesMap().entrySet()) {
            if (entry.getKey().isItemEqual(input)) return true;
        }
        return false;
    }

    public static SimpleTuple<ItemStack, Integer> getResult(ItemStack input) {
        ItemStack stack = ItemStack.EMPTY;
        int work = 0;
        for (Map.Entry<ItemStack, ItemStack> entry : getRecipesMap().entrySet()) {
            if (entry.getKey().isItemEqual(input)) stack = entry.getValue().copy();
        }
        for (Map.Entry<ItemStack, Integer> entry : getWorkMap().entrySet()) {
            if (entry.getKey().isItemEqual(input)) work = entry.getValue();
        }

        return SimpleTuple.from(stack, work);
    }
}
