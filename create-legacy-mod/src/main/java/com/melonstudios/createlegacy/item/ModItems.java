package com.melonstudios.createlegacy.item;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.block.BlockBlazeBurner;
import com.melonstudios.createlegacy.block.BlockWindow;
import com.melonstudios.createlegacy.block.BlockWindowPane;
import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.block.kinetic.AbstractBlockKinetic;
import com.melonstudios.createlegacy.block.stone.AbstractBlockOrestone;
import com.melonstudios.createlegacy.copycat.BlockCopycat;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public final class ModItems {
    public static final List<Item> ITEMS = new ArrayList<>();

    public static final ItemWrench WRENCH = registerItem(new ItemWrench());
    public static final ItemGoggles GOGGLES = registerItem(new ItemGoggles());
    public static final ItemIngredient INGREDIENT = registerItem(new ItemIngredient());
    public static final ItemSandpaper SANDPAPER = registerItem(new ItemSandpaper());
    public static final ItemSchematic SCHEMATIC = registerItem(new ItemSchematic());
    public static final ItemScrumptious FOOD = registerItem(new ItemScrumptious());

    private static <T extends Item> T registerItem(T item) {
        ITEMS.add(item);
        return item;
    }

    public static void setItemModels() {
        CreateLegacy.setItemModel(WRENCH);
        CreateLegacy.setItemModel(GOGGLES, "goggles");

        ItemIngredient.setItemModels();
        AbstractBlockOrestone.setItemModels();
        ItemScrumptious.setItemModels();
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.FRAMED_GLASS),
                0, "framed_glass/normal");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.FRAMED_GLASS),
                1, "framed_glass/horizontal");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.FRAMED_GLASS),
                2, "framed_glass/vertical");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.FRAMED_GLASS),
                3, "framed_glass/tiled");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.FRAMED_GLASS_PANE),
                0, "framed_glass_pane/normal");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.FRAMED_GLASS_PANE),
                1, "framed_glass_pane/horizontal");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.FRAMED_GLASS_PANE),
                2, "framed_glass_pane/vertical");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.FRAMED_GLASS_PANE),
                3, "framed_glass_pane/tiled");
        BlockWindowPane.createItemModels();

        AbstractBlockKinetic.setItemModels();
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORE),
                0, "ore/copper");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORE),
                1, "ore/zinc");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.METAL),
                0, "metal/andesite_alloy");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.METAL),
                1, "metal/copper");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.METAL),
                2, "metal/zinc");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.METAL),
                3, "metal/brass");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.CASING),
                0, "casing/andesite");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.CASING),
                1, "casing/copper");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.CASING),
                2, "casing/brass");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.CASING),
                3, "casing/train");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.FUNNEL),
                0, "funnel/andesite");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.FUNNEL),
                1, "funnel/brass");

        CreateLegacy.setItemModel(ModBlocks.BELT_LEGACY);

        CreateLegacy.setItemModel(SANDPAPER);
        CreateLegacy.setItemModel(SCHEMATIC, 0,
                "schematic/empty");
        CreateLegacy.setItemModel(SCHEMATIC, 1,
                "schematic/writeable");
        CreateLegacy.setItemModel(SCHEMATIC, 2,
                "schematic/written");

        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.INDUSTRIAL_IRON));
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.INDUSTRIAL_IRON_GLASS));

        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.DEPOT));
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.CHUTE));

        BlockWindow.createItemModels();

        BlockCopycat.setItemModels();

        BlockBlazeBurner.items();
    }
}
