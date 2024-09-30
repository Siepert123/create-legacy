package com.melonstudios.createapi.util;

import com.melonstudios.createapi.annotation.Incomplete;
import com.melonstudios.createapi.annotation.ReflectionConstant;
import net.minecraft.item.ItemStack;

/**
 * Simple implementation of a tuple
 *
 * @author moddingforreal
 * @since 0.1.0
 * @param <X> First value
 * @param <Y> Second value
 */
public class SimpleTuple<X, Y> {
    private X x;
    private Y y;
    public X getX() {
        return x;
    }
    public void setX(X x) {
        this.x = x;
    }
    public Y getY() {
        return y;
    }
    public void setY(Y y) {
        this.y = y;
    }
    public SimpleTuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @author Siepert123
     */
    public SimpleTuple<X, Y> copy() {
        return new SimpleTuple<>(x, y);
    }
    public static <X, Y> SimpleTuple<X, Y> from(X x, Y y) {
        return new SimpleTuple<>(x, y);
    }
    /**
     * Generates a simple tuple accepted by {@link com.melonstudios.createlegacy.recipe.WashingRecipes#addRecipe(ItemStack, SimpleTuple[])}.
     * @author Siepert123
     * @param result The result itemstack.
     * @param chance The chance of this item appearing (0.01f = 1%)
     * @return A tuple which can be passed into a new Washing Recipe.
     */
    public static SimpleTuple<ItemStack, Float> washingEntry(ItemStack result, float chance) {
        return new SimpleTuple<>(result, chance);
    }
    /**
     * Generates a simple tuple accepted by {@link com.melonstudios.createlegacy.recipe.WashingRecipes#addRecipe(ItemStack, SimpleTuple[])}.
     * The {@code chance} is always 100%.
     * @author Siepert123
     * @param result The result itemstack.
     * @return A tuple which can be passed into a new Washing Recipe.
     */
    public static SimpleTuple<ItemStack, Float> washingEntry(ItemStack result) {
        return washingEntry(result, 1.0f);
    }
}
