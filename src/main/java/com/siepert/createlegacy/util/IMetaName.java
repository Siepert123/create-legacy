package com.siepert.createlegacy.util;

import net.minecraft.item.ItemStack;

/**Allows different names to be applied to subtypes of a block or item. */
public interface IMetaName {
    public String getSpecialName(ItemStack stack);
}
