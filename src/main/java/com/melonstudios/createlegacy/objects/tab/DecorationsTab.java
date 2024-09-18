package com.melonstudios.createlegacy.objects.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public final class DecorationsTab extends CreativeTabs {
    public DecorationsTab() {
        super("create.decorations");
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(Items.COAL);
    }
}
