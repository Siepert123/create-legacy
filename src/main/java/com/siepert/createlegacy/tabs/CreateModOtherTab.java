package com.siepert.createlegacy.tabs;

import com.siepert.createlegacy.mainRegistry.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Calendar;
import java.util.Date;

public class CreateModOtherTab extends CreativeTabs {
    public CreateModOtherTab() {
        super("tab_create_other");
    }

    @Override
    public ItemStack getTabIconItem() {
        Date date = new Date();
        if (date.getMonth() == Calendar.APRIL && date.getDate() == 1) {
            return new ItemStack(Item.getItemFromBlock(ModBlocks.STONE_REINFORCED), 69, 10);
        }
        return new ItemStack(Item.getItemFromBlock(ModBlocks.STONE_REINFORCED), 1, 10);
    }
}
