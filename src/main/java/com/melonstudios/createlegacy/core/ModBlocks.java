package com.melonstudios.createlegacy.core;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

import java.util.ArrayList;
import java.util.List;

public final class ModBlocks {
    public static final List<Block> BLOCKS = new ArrayList<>();


    private static Block registerBlock(Block block) {
        BLOCKS.add(block);
        return block;
    }
    private static Block registerBlockWithItem(Block block, ItemBlock ib) {
        BLOCKS.add(block);
        ModItems.ITEMS.add(ib);
        return block;
    }
    private static Block registerBlockWithItem(Block block) {
        return registerBlockWithItem(block, (ItemBlock) new ItemBlock(block).setRegistryName(block.getRegistryName()));
    }

    public static void setTileEntities() {

    }
}
