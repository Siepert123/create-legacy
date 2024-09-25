package com.melonstudios.createlegacy.item;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.block.ModBlocks;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public final class ModItems {
    public static final List<Item> ITEMS = new ArrayList<>();

    public static final Item INGREDIENT = registerItem(new ItemIngredient());
    public static final Item SANDPAPER = registerItem(new ItemSandpaper());

    private static Item registerItem(Item item) {
        ITEMS.add(item);
        return item;
    }

    public static void setItemModels() {
        ItemIngredient.setItemModels();
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.STONE),
                0, "orestone/stone_asurine");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.STONE),
                1, "orestone/stone_crimsite");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.STONE),
                2, "orestone/stone_limestone");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.STONE),
                3, "orestone/stone_ochrum");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.STONE),
                4, "orestone/stone_scorchia");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.STONE),
                5, "orestone/stone_scoria");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.STONE),
                6, "orestone/stone_veridium");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.METAL),
                0, "metal/copper");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.METAL),
                1, "metal/zinc");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.METAL),
                2, "metal/brass");
        CreateLegacy.setItemModel(SANDPAPER);
    }
}
