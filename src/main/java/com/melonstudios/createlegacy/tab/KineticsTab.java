package com.melonstudios.createlegacy.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class KineticsTab extends CreativeTabs {
    public KineticsTab() {
        super("create.kinetics");
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(Items.STRING);
    }
}
