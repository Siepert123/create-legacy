package com.melonstudios.createlegacy.core;

import com.melonstudios.createlegacy.CreateLegacy;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public final class ModItems {
    public static final List<Item> ITEMS = new ArrayList<>();


    private static Item registerItem(Item item) {
        ITEMS.add(item);
        return item;
    }

    public static void setItemModels() {
        CreateLegacy.proxy.setItemModel(Item.getItemFromBlock(ModBlocks.STONE),
                0, "stone/stone_asurine");
        CreateLegacy.proxy.setItemModel(Item.getItemFromBlock(ModBlocks.STONE),
                1, "stone/stone_crimsite");
        CreateLegacy.proxy.setItemModel(Item.getItemFromBlock(ModBlocks.STONE),
                2, "stone/stone_limestone");
        CreateLegacy.proxy.setItemModel(Item.getItemFromBlock(ModBlocks.STONE),
                3, "stone/stone_ochrum");
        CreateLegacy.proxy.setItemModel(Item.getItemFromBlock(ModBlocks.STONE),
                4, "stone/stone_scorchia");
        CreateLegacy.proxy.setItemModel(Item.getItemFromBlock(ModBlocks.STONE),
                5, "stone/stone_scoria");
        CreateLegacy.proxy.setItemModel(Item.getItemFromBlock(ModBlocks.STONE),
                6, "stone/stone_veridium");
    }
}
