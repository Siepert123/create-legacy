package com.melonstudios.createlegacy.util;

import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.block.stone.AbstractBlockOrestone;
import com.melonstudios.createlegacy.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Adds items to the Ore Dictionary.
 * @since 0.1.0
 */
public final class OreDictHandler {
    private static boolean initialized = false;
    public static void init() {
        if (initialized) return;
        long startTime = System.currentTimeMillis();
        DisplayLink.debug("Starting Ore Dictionary init");

        OreDictionary.registerOre("ingotCopper", new ItemStack(ModItems.INGREDIENT, 1, 3));
        OreDictionary.registerOre("ingotZinc", new ItemStack(ModItems.INGREDIENT, 1, 6));
        OreDictionary.registerOre("ingotBrass", new ItemStack(ModItems.INGREDIENT, 1, 9));

        OreDictionary.registerOre("nuggetCopper", new ItemStack(ModItems.INGREDIENT, 1, 4));
        OreDictionary.registerOre("nuggetZinc", new ItemStack(ModItems.INGREDIENT, 1, 7));
        OreDictionary.registerOre("nuggetBrass", new ItemStack(ModItems.INGREDIENT, 1, 10));

        OreDictionary.registerOre("plateIron", new ItemStack(ModItems.INGREDIENT, 1, 1));
        OreDictionary.registerOre("plateGold", new ItemStack(ModItems.INGREDIENT, 1, 2));
        OreDictionary.registerOre("plateCopper", new ItemStack(ModItems.INGREDIENT, 1, 5));
        OreDictionary.registerOre("plateZinc", new ItemStack(ModItems.INGREDIENT, 1, 8));
        OreDictionary.registerOre("plateBrass", new ItemStack(ModItems.INGREDIENT, 1, 11));
        OreDictionary.registerOre("plateObsidian", new ItemStack(ModItems.INGREDIENT, 1, 18));

        OreDictionary.registerOre("blockCopper", new ItemStack(ModBlocks.METAL, 1, 0));
        OreDictionary.registerOre("blockZinc", new ItemStack(ModBlocks.METAL, 1, 1));
        OreDictionary.registerOre("blockBrass", new ItemStack(ModBlocks.METAL, 1, 2));

        OreDictionary.registerOre("crushedIron", new ItemStack(ModItems.INGREDIENT, 1, 12));
        OreDictionary.registerOre("crushedGold", new ItemStack(ModItems.INGREDIENT, 1, 13));
        OreDictionary.registerOre("crushedCopper", new ItemStack(ModItems.INGREDIENT, 1, 14));
        OreDictionary.registerOre("crushedZinc", new ItemStack(ModItems.INGREDIENT, 1, 15));

        OreDictionary.registerOre("dustNetherrack", new ItemStack(ModItems.INGREDIENT, 1, 16));
        OreDictionary.registerOre("dustObsidian", new ItemStack(ModItems.INGREDIENT, 1, 17));

        OreDictionary.registerOre("gemRoseQuartz", new ItemStack(ModItems.INGREDIENT, 1, 19));
        OreDictionary.registerOre("gemRoseQuartzPolished", new ItemStack(ModItems.INGREDIENT, 1, 20));

        OreDictionary.registerOre("propeller", new ItemStack(ModItems.INGREDIENT, 1, 23));
        OreDictionary.registerOre("whisk", new ItemStack(ModItems.INGREDIENT, 1, 24));
        OreDictionary.registerOre("electronTube", new ItemStack(ModItems.INGREDIENT, 1, 25));
        OreDictionary.registerOre("handBrass", new ItemStack(ModItems.INGREDIENT, 1, 26));

        addOrestoneTags(ModBlocks.ORESTONE);
        addOrestoneTags(ModBlocks.ORESTONE_POLISHED);
        addOrestoneTags(ModBlocks.ORESTONE_BRICKS);
        addOrestoneTags(ModBlocks.ORESTONE_BRICKS_FANCY);

        OreDictionary.registerOre("create:unprocessedItem", new ItemStack(ModItems.INGREDIENT, 1, 28));
        OreDictionary.registerOre("create:unprocessedItem", new ItemStack(ModItems.INGREDIENT, 1, 29));

        DisplayLink.debug("Ore Dictionary init done in %s ms!", System.currentTimeMillis() - startTime);
        initialized = true;
    }

    private static void addOrestoneTags(AbstractBlockOrestone orestone) {
        OreDictionary.registerOre("stoneAsurine", new ItemStack(orestone, 1, 0));
        OreDictionary.registerOre("stoneCrimsite", new ItemStack(orestone, 1, 1));
        OreDictionary.registerOre("stoneLimestone", new ItemStack(orestone, 1, 2));
        OreDictionary.registerOre("stoneOchrum", new ItemStack(orestone, 1, 3));
        OreDictionary.registerOre("stoneScorchia", new ItemStack(orestone, 1, 4));
        OreDictionary.registerOre("stoneScoria", new ItemStack(orestone, 1, 5));
        OreDictionary.registerOre("stoneVeridium", new ItemStack(orestone, 1, 6));
    }
}
