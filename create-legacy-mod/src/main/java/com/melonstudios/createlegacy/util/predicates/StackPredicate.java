package com.melonstudios.createlegacy.util.predicates;

import net.minecraft.item.ItemStack;

public abstract class StackPredicate {
    public abstract boolean matches(ItemStack stack);
}
