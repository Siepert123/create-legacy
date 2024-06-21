package com.siepert.createlegacy.util.handlers;

import net.minecraft.item.ItemStack;

public class StackSet {
    private final ItemStack stack1, stack2;

    public StackSet(ItemStack stack1, ItemStack stack2) {
        this.stack1 = stack1;
        this.stack2 = stack2;
    }

    public ItemStack getFirstStack() {
        return stack1;
    }
    public ItemStack getSecondStack() {
        return stack2;
    }
}
