package com.siepert.createlegacy.tabs;

import com.siepert.createlegacy.mainRegistry.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreateModTab extends CreativeTabs {
    public CreateModTab(String label) {
        super("tab_create");
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(Item.getItemFromBlock(ModBlocks.COGWHEEL));
    }
}
