package com.melonstudios.createlegacy.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Simulates ore dict for blocks.
 *
 * @author Siepert123
 */
public final class BlockTagHelper {
    private BlockTagHelper() {}

    /**
     * @param state The blockstate to get the item from.
     * @return The item as if you middle-clicked it.
     */
    public static ItemStack getItem(IBlockState state) {
        try {
            return state.getBlock().getPickBlock(state, null, null, null, null);  //highly corrosive
        } catch (NullPointerException e) {
            return new ItemStack(Item.getItemFromBlock(state.getBlock()),
                    1, state.getBlock().damageDropped(state));
        }
    }

    /**
     * @param state The Blockstate to check
     * @param tag The OreDict tag to check for
     * @return Whether the block contains said OreDict tag.
     */
    public static boolean hasTag(IBlockState state, String tag) {
        ItemStack stack = getItem(state);

        for (ItemStack tagged : OreDictionary.getOres(tag)) {
            if (tagged.isItemEqual(stack)) return true;
        }
        return false;
    }

    //Overloaded method
    public static boolean isWood(IBlockState state) {
        return hasTag(state, "logWood");
    }
    //Overloaded method
    public static boolean isLeaves(IBlockState state) {
        return hasTag(state, "treeLeaves");
    }
}
