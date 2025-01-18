package com.melonstudios.createlegacy.tab;

import com.melonstudios.createlegacy.block.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public final class KineticsTab extends CreativeTabs {
    public KineticsTab() {
        super("create.kinetics");
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ModBlocks.ROTATOR, 1, 1);
    }
}
