package com.melonstudios.createlegacy.recipe;

import com.google.common.collect.Maps;
import com.melonstudios.createlegacy.util.DisplayLink;
import net.minecraft.item.ItemStack;

import java.util.Map;

public final class PressingRecipes {
    private static final PressingRecipes INSTANCE = new PressingRecipes();
    public static PressingRecipes getInstance() {
        return INSTANCE;
    }

    private PressingRecipes() {}

    private final Map<ItemStack, ItemStack> recipes = Maps.newHashMap();
    public static Map<ItemStack, ItemStack> getRecipesMap() {
        return getInstance().recipes;
    }
    public static void addRecipe(ItemStack input, ItemStack result) {
        addRecipe(input, result, false);
    }
    public static void addRecipe(ItemStack input, ItemStack result, boolean overwrite) {
        input.setCount(1);
        if (overwrite) {
            removeRecipe(input);
            getRecipesMap().put(input, result);
        } else {
            if (!getRecipesMap().containsKey(input)) {
                getRecipesMap().put(input, result);
            } else {
                DisplayLink.warn("Ignored pressing recipe with input %s because it already existed", input.getDisplayName());
            }
        }
    }
    public static void removeRecipe(ItemStack input) {
        getRecipesMap().entrySet().removeIf(entry -> entry.getKey().isItemEqual(input));
    }

    public static ItemStack getResult(ItemStack input) {
        for (Map.Entry<ItemStack, ItemStack> entry : getRecipesMap().entrySet()) {
            if (entry.getKey().isItemEqual(input)) return entry.getValue();
        }

        return ItemStack.EMPTY;
    }
    public static boolean hasResult(ItemStack input) {
        for (Map.Entry<ItemStack, ItemStack> entry : getRecipesMap().entrySet()) {
            if (entry.getKey().isItemEqual(input)) return true;
        }
        return false;
    }
}
