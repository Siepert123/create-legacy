package com.melonstudios.createlegacy.util;

import com.melonstudios.createlegacy.core.ModItems;
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

        initialized = true;
    }
}
