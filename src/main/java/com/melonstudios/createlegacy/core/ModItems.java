package com.melonstudios.createlegacy.core;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.objects.item.ItemIngredient;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public final class ModItems {
    public static final List<Item> ITEMS = new ArrayList<>();

    public static final Item INGREDIENT = registerItem(new ItemIngredient());

    private static Item registerItem(Item item) {
        ITEMS.add(item);
        return item;
    }

    public static void setItemModels() {
        ItemIngredient.setItemModels();
        CreateLegacy.proxy.setItemModel(Item.getItemFromBlock(ModBlocks.STONE),
                0, "orestone/stone_asurine");
        CreateLegacy.proxy.setItemModel(Item.getItemFromBlock(ModBlocks.STONE),
                1, "orestone/stone_crimsite");
        CreateLegacy.proxy.setItemModel(Item.getItemFromBlock(ModBlocks.STONE),
                2, "orestone/stone_limestone");
        CreateLegacy.proxy.setItemModel(Item.getItemFromBlock(ModBlocks.STONE),
                3, "orestone/stone_ochrum");
        CreateLegacy.proxy.setItemModel(Item.getItemFromBlock(ModBlocks.STONE),
                4, "orestone/stone_scorchia");
        CreateLegacy.proxy.setItemModel(Item.getItemFromBlock(ModBlocks.STONE),
                5, "orestone/stone_scoria");
        CreateLegacy.proxy.setItemModel(Item.getItemFromBlock(ModBlocks.STONE),
                6, "orestone/stone_veridium");
    }
}
