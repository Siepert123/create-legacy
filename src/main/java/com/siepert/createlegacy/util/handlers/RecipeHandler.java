package com.siepert.createlegacy.util.handlers;

import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;

public class RecipeHandler {
    public static void registerOreSmelting(Logger logger) {
        logger.info("Registering Create's smelting recipes");
        GameRegistry.addSmelting(new ItemStack(ModBlocks.ORE, 1, 0),
                new ItemStack(ModItems.INGREDIENT, 1, 3), 0.2f);
        GameRegistry.addSmelting(new ItemStack(ModBlocks.ORE, 1, 1),
                new ItemStack(ModItems.INGREDIENT, 1, 6), 0.4f);
        GameRegistry.addSmelting(new ItemStack(ModItems.INGREDIENT, 1, 12),
                new ItemStack(Items.IRON_INGOT, 1, 0), 0.1f);
        GameRegistry.addSmelting(new ItemStack(ModItems.INGREDIENT, 1, 13),
                new ItemStack(Items.GOLD_INGOT, 1, 0), 0.1f);
        GameRegistry.addSmelting(new ItemStack(ModItems.INGREDIENT, 1, 14),
                new ItemStack(ModItems.INGREDIENT, 1, 3), 0.1f);
        GameRegistry.addSmelting(new ItemStack(ModItems.INGREDIENT, 1, 15),
                new ItemStack(ModItems.INGREDIENT, 1, 6), 0.1f);
    }

    public static void registerCrushedOreCompatSmelting(Logger logger) { //TODO: Test if this works (using IE)
        int compatSmeltsFound = 0;
        logger.info("Registering Create's compatible smeltables");
        if (OreDictionary.doesOreNameExist("ingotAluminum")) {
            logger.info("ingotAluminum found, registering smelting recipe");
            GameRegistry.addSmelting(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 0),
                    OreDictionary.getOres("ingotAluminum").get(0), 0.1f);
            compatSmeltsFound++;
        }
        if (OreDictionary.doesOreNameExist("ingotLead")) {
            logger.info("ingotLead found, registering smelting recipe");
            GameRegistry.addSmelting(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 1),
                    OreDictionary.getOres("ingotLead").get(0), 0.1f);
            compatSmeltsFound++;
        }
        if (OreDictionary.doesOreNameExist("ingotNickel")) {
            logger.info("ingotNickel found, registering smelting recipe");
            GameRegistry.addSmelting(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 2),
                    OreDictionary.getOres("ingotNickel").get(0), 0.1f);
            compatSmeltsFound++;
        }
        if (OreDictionary.doesOreNameExist("ingotOsmium")) {
            logger.info("ingotOsmium found, registering smelting recipe");
            GameRegistry.addSmelting(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 3),
                    OreDictionary.getOres("ingotOsmium").get(0), 0.1f);
            compatSmeltsFound++;
        }
        if (OreDictionary.doesOreNameExist("ingotPlatinum")) {
            logger.info("ingotPlatinum found, registering smelting recipe");
            GameRegistry.addSmelting(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 4),
                    OreDictionary.getOres("ingotPlatinum").get(0), 0.1f);
            compatSmeltsFound++;
        }
        if (OreDictionary.doesOreNameExist("ingotQuicksilver")) {
            logger.info("ingotQuicksilver found, registering smelting recipe");
            GameRegistry.addSmelting(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 5),
                    OreDictionary.getOres("ingotQuicksilver").get(0), 0.1f);
            compatSmeltsFound++;
        }
        if (OreDictionary.doesOreNameExist("ingotSilver")) {
            logger.info("ingotSilver found, registering smelting recipe");
            GameRegistry.addSmelting(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 6),
                    OreDictionary.getOres("ingotSilver").get(0), 0.1f);
            compatSmeltsFound++;
        }
        if (OreDictionary.doesOreNameExist("ingotTin")) {
            logger.info("ingotTin found, registering smelting recipe");
            GameRegistry.addSmelting(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 7),
                    OreDictionary.getOres("ingotTin").get(0), 0.1f);
            compatSmeltsFound++;
        }
        if (OreDictionary.doesOreNameExist("ingotUranium")) {
            logger.info("ingotUranium found, registering smelting recipe");
            GameRegistry.addSmelting(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 8),
                    OreDictionary.getOres("ingotUranium").get(0), 0.1f);
            compatSmeltsFound++;
        }
        logger.info("Found " + compatSmeltsFound + " compatible smeltables total");
    }


}
