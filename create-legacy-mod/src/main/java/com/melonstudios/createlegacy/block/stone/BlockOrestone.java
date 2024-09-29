package com.melonstudios.createlegacy.block.stone;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public final class BlockOrestone extends AbstractBlockOrestone {
    public BlockOrestone() {
        super("orestone");
    }

    @Override
    protected String getOrestonePrefix() {
        return "orestone";
    }
}
