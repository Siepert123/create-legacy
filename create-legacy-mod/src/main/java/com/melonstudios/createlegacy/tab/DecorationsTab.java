package com.melonstudios.createlegacy.tab;

import com.melonstudios.createlegacy.block.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public final class DecorationsTab extends CreativeTabs {
    public DecorationsTab() {
        super("create.decorations");
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ModBlocks.WINDOW_IRON);
    }
}
