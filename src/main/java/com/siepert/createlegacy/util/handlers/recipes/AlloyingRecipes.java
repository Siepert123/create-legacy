package com.siepert.createlegacy.util.handlers.recipes;

import com.google.common.collect.Maps;
import com.siepert.createlegacy.util.handlers.StackSet;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class AlloyingRecipes {
    private static final AlloyingRecipes ALLOYING_RECIPES = new AlloyingRecipes();

    public static AlloyingRecipes getInstance() {
        return ALLOYING_RECIPES;
    }

    private final Map<StackSet, ItemStack> recipes = Maps.newHashMap();

    private AlloyingRecipes() {

    }


}
