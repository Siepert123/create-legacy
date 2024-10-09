package com.melonstudios.createlegacy.recipe;

import com.melonstudios.createlegacy.util.SimpleTuple;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class DeployingRecipes {
    private static final DeployingRecipes INSTANCE = new DeployingRecipes();
    public static DeployingRecipes getInstance() {
        return INSTANCE;
    }

    private final Map<SimpleTuple<ItemStack, ItemStack>, ItemStack> recipeMap = new HashMap<>(); //Base item, application, result
    public static Map<SimpleTuple<ItemStack, ItemStack>, ItemStack> getRecipeMap() {
        return getInstance().recipeMap;
    }

    public static void addRecipe(ItemStack baseItem, ItemStack application, ItemStack result) {
        SimpleTuple<ItemStack, ItemStack> inputs = SimpleTuple.from(baseItem, application);

        if (getRecipeMap().containsKey(inputs)) return;

        getRecipeMap().put(inputs, result);
    }

    public static ItemStack getResult(ItemStack baseItem, ItemStack application) {
        SimpleTuple<ItemStack, ItemStack> inputs = SimpleTuple.from(baseItem, application);
        for (Map.Entry<SimpleTuple<ItemStack, ItemStack>, ItemStack> entry : getRecipeMap().entrySet()) {
            if (entry.getKey() == inputs) return entry.getValue().copy();
        }
        return ItemStack.EMPTY;
    }
    public static boolean hasResult(ItemStack baseItem, ItemStack application) {
        return !getResult(baseItem, application).isEmpty();
    }
}
