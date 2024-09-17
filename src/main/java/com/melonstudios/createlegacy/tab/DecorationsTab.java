package com.melonstudios.createlegacy.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class DecorationsTab extends CreativeTabs {
    public DecorationsTab() {
        super("create.decorations");
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(Items.COAL);
    }
}
