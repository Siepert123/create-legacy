package com.melonstudios.createlegacy.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public final class BlockTagHelper {
    private BlockTagHelper() {}

    public static ItemStack getItem(IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(state.getBlock()),
                1, state.getBlock().damageDropped(state));
    }

    public static boolean hasTag(IBlockState state, String tag) {
        ItemStack stack = getItem(state);

        for (ItemStack tagged : OreDictionary.getOres(tag)) {
            if (tagged.isItemEqual(stack)) return true;
        }
        return false;
    }

    public static boolean isWood(IBlockState state) {
        return hasTag(state, "logWood");
    }
    public static boolean isLeaves(IBlockState state) {
        return hasTag(state, "treeLeaves");
    }
}
