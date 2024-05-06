package com.siepert.createlegacy.util.compat;

import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryCompat {
    public static void registerOres() {
        OreDictionary.registerOre("oreCopper", new ItemStack(Item.getItemFromBlock(ModBlocks.ORE), 1, 0));
        OreDictionary.registerOre("oreZinc", new ItemStack(Item.getItemFromBlock(ModBlocks.ORE), 1, 1));

        OreDictionary.registerOre("blockCopper", new ItemStack(ModBlocks.MATERIAL_STORAGE_BLOCK, 1, 0));
        OreDictionary.registerOre("ingotCopper", new ItemStack(ModItems.INGREDIENT, 1, 3));
        OreDictionary.registerOre("nuggetCopper", new ItemStack(ModItems.INGREDIENT, 1, 4));
        OreDictionary.registerOre("plateCopper", new ItemStack(ModItems.INGREDIENT, 1, 5));

        OreDictionary.registerOre("blockZinc", new ItemStack(ModBlocks.MATERIAL_STORAGE_BLOCK, 1, 1));
        OreDictionary.registerOre("ingotZinc", new ItemStack(ModItems.INGREDIENT, 1, 6));
        OreDictionary.registerOre("nuggetZinc", new ItemStack(ModItems.INGREDIENT, 1, 7));
        OreDictionary.registerOre("plateZinc", new ItemStack(ModItems.INGREDIENT, 1, 8));

        OreDictionary.registerOre("blockBrass", new ItemStack(ModBlocks.MATERIAL_STORAGE_BLOCK, 1, 2));
        OreDictionary.registerOre("ingotBrass", new ItemStack(ModItems.INGREDIENT, 1, 9));
        OreDictionary.registerOre("nuggetBrass", new ItemStack(ModItems.INGREDIENT, 1, 10));
        OreDictionary.registerOre("plateBrass", new ItemStack(ModItems.INGREDIENT, 1, 11));

        OreDictionary.registerOre("plateIron", new ItemStack(ModItems.INGREDIENT, 1, 1));
        OreDictionary.registerOre("plateGold", new ItemStack(ModItems.INGREDIENT, 1, 2));
        OreDictionary.registerOre("plateObsidian", new ItemStack(ModItems.INGREDIENT, 1, 18));

        OreDictionary.registerOre("dustNetherrack", new ItemStack(ModItems.INGREDIENT, 1, 16));
        OreDictionary.registerOre("dustObsidian", new ItemStack(ModItems.INGREDIENT, 1, 17));

        OreDictionary.registerOre("gemRoseQuartz", new ItemStack(ModItems.INGREDIENT, 1, 19));
        OreDictionary.registerOre("gemPolishedRoseQuartz", new ItemStack(ModItems.INGREDIENT, 1, 20));
        OreDictionary.registerOre("electronTube", new ItemStack(ModItems.ADVANCED_INGREDIENT, 1, 2));

        OreDictionary.registerOre("crushedIron", new ItemStack(ModItems.INGREDIENT, 1, 12));
        OreDictionary.registerOre("crushedGold", new ItemStack(ModItems.INGREDIENT, 1, 13));
        OreDictionary.registerOre("crushedCopper", new ItemStack(ModItems.INGREDIENT, 1, 14));
        OreDictionary.registerOre("crushedZinc", new ItemStack(ModItems.INGREDIENT, 1, 15));

        OreDictionary.registerOre("crushedAluminum", new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 0));
        OreDictionary.registerOre("crushedLead", new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 1));
        OreDictionary.registerOre("crushedNickel", new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 2));
        OreDictionary.registerOre("crushedOsmium", new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 3));
        OreDictionary.registerOre("crushedPlatinum", new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 4));
        OreDictionary.registerOre("crushedQuicksilver", new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 5));
        OreDictionary.registerOre("crushedSilver", new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 6));
        OreDictionary.registerOre("crushedTin", new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 7));
        OreDictionary.registerOre("crushedUranium", new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 8));
    }
}
