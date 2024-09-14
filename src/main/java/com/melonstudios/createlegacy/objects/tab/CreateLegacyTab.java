package com.melonstudios.createlegacy.objects.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreateLegacyTab extends CreativeTabs {
    public CreateLegacyTab(String label, ItemStack icon) {
        super(label);
        this.icon = icon;
    }

    private final ItemStack icon;

    @Override
    public ItemStack getTabIconItem() {
        return icon;
    }
}
