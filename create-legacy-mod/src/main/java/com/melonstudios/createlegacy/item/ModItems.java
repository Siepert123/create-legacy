package com.melonstudios.createlegacy.item;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.block.stone.AbstractBlockOrestone;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public final class ModItems {
    public static final List<Item> ITEMS = new ArrayList<>();

    public static final Item INGREDIENT = registerItem(new ItemIngredient());
    public static final Item SANDPAPER = registerItem(new ItemSandpaper());
    public static final Item SCHEMATIC = registerItem(new ItemSchematic());

    private static Item registerItem(Item item) {
        ITEMS.add(item);
        return item;
    }

    public static void setItemModels() {
        ItemIngredient.setItemModels();
        AbstractBlockOrestone.setItemModels();
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORE),
                0, "ore/copper");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORE),
                1, "ore/zinc");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.METAL),
                0, "metal/copper");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.METAL),
                1, "metal/zinc");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.METAL),
                2, "metal/brass");
        CreateLegacy.setItemModel(SANDPAPER);
        CreateLegacy.setItemModel(SCHEMATIC, 0,
                "schematic/empty");
        CreateLegacy.setItemModel(SCHEMATIC, 1,
                "schematic/writeable");
        CreateLegacy.setItemModel(SCHEMATIC, 2,
                "schematic/written");
    }
}
