package com.siepert.createlegacy.util.compat;

import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryCompat {
    public static void registerOres() {
        OreDictionary.registerOre("ingotChocolate", new ItemStack(ModItems.SCRUMPTIOUS_FOOD, 1, 2));

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
        OreDictionary.registerOre("gemRoseQuartzPolished", new ItemStack(ModItems.INGREDIENT, 1, 20));
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

        OreDictionary.registerOre("wheatFlour", new ItemStack(ModItems.SCRUMPTIOUS_FOOD, 1, 0));
        OreDictionary.registerOre("dough", new ItemStack(ModItems.SCRUMPTIOUS_FOOD, 1, 1));
    }
    /** Saves all stone types to the OreDict.
     * Useful for compat 'n stuff like that.*/
    public static void registerStoneTypes() {
        OreDictionary.registerOre("stoneAsurine", new ItemStack(ModBlocks.STONE, 1, 2));
        OreDictionary.registerOre("stoneCrimsite", new ItemStack(ModBlocks.STONE, 1, 3));
        OreDictionary.registerOre("stoneLimestone", new ItemStack(ModBlocks.STONE, 1, 4));
        OreDictionary.registerOre("stoneOchrum", new ItemStack(ModBlocks.STONE, 1, 5));
        OreDictionary.registerOre("stoneScorchia", new ItemStack(ModBlocks.STONE, 1, 6));
        OreDictionary.registerOre("stoneScoria", new ItemStack(ModBlocks.STONE, 1, 7));
        OreDictionary.registerOre("stoneVeridium", new ItemStack(ModBlocks.STONE, 1, 8));

        OreDictionary.registerOre("stoneCalcite", new ItemStack(ModBlocks.STONE, 1, 0));
        OreDictionary.registerOre("stoneTuff", new ItemStack(ModBlocks.STONE, 1, 1));


        OreDictionary.registerOre("stoneAsurinePolished", new ItemStack(ModBlocks.STONE_POLISHED, 1, 0));
        OreDictionary.registerOre("stoneCalcitePolished", new ItemStack(ModBlocks.STONE_POLISHED, 1, 1));
        OreDictionary.registerOre("stoneCrimsitePolished", new ItemStack(ModBlocks.STONE_POLISHED, 1, 2));
        OreDictionary.registerOre("stoneDeepslatePolished", new ItemStack(ModBlocks.STONE_POLISHED, 1, 3));
        OreDictionary.registerOre("stoneDripstonePolished", new ItemStack(ModBlocks.STONE_POLISHED, 1, 4));
        OreDictionary.registerOre("stoneLimestonePolished", new ItemStack(ModBlocks.STONE_POLISHED, 1, 5));
        OreDictionary.registerOre("stoneOchrumPolished", new ItemStack(ModBlocks.STONE_POLISHED, 1, 6));
        OreDictionary.registerOre("stoneScorchiaPolished", new ItemStack(ModBlocks.STONE_POLISHED, 1, 7));
        OreDictionary.registerOre("stoneScoriaPolished", new ItemStack(ModBlocks.STONE_POLISHED, 1, 8));
        OreDictionary.registerOre("stoneTuffPolished", new ItemStack(ModBlocks.STONE_POLISHED, 1, 9));
        OreDictionary.registerOre("stoneVeridiumPolished", new ItemStack(ModBlocks.STONE_POLISHED, 1, 10));
        OreDictionary.registerOre("stoneAndesitePolished", new ItemStack(ModBlocks.STONE_POLISHED, 1, 11));
        OreDictionary.registerOre("stoneDioritePolished", new ItemStack(ModBlocks.STONE_POLISHED, 1, 12));
        OreDictionary.registerOre("stoneGranitePolished", new ItemStack(ModBlocks.STONE_POLISHED, 1, 13));


        OreDictionary.registerOre("stoneAsurineCut", new ItemStack(ModBlocks.STONE_CUT, 1, 0));
        OreDictionary.registerOre("stoneCalciteCut", new ItemStack(ModBlocks.STONE_CUT, 1, 1));
        OreDictionary.registerOre("stoneCrimsiteCut", new ItemStack(ModBlocks.STONE_CUT, 1, 2));
        OreDictionary.registerOre("stoneDeepslateCut", new ItemStack(ModBlocks.STONE_CUT, 1, 3));
        OreDictionary.registerOre("stoneDripstoneCut", new ItemStack(ModBlocks.STONE_CUT, 1, 4));
        OreDictionary.registerOre("stoneLimestoneCut", new ItemStack(ModBlocks.STONE_CUT, 1, 5));
        OreDictionary.registerOre("stoneOchrumCut", new ItemStack(ModBlocks.STONE_CUT, 1, 6));
        OreDictionary.registerOre("stoneScorchiaCut", new ItemStack(ModBlocks.STONE_CUT, 1, 7));
        OreDictionary.registerOre("stoneScoriaCut", new ItemStack(ModBlocks.STONE_CUT, 1, 8));
        OreDictionary.registerOre("stoneTuffCut", new ItemStack(ModBlocks.STONE_CUT, 1, 9));
        OreDictionary.registerOre("stoneVeridiumCut", new ItemStack(ModBlocks.STONE_CUT, 1, 10));
        OreDictionary.registerOre("stoneAndesiteCut", new ItemStack(ModBlocks.STONE_CUT, 1, 11));
        OreDictionary.registerOre("stoneDioriteCut", new ItemStack(ModBlocks.STONE_CUT, 1, 12));
        OreDictionary.registerOre("stoneGraniteCut", new ItemStack(ModBlocks.STONE_CUT, 1, 13));
    }

    //Deco stones OreDict template.
    /*
    OreDictionary.registerOre("stoneAsurine", new ItemStack(ModBlocks.STONE, 1, 0));
    OreDictionary.registerOre("stoneCalcite", new ItemStack(ModBlocks.STONE, 1, 1));
    OreDictionary.registerOre("stoneCrimsite", new ItemStack(ModBlocks.STONE, 1, 2));
    OreDictionary.registerOre("stoneDeepslate", new ItemStack(ModBlocks.STONE, 1, 3));
    OreDictionary.registerOre("stoneDripstone", new ItemStack(ModBlocks.STONE, 1, 4));
    OreDictionary.registerOre("stoneLimestone", new ItemStack(ModBlocks.STONE, 1, 5));
    OreDictionary.registerOre("stoneOchrum", new ItemStack(ModBlocks.STONE, 1, 6));
    OreDictionary.registerOre("stoneScorchia", new ItemStack(ModBlocks.STONE, 1, 7));
    OreDictionary.registerOre("stoneScoria", new ItemStack(ModBlocks.STONE, 1, 8));
    OreDictionary.registerOre("stoneTuff", new ItemStack(ModBlocks.STONE, 1, 9));
    OreDictionary.registerOre("stoneVeridium", new ItemStack(ModBlocks.STONE, 1, 10));
    OreDictionary.registerOre("stoneAndesite", new ItemStack(ModBlocks.STONE, 1, 11));
    OreDictionary.registerOre("stoneDiorite", new ItemStack(ModBlocks.STONE, 1, 12));
    OreDictionary.registerOre("stoneGranite", new ItemStack(ModBlocks.STONE, 1, 13));
     */
}
