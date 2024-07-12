package com.siepert.createlegacy.tabs;

import com.siepert.createlegacy.mainRegistry.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreateModOtherTab extends CreativeTabs {
    public CreateModOtherTab() {
        super("tab_create_other");
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(Item.getItemFromBlock(ModBlocks.STONE_REINFORCED), 2, 10);
    }
}
