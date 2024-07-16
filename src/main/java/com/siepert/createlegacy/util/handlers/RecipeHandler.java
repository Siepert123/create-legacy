package com.siepert.createlegacy.util.handlers;

import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.blocks.kinetic.BlockBlazeBurner;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.compat.MetalTypes;
import com.siepert.createlegacy.util.compat.OreDictionaryCompat;
import com.siepert.createlegacy.util.handlers.recipes.CompactingRecipes;
import com.siepert.createlegacy.util.handlers.recipes.MillingRecipes;
import com.siepert.createlegacy.util.handlers.recipes.PressingRecipes;
import com.siepert.createlegacy.util.handlers.recipes.WashingRecipes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;

public class RecipeHandler {
    public static void registerOreSmelting() {
        CreateLegacy.logger.info("Registering Create's smelting recipes");
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
    
    public static void registerOther() {
        GameRegistry.addSmelting(new ItemStack(ModItems.SCRUMPTIOUS_FOOD, 1, 1),
                new ItemStack(Items.BREAD), 0.1f);
    }

    public static void registerWashing() {
        for (int i = 0; i < OreDictionary.getOres("wheatFlour").size(); i++) {
            addWashing(OreDictionary.getOres("wheatFlour").get(i),
                    new ItemStack(ModItems.SCRUMPTIOUS_FOOD, 1, 1));
        }


        CreateLegacy.logger.info("Registering compatible Washing recipes");

        int compatWashFound = 0;
        if (OreDictionaryCompat.doesOreDictReallyExist("nuggetAluminum")) {
            CreateLegacy.logger.info("ingotAluminum found, registering washing recipe");
            ItemStack stack0 = OreDictionary.getOres("nuggetAluminum").get(0).copy();
            stack0.setCount(9);
            addWashing(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 0),
                    stack0);
            compatWashFound++;
        }
        if (OreDictionaryCompat.doesOreDictReallyExist("nuggetLead")) {
            CreateLegacy.logger.info("ingotLead found, registering washing recipe");
            ItemStack stack1 = OreDictionary.getOres("nuggetLead").get(0).copy();
            stack1.setCount(9);
            addWashing(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 1),
                    stack1);
            compatWashFound++;
        }
        if (OreDictionaryCompat.doesOreDictReallyExist("nuggetNickel")) {
            CreateLegacy.logger.info("ingotNickel found, registering washing recipe");
            ItemStack stack2 = OreDictionary.getOres("nuggetNickel").get(0).copy();
            stack2.setCount(9);
            addWashing(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 2),
                    stack2);
            compatWashFound++;
        }
        if (OreDictionaryCompat.doesOreDictReallyExist("nuggetOsmium")) {
            CreateLegacy.logger.info("ingotOsmium found, registering washing recipe");
            ItemStack stack3 = OreDictionary.getOres("nuggetOsmium").get(0).copy();
            stack3.setCount(9);
            addWashing(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 3),
                    stack3);
            compatWashFound++;
        }
        if (OreDictionaryCompat.doesOreDictReallyExist("nuggetPlatinum")) {
            CreateLegacy.logger.info("ingotPlatinum found, registering washing recipe");
            ItemStack stack4 = OreDictionary.getOres("nuggetPlatinum").get(0).copy();
            stack4.setCount(9);
            addWashing(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 4),
                    stack4);
            compatWashFound++;
        }
        if (OreDictionaryCompat.doesOreDictReallyExist("nuggetQuicksilver")) {
            CreateLegacy.logger.info("ingotQuicksilver found, registering washing recipe");
            ItemStack stack5 = OreDictionary.getOres("nuggetQuicksilver").get(0).copy();
            stack5.setCount(9);
            addWashing(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 5),
                    stack5);
            compatWashFound++;
        }
        if (OreDictionaryCompat.doesOreDictReallyExist("nuggetSilver")) {
            CreateLegacy.logger.info("ingotSilver found, registering washing recipe");
            ItemStack stack6 = OreDictionary.getOres("nuggetSilver").get(0).copy();
            stack6.setCount(9);
            addWashing(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 6),
                    stack6);
            compatWashFound++;
        }
        if (OreDictionaryCompat.doesOreDictReallyExist("nuggetTin")) {
            CreateLegacy.logger.info("ingotTin found, registering washing recipe");
            ItemStack stack7 = OreDictionary.getOres("nuggetTin").get(0).copy();
            stack7.setCount(9);
            addWashing(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 7),
                    stack7);
            compatWashFound++;
        }
        if (OreDictionaryCompat.doesOreDictReallyExist("nuggetUranium")) {
            CreateLegacy.logger.info("ingotUranium found, registering washing recipe");
            ItemStack stack8 = OreDictionary.getOres("nuggetUranium").get(0).copy();
            stack8.setCount(9);
            addWashing(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 8),
                    stack8);
            compatWashFound++;
        }
        CreateLegacy.logger.info("Found {} compatible washables total", compatWashFound);
    }

    public static void registerCrushedOreCompatSmelting() { //Registers smelting crushed ore if the according ingot is real
        int compatSmeltsFound = 0;
        CreateLegacy.logger.info("Registering Create's compatible smeltables");
        if (OreDictionaryCompat.doesOreDictReallyExist("ingotAluminum")) {
            CreateLegacy.logger.info("ingotAluminum found, registering smelting recipe");
            GameRegistry.addSmelting(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 0),
                    OreDictionary.getOres("ingotAluminum").get(0), 0.1f);
            compatSmeltsFound++;
        }
        if (OreDictionaryCompat.doesOreDictReallyExist("ingotLead")) {
            CreateLegacy.logger.info("ingotLead found, registering smelting recipe");
            GameRegistry.addSmelting(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 1),
                    OreDictionary.getOres("ingotLead").get(0), 0.1f);
            compatSmeltsFound++;
        }
        if (OreDictionaryCompat.doesOreDictReallyExist("ingotNickel")) {
            CreateLegacy.logger.info("ingotNickel found, registering smelting recipe");
            GameRegistry.addSmelting(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 2),
                    OreDictionary.getOres("ingotNickel").get(0), 0.1f);
            compatSmeltsFound++;
        }
        if (OreDictionaryCompat.doesOreDictReallyExist("ingotOsmium")) {
            CreateLegacy.logger.info("ingotOsmium found, registering smelting recipe");
            GameRegistry.addSmelting(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 3),
                    OreDictionary.getOres("ingotOsmium").get(0), 0.1f);
            compatSmeltsFound++;
        }
        if (OreDictionaryCompat.doesOreDictReallyExist("ingotPlatinum")) {
            CreateLegacy.logger.info("ingotPlatinum found, registering smelting recipe");
            GameRegistry.addSmelting(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 4),
                    OreDictionary.getOres("ingotPlatinum").get(0), 0.1f);
            compatSmeltsFound++;
        }
        if (OreDictionaryCompat.doesOreDictReallyExist("ingotQuicksilver")) {
            CreateLegacy.logger.info("ingotQuicksilver found, registering smelting recipe");
            GameRegistry.addSmelting(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 5),
                    OreDictionary.getOres("ingotQuicksilver").get(0), 0.1f);
            compatSmeltsFound++;
        }
        if (OreDictionaryCompat.doesOreDictReallyExist("ingotSilver")) {
            CreateLegacy.logger.info("ingotSilver found, registering smelting recipe");
            GameRegistry.addSmelting(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 6),
                    OreDictionary.getOres("ingotSilver").get(0), 0.1f);
            compatSmeltsFound++;
        }
        if (OreDictionaryCompat.doesOreDictReallyExist("ingotTin")) {
            CreateLegacy.logger.info("ingotTin found, registering smelting recipe");
            GameRegistry.addSmelting(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 7),
                    OreDictionary.getOres("ingotTin").get(0), 0.1f);
            compatSmeltsFound++;
        }
        if (OreDictionaryCompat.doesOreDictReallyExist("ingotUranium")) {
            CreateLegacy.logger.info("ingotUranium found, registering smelting recipe");
            GameRegistry.addSmelting(new ItemStack(ModItems.COMPAT_INGREDIENT, 1, 8),
                    OreDictionary.getOres("ingotUranium").get(0), 0.1f);
            compatSmeltsFound++;
        }
        CreateLegacy.logger.info("Found {} compatible smeltables total", compatSmeltsFound);
    }

    public static void registerCompatPressRecipes() {
        int compatPlatesFound = 0;
        CreateLegacy.logger.info("Attempting to register compat plate pressing");
        for(String metal : MetalTypes.METAL_NAMES) {
            if (OreDictionaryCompat.doesOreDictReallyExist(MetalTypes.INGOT + metal) && OreDictionaryCompat.doesOreDictReallyExist(MetalTypes.PLATE + metal)) {
                for (int m = 0; m < OreDictionary.getOres(MetalTypes.INGOT + metal).size(); m++) {
                    CreateLegacy.logger.info("Found {} metal set for plate", metal.toLowerCase());
                    compatPlatesFound++;
                    addPressing(OreDictionary.getOres(MetalTypes.INGOT + metal).get(m).copy(),
                            OreDictionary.getOres(MetalTypes.PLATE + metal).get(0).copy());
                }
            }
        }
        CreateLegacy.logger.info("Compat plate pressing registry complete, {} compat(s) found", compatPlatesFound);
    }
    public static void registerCompatCompactingRecipes() {
        int compatIngotsFound = 0;
        CreateLegacy.logger.info("Attempting to register compat nugget to ingot compressing");
        for(String metal : MetalTypes.METAL_NAMES) {
            if (OreDictionaryCompat.doesOreDictReallyExist(MetalTypes.NUGGET + metal) && OreDictionaryCompat.doesOreDictReallyExist(MetalTypes.INGOT + metal)) {
                for (int m = 0; m < OreDictionary.getOres(MetalTypes.NUGGET + metal).size(); m++) {
                    CreateLegacy.logger.info("Found {} metal set for nugget to ingot", metal.toLowerCase());
                    compatIngotsFound++;
                    ItemStack stack0 = OreDictionary.getOres(MetalTypes.NUGGET + metal).get(m).copy();
                    ItemStack stack1 = OreDictionary.getOres(MetalTypes.INGOT + metal).get(0).copy();
                    add9Compacting(stack0, stack1);
                }
            }
        }
        CreateLegacy.logger.info("Compat nugget to ingot compacting registry complete, {} compat(s) found", compatIngotsFound);


        int compatBlocksFound = 0;
        CreateLegacy.logger.info("Attempting to register compat ingot to block compressing");
        for(String metal : MetalTypes.METAL_NAMES) {
            if (OreDictionaryCompat.doesOreDictReallyExist(MetalTypes.INGOT + metal) && OreDictionaryCompat.doesOreDictReallyExist(MetalTypes.BLOCK + metal)) {
                for (int m = 0; m < OreDictionary.getOres(MetalTypes.INGOT + metal).size(); m++) {
                    CreateLegacy.logger.info("Found {} metal set for ingot to block", metal.toLowerCase());
                    compatBlocksFound++;
                    ItemStack stack0 = OreDictionary.getOres(MetalTypes.INGOT + metal).get(m).copy();
                    ItemStack stack1 = OreDictionary.getOres(MetalTypes.BLOCK + metal).get(0).copy();
                    add9Compacting(stack0, stack1);
                }
            }
        }
        CreateLegacy.logger.info("Compat ingot to block compacting registry complete, {} compat(s) found", compatBlocksFound);
    }

    public static void registerCompatCrushingRecipes() {
        int compatOresFound = 0;
        CreateLegacy.logger.info("Attempting to register compat ore to crushed ore crushing");
        for(String metal : MetalTypes.METAL_NAMES) {
            if (OreDictionaryCompat.doesOreDictReallyExist(MetalTypes.ORE + metal) && OreDictionaryCompat.doesOreDictReallyExist(MetalTypes.CRUSHED + metal)) {
                for (int m = 0; m < OreDictionary.getOres(MetalTypes.ORE + metal).size(); m++) {
                    CreateLegacy.logger.info("Found {} metal set for ore to crushed", metal.toLowerCase());
                    compatOresFound++;
                    ItemStack stack0 = OreDictionary.getOres(MetalTypes.ORE + metal).get(m).copy();
                    ItemStack stack1 = OreDictionary.getOres(MetalTypes.CRUSHED + metal).get(0).copy();
                    addOreCrushing(stack0, stack1);
                }
            }
        }
        CreateLegacy.logger.info("Compat ore crushing complete, {} compat(s) found", compatOresFound);
    }
    private static void addWashing(@Nonnull ItemStack input, @Nonnull ItemStack result, ItemStack resultOptional) {
        WashingRecipes.instance().addWashingRecipe(input, result, resultOptional);
    }
    private static void addWashing(@Nonnull ItemStack input, @Nonnull ItemStack result) {
        WashingRecipes.instance().addWashingRecipe(input, result, ItemStack.EMPTY);
    }

    private static void addPressing(@Nonnull ItemStack input, @Nonnull ItemStack result) {
        PressingRecipes.instance().addPressing(input, result);
    }

    private static void addCompacting(@Nonnull ItemStack input, @Nonnull ItemStack result, BlockBlazeBurner.State heatRequirement) {
        CompactingRecipes.instance().addCompacting(input, result, heatRequirement);
    }
    private static void addCompacting(@Nonnull ItemStack input, @Nonnull ItemStack result) {
        CompactingRecipes.instance().addCompacting(input, result);
    }

    private static void add9Compacting(@Nonnull ItemStack input, @Nonnull ItemStack result) {
        input.setCount(9);
        result.setCount(1);
        addCompacting(input, result);
    }

    private static void addOreCrushing(ItemStack input, ItemStack result) {
        MillingRecipes.instance().addMillingRecipe(input, result, result, MillingRecipes.MILLING_TIME_DEFAULT, 50);
    }
}
