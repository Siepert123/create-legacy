package com.melonstudios.createlegacy.recipe;

import com.google.common.collect.Maps;
import com.melonstudios.createlegacy.util.DisplayLink;
import net.minecraft.item.ItemStack;

import java.util.Map;

public final class CompactingRecipes {
    private static final CompactingRecipes INSTANCE = new CompactingRecipes();
    public static CompactingRecipes getInstance() {
        return INSTANCE;
    }

    private CompactingRecipes() {}

    private final Map<ItemStack[], ItemStack> recipes = Maps.newHashMap();
    public static Map<ItemStack[], ItemStack> getRecipesMap() {
        return getInstance().recipes;
    }
    public static void addRecipe(ItemStack[] inputs, ItemStack result) {
        addRecipe(inputs, result, false);
    }
    public static void addRecipe(ItemStack[] inputs, ItemStack result, boolean overwrite) {
        if (overwrite) {
            getRecipesMap().remove(inputs);
            getRecipesMap().put(inputs, result);
        } else {
            if (!getRecipesMap().containsKey(inputs)) {
                getRecipesMap().put(inputs, result);
            } else {
                DisplayLink.warn("Ignored compacting recipe with inputs %s because it already existed", (Object) inputs);
            }
        }
    }
    public static void removeRecipe(ItemStack[] inputs) {
        getRecipesMap().remove(inputs);
    }

    public static ItemStack getResult(ItemStack[] inputs) {
        for (Map.Entry<ItemStack[], ItemStack> entry : getRecipesMap().entrySet()) {

        }

        return ItemStack.EMPTY; 
    }
}
