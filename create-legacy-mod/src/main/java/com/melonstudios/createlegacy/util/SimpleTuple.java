package com.melonstudios.createlegacy.util;

import net.minecraft.item.ItemStack;
import com.melonstudios.createlegacy.recipe.WashingRecipes;

/**
 * Simple implementation of a tuple
 *
 * @author moddingforreal
 * @since 0.1.0
 * @param <X> First value
 * @param <Y> Second value
 */
public class SimpleTuple<X, Y> {
    private X value1;
    private Y value2;
    public X getValue1() {
        return value1;
    }
    public void setValue1(X value1) {
        this.value1 = value1;
    }
    public Y getValue2() {
        return value2;
    }
    public void setValue2(Y value2) {
        this.value2 = value2;
    }
    public SimpleTuple(X value1, Y value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    /**
     * @author Siepert123
     */
    public SimpleTuple<X, Y> copy() {
        return new SimpleTuple<>(value1, value2);
    }
    public static <X, Y> SimpleTuple<X, Y> from(X x, Y y) {
        return new SimpleTuple<>(x, y);
    }
    /**
     * Generates a simple tuple accepted by {@link WashingRecipes#addRecipe(ItemStack, SimpleTuple[])}.
     * @author Siepert123
     * @param result The result itemstack.
     * @param chance The chance of this item appearing (0.01f = 1%)
     * @return A tuple which can be passed into a new Washing Recipe.
     */
    public static SimpleTuple<ItemStack, Float> optionalRecipeEntry(ItemStack result, float chance) {
        return new SimpleTuple<>(result, chance);
    }
    /**
     * Generates a simple tuple accepted by {@link WashingRecipes#addRecipe(ItemStack, SimpleTuple[])}.
     * The {@code chance} is always 100%.
     * @author Siepert123
     * @param result The result itemstack.
     * @return A tuple which can be passed into a new Washing Recipe.
     */
    public static SimpleTuple<ItemStack, Float> optionalRecipeEntry(ItemStack result) {
        return optionalRecipeEntry(result, 1.0f);
    }
}
