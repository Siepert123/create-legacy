package com.melonstudios.createlegacy.util;

import net.minecraft.item.ItemStack;

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

    public static SimpleTuple<ItemStack, Float> washingEntry(ItemStack result, float chance) {
        return new SimpleTuple<>(result, chance);
    }
    public static SimpleTuple<ItemStack, Float> washingEntry(ItemStack result) {
        return washingEntry(result, 1.0f);
    }
}
