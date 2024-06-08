package com.siepert.createlegacy.tabs;

import com.siepert.createlegacy.mainRegistry.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreateModDecoTab extends CreativeTabs {
    public CreateModDecoTab(String label) {
        super("tab_create_decorations");
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ModBlocks.STONE_BRICKS, 1, 10);
    }
}
