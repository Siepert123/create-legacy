package com.siepert.createlegacy.util.handlers;

import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.handlers.recipes.WashingRecipes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public class RecipeHandler {
    public static void registerOreSmelting() {
        CreateLegacy.logger.info("Registering Create's smelting recipes");
        GameRegistry.addSmelting(new ItemStack(ModBlocks.ORE, 1, 0),
                new ItemStack(ModItems.INGREDIENT, 1, 3), 0.2f);
        GameRegistry.addSmelting(new ItemStack(ModBlocks.ORE, 1, 1),
                new ItemStack(ModItems.INGREDIENT, 1, 6), 0.4f);
        GameRegistry.addSmelting(new ItemStack(ModItems.INGREDIENT, 1, 12),
                new ItemStack(Items.IRON_INGOT, 2, 0), 0.1f);
        GameRegistry.addSmelting(new ItemStack(ModItems.INGREDIENT, 1, 13),
                new ItemStack(Items.GOLD_INGOT, 2, 0), 0.1f);
        GameRegistry.addSmelting(new ItemStack(ModItems.INGREDIENT, 1, 14),
                new ItemStack(ModItems.INGREDIENT, 2, 3), 0.1f);
        GameRegistry.addSmelting(new ItemStack(ModItems.INGREDIENT, 1, 15),
                new ItemStack(ModItems.INGREDIENT, 2, 6), 0.1f);
    }

    public static void registerWashing() {
        CreateLegacy.logger.info("Registering compatible Washing recipes (highly WIP)");

        int compatWashFound = 0;
        if (OreDictionary.doesOreNameExist("nuggetAluminum")) {
            CreateLegacy.logger.info("ingotAluminum found, registering washing recipe");
            ItemStack stack0 = OreDictionary.getOres("nuggetAluminum").get(0);
            stack0.setCount(9);
            addWashing(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 0),
                    stack0);
            compatWashFound++;
        }
        if (OreDictionary.doesOreNameExist("nuggetLead")) {
            CreateLegacy.logger.info("ingotLead found, registering washing recipe");;
            ItemStack stack1 = OreDictionary.getOres("nuggetLead").get(0);
            stack1.setCount(9);
            addWashing(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 1),
                    stack1);
            compatWashFound++;
        }
        if (OreDictionary.doesOreNameExist("nuggetNickel")) {
            CreateLegacy.logger.info("ingotNickel found, registering washing recipe");;
            ItemStack stack2 = OreDictionary.getOres("nuggetNickel").get(0);
            stack2.setCount(9);
            addWashing(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 2),
                    stack2);
            compatWashFound++;
        }
        if (OreDictionary.doesOreNameExist("nuggetOsmium")) {
            CreateLegacy.logger.info("ingotOsmium found, registering washing recipe");;
            ItemStack stack3 = OreDictionary.getOres("nuggetOsmium").get(0);
            stack3.setCount(9);
            addWashing(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 3),
                    stack3);
            compatWashFound++;
        }
        if (OreDictionary.doesOreNameExist("nuggetPlatinum")) {
            CreateLegacy.logger.info("ingotPlatinum found, registering washing recipe");;
            ItemStack stack4 = OreDictionary.getOres("nuggetPlatinum").get(0);
            stack4.setCount(9);
            addWashing(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 4),
                    stack4);
            compatWashFound++;
        }
        if (OreDictionary.doesOreNameExist("nuggetQuicksilver")) {
            CreateLegacy.logger.info("ingotQuicksilver found, registering washing recipe");;
            ItemStack stack5 = OreDictionary.getOres("nuggetQuicksilver").get(0);
            stack5.setCount(9);
            addWashing(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 5),
                    stack5);
            compatWashFound++;
        }
        if (OreDictionary.doesOreNameExist("nuggetSilver")) {
            CreateLegacy.logger.info("ingotSilver found, registering washing recipe");;
            ItemStack stack6 = OreDictionary.getOres("nuggetSilver").get(0);
            stack6.setCount(9);
            addWashing(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 6),
                    stack6);
            compatWashFound++;
        }
        if (OreDictionary.doesOreNameExist("nuggetTin")) {
            CreateLegacy.logger.info("ingotTin found, registering washing recipe");;
            ItemStack stack7 = OreDictionary.getOres("nuggetTin").get(0);
            stack7.setCount(9);
            addWashing(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 7),
                    stack7);
            compatWashFound++;
        }
        if (OreDictionary.doesOreNameExist("nuggetUranium")) {
            CreateLegacy.logger.info("ingotUranium found, registering washing recipe");;
            ItemStack stack8 = OreDictionary.getOres("nuggetUranium").get(0);
            stack8.setCount(9);
            addWashing(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 8),
                    stack8);
            compatWashFound++;
        }
        CreateLegacy.logger.info("Found " + compatWashFound + " compatible washables total");
    }

    public static void registerCrushedOreCompatSmelting() { //Registers smelting crushed ore if the according ingot is real
        int compatSmeltsFound = 0;
        CreateLegacy.logger.info("Registering Create's compatible smeltables");
        if (OreDictionary.doesOreNameExist("ingotAluminum")) {
            CreateLegacy.logger.info("ingotAluminum found, registering smelting recipe");
            GameRegistry.addSmelting(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 0),
                    OreDictionary.getOres("ingotAluminum").get(0), 0.1f);
            compatSmeltsFound++;
        }
        if (OreDictionary.doesOreNameExist("ingotLead")) {
            CreateLegacy.logger.info("ingotLead found, registering smelting recipe");
            GameRegistry.addSmelting(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 1),
                    OreDictionary.getOres("ingotLead").get(0), 0.1f);
            compatSmeltsFound++;
        }
        if (OreDictionary.doesOreNameExist("ingotNickel")) {
            CreateLegacy.logger.info("ingotNickel found, registering smelting recipe");
            GameRegistry.addSmelting(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 2),
                    OreDictionary.getOres("ingotNickel").get(0), 0.1f);
            compatSmeltsFound++;
        }
        if (OreDictionary.doesOreNameExist("ingotOsmium")) {
            CreateLegacy.logger.info("ingotOsmium found, registering smelting recipe");
            GameRegistry.addSmelting(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 3),
                    OreDictionary.getOres("ingotOsmium").get(0), 0.1f);
            compatSmeltsFound++;
        }
        if (OreDictionary.doesOreNameExist("ingotPlatinum")) {
            CreateLegacy.logger.info("ingotPlatinum found, registering smelting recipe");
            GameRegistry.addSmelting(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 4),
                    OreDictionary.getOres("ingotPlatinum").get(0), 0.1f);
            compatSmeltsFound++;
        }
        if (OreDictionary.doesOreNameExist("ingotQuicksilver")) {
            CreateLegacy.logger.info("ingotQuicksilver found, registering smelting recipe");
            GameRegistry.addSmelting(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 5),
                    OreDictionary.getOres("ingotQuicksilver").get(0), 0.1f);
            compatSmeltsFound++;
        }
        if (OreDictionary.doesOreNameExist("ingotSilver")) {
            CreateLegacy.logger.info("ingotSilver found, registering smelting recipe");
            GameRegistry.addSmelting(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 6),
                    OreDictionary.getOres("ingotSilver").get(0), 0.1f);
            compatSmeltsFound++;
        }
        if (OreDictionary.doesOreNameExist("ingotTin")) {
            CreateLegacy.logger.info("ingotTin found, registering smelting recipe");
            GameRegistry.addSmelting(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 7),
                    OreDictionary.getOres("ingotTin").get(0), 0.1f);
            compatSmeltsFound++;
        }
        if (OreDictionary.doesOreNameExist("ingotUranium")) {
            CreateLegacy.logger.info("ingotUranium found, registering smelting recipe");
            GameRegistry.addSmelting(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 8),
                    OreDictionary.getOres("ingotUranium").get(0), 0.1f);
            compatSmeltsFound++;
        }
        CreateLegacy.logger.info("Found " + compatSmeltsFound + " compatible smeltables total");
    }


    private static void addWashing(@Nonnull ItemStack input, @Nonnull ItemStack result, ItemStack resultOptional) {
        WashingRecipes.instance().addWashingRecipe(input, result, resultOptional);
    }
    private static void addWashing(@Nonnull ItemStack input, @Nonnull ItemStack result) {
        WashingRecipes.instance().addWashingRecipe(input, result, ItemStack.EMPTY);
    }
}
