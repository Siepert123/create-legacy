package com.melonstudios.createlegacy.core;

import com.melonstudios.createlegacy.objects.block.stone.BlockOrestone;
import com.melonstudios.createlegacy.objects.item.ItemBlockVariants;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import java.util.ArrayList;
import java.util.List;

public final class ModBlocks {
    public static final List<Block> BLOCKS = new ArrayList<>();

    public static final Block STONE = registerBlockWithItem(new BlockOrestone("orestone"), true);

    private static Block registerBlock(Block block) {
        BLOCKS.add(block);
        return block;
    }
    private static Block registerBlockWithItem(Block block, Item ib) {
        BLOCKS.add(block);
        ModItems.ITEMS.add(ib);
        return block;
    }
    private static Block registerBlockWithItem(Block block, boolean variants) {
        if (variants) return registerBlockWithItem(block, new ItemBlockVariants(block).setRegistryName(block.getRegistryName()));
        return registerBlockWithItem(block, new ItemBlock(block).setRegistryName(block.getRegistryName()));
    }
    private static Block registerBlockWithItem(Block block) {
        return registerBlockWithItem(block, false);
    }

    public static void setTileEntities() {

    }
}
