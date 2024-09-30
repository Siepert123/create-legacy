package com.melonstudios.createlegacy.recipe;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SawingRecipes {
    private SawingRecipes() {}
    private static final SawingRecipes INSTANCE = new SawingRecipes();
    public static SawingRecipes instance() {
        return INSTANCE;
    }

    private final Map<ItemStack, List<ItemStack>> recipes = new HashMap<>();
    public static Map<ItemStack, List<ItemStack>> getRecipesMap() {
        return instance().recipes;
    }

    public static void addRecipe(ItemStack input, ItemStack result) {
        boolean appended = false;
        for (Map.Entry<ItemStack, List<ItemStack>> entry : getRecipesMap().entrySet()) {
            if (entry.getKey().isItemEqual(input)) {
                entry.getValue().add(result);
                appended = true;
            }
        }
        if (!appended) {
            List<ItemStack> list = new ArrayList<>();
            list.add(result);
            getRecipesMap().put(input, list);
        }
    }

    public static boolean hasResult(ItemStack input) {
        for (Map.Entry<ItemStack, List<ItemStack>> entry : getRecipesMap().entrySet()) {
            if (entry.getKey().isItemEqual(input)) {
                return true;
            }
        }
        return false;
    }

    @Nonnull
    public static ItemStack getResult(ItemStack input, int index) {
        for (Map.Entry<ItemStack, List<ItemStack>> entry : getRecipesMap().entrySet()) {
            if (entry.getKey().isItemEqual(input)) {
                return entry.getValue().get(index % entry.getValue().size());
            }
        }
        return ItemStack.EMPTY;
    }
}
