package com.melonstudios.createlegacy.util;

import net.minecraft.item.ItemStack;

public class RecipeEntry extends SimpleTuple<ItemStack, Float> {
    public RecipeEntry(ItemStack value1, Float value2) {
        super(value1, value2);
    }

    public static RecipeEntry get(ItemStack stack, float chance) {
        return new RecipeEntry(stack, chance);
    }
    public static RecipeEntry get(ItemStack stack) {
        return get(stack, 1.0f);
    }
    public static final RecipeEntry EMPTY = RecipeEntry.get(ItemStack.EMPTY, 0.0f);
}
