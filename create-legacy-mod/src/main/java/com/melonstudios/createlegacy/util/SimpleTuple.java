package com.melonstudios.createlegacy.util;

import net.minecraft.item.ItemStack;
import com.melonstudios.createlegacy.recipe.WashingRecipes;

/**
 * A simple tuple.
 * @param <X> Type of value 1.
 * @param <Y> Type of value 2.
 * @deprecated Use {@link com.melonstudios.createapi.util.SimpleTuple} instead
 */
@Deprecated
public class SimpleTuple<X, Y> {
    public SimpleTuple(X value1, Y value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public void setValue1(X value1) {
        this.value1 = value1;
    }
    private X value1;
    public X getValue1() {
        return value1;
    }
    public void setValue2(Y value2) {
        this.value2 = value2;
    }
    private Y value2;
    public Y getValue2() {
        return value2;
    }

    public SimpleTuple<X, Y> copy() {
        return new SimpleTuple<>(value1, value2);
    }

    /**
     * Generates a simple tuple accepted by {@link WashingRecipes#addRecipe(ItemStack, SimpleTuple[])}.
     * @param result The result itemstack.
     * @param chance The chance of this item appearing (0.01f = 1%)
     * @return A tuple which can be passed into a new Washing Recipe.
     */
    public static SimpleTuple<ItemStack, Float> washingEntry(ItemStack result, float chance) {
        return new SimpleTuple<>(result, chance);
    }
    /**
     * Generates a simple tuple accepted by {@link WashingRecipes#addRecipe(ItemStack, SimpleTuple[])}.
     * The {@code chance} is always 100%.
     * @param result The result itemstack.
     * @return A tuple which can be passed into a new Washing Recipe.
     */
    public static SimpleTuple<ItemStack, Float> washingEntry(ItemStack result) {
        return washingEntry(result, 1.0f);
    }
}
