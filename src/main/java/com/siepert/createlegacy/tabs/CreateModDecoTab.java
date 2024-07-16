package com.siepert.createlegacy.tabs;

import com.siepert.createlegacy.mainRegistry.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import java.util.Calendar;
import java.util.Date;

public class CreateModDecoTab extends CreativeTabs {
    public CreateModDecoTab(String label) {
        super("tab_create_decorations");
    }

    @Override
    public ItemStack getTabIconItem() {
        Date date = new Date();
        if (date.getMonth() == Calendar.APRIL && date.getDate() == 1) {
            return new ItemStack(ModBlocks.STONE_BRICKS, 69, 10);
        }
        return new ItemStack(ModBlocks.STONE_BRICKS, 1, 10);
    }
}
