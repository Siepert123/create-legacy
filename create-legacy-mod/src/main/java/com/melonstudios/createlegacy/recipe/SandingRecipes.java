package com.melonstudios.createlegacy.recipe;

import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;

import java.util.Map;

public final class SandingRecipes {
    private SandingRecipes() {}

    private static final SandingRecipes INSTANCE = new SandingRecipes();
    public static SandingRecipes getInstance() {
        return INSTANCE;
    }

    private final Map<ItemStack, ItemStack> recipes = Maps.newHashMap();
    public static Map<ItemStack, ItemStack> getRecipesMap() {
        return getInstance().recipes;
    }

    public static void addRecipe(ItemStack input, ItemStack result) {
        getInstance().recipes.put(input, result);
    }

    public static boolean hasResult(ItemStack input) {
        for (Map.Entry<ItemStack, ItemStack> entry : getRecipesMap().entrySet()) {
            if (entry.getKey().isItemEqual(input)) return true;
        }
        return false;
    }
    public static ItemStack getResult(ItemStack input) {
        for (Map.Entry<ItemStack, ItemStack> entry : getRecipesMap().entrySet()) {
            if (entry.getKey().isItemEqual(input)) return entry.getValue();
        }
        return ItemStack.EMPTY;
    }
}
