package com.melonstudios.createlegacy.recipe;

import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.event.MetalTypesQueryEvent;
import com.melonstudios.createlegacy.item.ModItems;
import com.melonstudios.createlegacy.util.DisplayLink;
import com.melonstudios.createlegacy.util.RecipeEntry;
import com.melonstudios.createlegacy.util.SimpleTuple;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Initialized all recipes that aren't data-driven.
 * @since 0.1.0
 * @see PressingRecipes
 * @see SandingRecipes
 * @see WashingRecipes
 */
public final class RecipeInit {
    private static boolean initialized = false;
    private static boolean doesOreDictNameExist(String ore) {
        if (OreDictionary.doesOreNameExist(ore)) {
            return !OreDictionary.getOres(ore).isEmpty();
        }
        return false;
    }

    private static final List<String> metals = new ArrayList<>();
    private static String ingot(String metal) {
        return "ingot" + metal;
    }
    private static String plate(String metal) {
        return "plate" + metal;
    }
    private static String crushed(String metal) {
        return "crushed" + metal;
    }
    private static String nugget(String metal) {
        return "nugget" + metal;
    }
    private static String ore(String metal) {
        return "ore" + metal;
    }
    public static void init() {
        if (initialized) return;
        long startTime = System.currentTimeMillis();

        MinecraftForge.EVENT_BUS.post(new MetalTypesQueryEvent(metals));

        DisplayLink.debug("Initializing recipes");

        for (String metal : metals) {
            if (doesOreDictNameExist(ingot(metal))) {
                if (doesOreDictNameExist(plate(metal))) {
                    for (ItemStack stack : OreDictionary.getOres(ingot(metal))) {
                        PressingRecipes.addRecipe(stack, OreDictionary.getOres(plate(metal)).get(0));
                    }
                }
            }
            if (doesOreDictNameExist(ore(metal))) {
                if (doesOreDictNameExist(crushed(metal))) {
                    for (ItemStack stack : OreDictionary.getOres(ore(metal))) {
                        ItemStack crushed = OreDictionary.getOres(crushed(metal)).get(0).copy();
                        //Crushing
                    }
                }
            }
            if (doesOreDictNameExist(crushed(metal))) {
                if (doesOreDictNameExist(nugget(metal))) {
                    for (ItemStack stack : OreDictionary.getOres(crushed(metal))) {
                        ItemStack nuggets = OreDictionary.getOres(nugget(metal)).get(0).copy();
                        nuggets.setCount(9);
                        WashingRecipes.addRecipe(stack,
                                SimpleTuple.optionalRecipeEntry(nuggets)
                        );
                    }
                }
                if (doesOreDictNameExist(ingot(metal))) {
                    for (ItemStack stack : OreDictionary.getOres(crushed(metal))) {
                        ItemStack ingot = OreDictionary.getOres(ingot(metal)).get(0).copy();
                        FurnaceRecipes.instance().addSmeltingRecipe(stack, ingot, 0.1f);
                    }
                }
            }
        }

        //CRUSH!!! PREPARE THYSELF!!!
        CrushingRecipes.addRecipe(new ItemStack(Blocks.OBSIDIAN, 1), 10.0f,
                RecipeEntry.get(new ItemStack(ModItems.INGREDIENT, 1, 17)),
                RecipeEntry.get(new ItemStack(Blocks.OBSIDIAN, 1), .75f));
        CrushingRecipes.addRecipe(new ItemStack(Blocks.NETHERRACK, 1), 0.5f,
                RecipeEntry.get(new ItemStack(ModItems.INGREDIENT, 1, 16)),
                RecipeEntry.get(new ItemStack(ModItems.INGREDIENT, 1, 16), .5f));
        for (String metal : metals) {
            for (ItemStack ore : OreDictionary.getOres(ore(metal))) {
                if (doesOreDictNameExist(crushed(metal))) {
                    CrushingRecipes.addRecipe(ore, 1.0f,
                            RecipeEntry.get(OreDictionary.getOres(crushed(metal)).get(0)),
                            RecipeEntry.get(OreDictionary.getOres(crushed(metal)).get(0), .5f),
                            RecipeEntry.get(new ItemStack(Blocks.COBBLESTONE), .25f));
                }
            }
        }

        //MILL!!!!
        MillingRecipes.addRecipe(new ItemStack(Blocks.COBBLESTONE, 1), 2.5f,
                RecipeEntry.get(new ItemStack(Blocks.GRAVEL, 1)),
                RecipeEntry.get(new ItemStack(Items.FLINT, 1), .25f));
        MillingRecipes.addRecipe(new ItemStack(Blocks.GRAVEL, 1), 2.5f,
                RecipeEntry.get(new ItemStack(Items.FLINT, 1)));
        for (int i = 0; i < 16; i++) {
            MillingRecipes.addRecipe(new ItemStack(Blocks.WOOL, 1, i), 0.25f,
                    RecipeEntry.get(new ItemStack(Items.STRING, 2)),
                    RecipeEntry.get(new ItemStack(Items.STRING), 0.75f),
                    RecipeEntry.get(new ItemStack(Items.STRING), 0.25f));
        }
        MillingRecipes.addRecipe(new ItemStack(Blocks.YELLOW_FLOWER, 1, 0), 0.5f,
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 11)),
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 2), 0.5f));
        MillingRecipes.addRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 0), 0.5f,
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 1)),
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 2), 0.5f));
        MillingRecipes.addRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 1), 0.5f,
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 12)),
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 2), 0.5f));
        MillingRecipes.addRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 2), 0.5f,
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 13)),
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 10), 0.5f));
        MillingRecipes.addRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 3), 0.5f,
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 7)),
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 2), 0.5f));
        MillingRecipes.addRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 4), 0.5f,
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 1)),
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 10), 0.5f));
        MillingRecipes.addRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 5), 0.5f,
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 14)),
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 10), 0.5f));
        MillingRecipes.addRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 6), 0.5f,
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 7)),
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 10), 0.5f));
        MillingRecipes.addRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 7), 0.5f,
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 9)),
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 10), 0.5f));
        MillingRecipes.addRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 8), 0.5f,
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 7)),
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 2), 0.5f));
        MillingRecipes.addRecipe(new ItemStack(Blocks.DOUBLE_PLANT, 1, 0), 0.5f,
                RecipeEntry.get(new ItemStack(Items.DYE, 2, 11)),
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 2)),
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 2), 0.5f));
        MillingRecipes.addRecipe(new ItemStack(Blocks.DOUBLE_PLANT, 1, 1), 0.5f,
                RecipeEntry.get(new ItemStack(Items.DYE, 2, 9)),
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 2)),
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 10), 0.5f));
        MillingRecipes.addRecipe(new ItemStack(Blocks.DOUBLE_PLANT, 1, 4), 0.5f,
                RecipeEntry.get(new ItemStack(Items.DYE, 2, 1)),
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 2)),
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 10), 0.5f));
        MillingRecipes.addRecipe(new ItemStack(Blocks.DOUBLE_PLANT, 1, 5), 0.5f,
                RecipeEntry.get(new ItemStack(Items.DYE, 2, 9)),
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 2)),
                RecipeEntry.get(new ItemStack(Items.DYE, 1, 2), 0.5f));

        FurnaceRecipes.instance().addSmeltingRecipe(
                new ItemStack(ModBlocks.ORE, 1, 0),
                new ItemStack(ModItems.INGREDIENT, 1, 3),
                0.25f);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(ModBlocks.ORE, 1, 1),
                new ItemStack(ModItems.INGREDIENT, 1, 6),
                0.25f);

        for (ItemStack input : OreDictionary.getOres(crushed("iron"))) {
            WashingRecipes.addRecipe(input, true,
                    SimpleTuple.optionalRecipeEntry(new ItemStack(Items.IRON_NUGGET, 9)),
                    SimpleTuple.optionalRecipeEntry(new ItemStack(Items.REDSTONE), 0.12f)
            );
        }
        for (ItemStack input : OreDictionary.getOres(crushed("gold"))) {
            WashingRecipes.addRecipe(input, true,
                    SimpleTuple.optionalRecipeEntry(new ItemStack(Items.GOLD_NUGGET, 9)),
                    SimpleTuple.optionalRecipeEntry(new ItemStack(Items.QUARTZ), 0.50f)
            );
        }
        for (ItemStack input : OreDictionary.getOres(crushed("copper"))) {
            ItemStack nuggets = OreDictionary.getOres(nugget("copper")).get(0).copy();
            nuggets.setCount(9);
            WashingRecipes.addRecipe(input, true,
                    SimpleTuple.optionalRecipeEntry(nuggets),
                    SimpleTuple.optionalRecipeEntry(new ItemStack(Items.CLAY_BALL), 0.50f)
            );
        }
        for (ItemStack input : OreDictionary.getOres(crushed("zinc"))) {
            ItemStack nuggets = OreDictionary.getOres(nugget("zinc")).get(0).copy();
            nuggets.setCount(9);
            WashingRecipes.addRecipe(input, true,
                    SimpleTuple.optionalRecipeEntry(nuggets),
                    SimpleTuple.optionalRecipeEntry(new ItemStack(Items.GUNPOWDER), 0.25f)
            );
        }

        for (ItemStack stack : OreDictionary.getOres("gravel")) {
            WashingRecipes.addRecipe(stack,
                    SimpleTuple.optionalRecipeEntry(new ItemStack(Items.FLINT, 1), 0.25f),
                    SimpleTuple.optionalRecipeEntry(new ItemStack(Items.IRON_NUGGET, 1), 0.12f)
            );
        }
        for (ItemStack stack : OreDictionary.getOres("sand")) {
            if (stack.getItem() == Item.getItemFromBlock(Blocks.SAND) && stack.getMetadata() == 1) {
                WashingRecipes.addRecipe(stack,
                        SimpleTuple.optionalRecipeEntry(new ItemStack(Items.GOLD_NUGGET, 3), 0.12f),
                        SimpleTuple.optionalRecipeEntry(new ItemStack(Blocks.DEADBUSH, 1), 0.05f)
                );
            } else {
                WashingRecipes.addRecipe(stack,
                        SimpleTuple.optionalRecipeEntry(new ItemStack(Items.CLAY_BALL, 1), 0.25f)
                );
            }
        }

        for (ItemStack gem : OreDictionary.getOres("gemRoseQuartz")) {
            SandingRecipes.addRecipe(gem, new ItemStack(ModItems.INGREDIENT, 1, 20));
        }

        for (int i = 0; i < 16; i++) {
            if (i != 0) {
                WashingRecipes.addRecipe(new ItemStack(Blocks.WOOL, 1, i),
                        SimpleTuple.optionalRecipeEntry(new ItemStack(Blocks.WOOL, 1, 0))
                );
            }
            WashingRecipes.addRecipe(new ItemStack(Blocks.STAINED_GLASS, 1, i),
                    SimpleTuple.optionalRecipeEntry(new ItemStack(Blocks.GLASS, 1))
            );
            WashingRecipes.addRecipe(new ItemStack(Blocks.STAINED_GLASS_PANE, 1, i),
                    SimpleTuple.optionalRecipeEntry(new ItemStack(Blocks.GLASS_PANE, 1))
            );
            WashingRecipes.addRecipe(new ItemStack(Blocks.CONCRETE_POWDER, 1, i),
                    SimpleTuple.optionalRecipeEntry(new ItemStack(Blocks.CONCRETE, 1, i))
            );
        }

        PressingRecipes.addRecipe(new ItemStack(Items.REEDS, 1), new ItemStack(Items.PAPER, 1));
        PressingRecipes.addRecipe(new ItemStack(Blocks.GRASS, 1), new ItemStack(Blocks.GRASS_PATH, 1));

        WashingRecipes.addRecipe(new ItemStack(Blocks.MAGMA, 1),
                SimpleTuple.optionalRecipeEntry(new ItemStack(Blocks.OBSIDIAN, 1))
        );
        WashingRecipes.addRecipe(new ItemStack(Blocks.SOUL_SAND, 1),
                SimpleTuple.optionalRecipeEntry(new ItemStack(Items.QUARTZ, 4), 0.12f),
                SimpleTuple.optionalRecipeEntry(new ItemStack(Items.GOLD_NUGGET, 1), 0.02f)
        );

        {
            final String[] stones = new String[]{
                    "stoneAsurine",
                    "stoneCrimsite",
                    "stoneLimestone",
                    "stoneOchrum",
                    "stoneScorchia",
                    "stoneScoria",
                    "stoneVeridium",
            };

            for (String stone : stones) {
                for (ItemStack stack : OreDictionary.getOres(stone)) {
                    for (ItemStack stack1: OreDictionary.getOres(stone)) {
                        SawingRecipes.addRecipe(stack, stack1);
                    }
                }
            }
        }

        for (ItemStack colorless : OreDictionary.getOres("blockGlassColorless")) {
            SawingRecipes.addRecipe(colorless, new ItemStack(ModBlocks.FRAMED_GLASS, 1, 0));
            SawingRecipes.addRecipe(colorless, new ItemStack(ModBlocks.FRAMED_GLASS, 1, 1));
            SawingRecipes.addRecipe(colorless, new ItemStack(ModBlocks.FRAMED_GLASS, 1, 2));
            SawingRecipes.addRecipe(colorless, new ItemStack(ModBlocks.FRAMED_GLASS, 1, 3));
        }
        for (ItemStack colorless : OreDictionary.getOres("paneGlassColorless")) {
            SawingRecipes.addRecipe(colorless, new ItemStack(ModBlocks.FRAMED_GLASS_PANE, 1, 0));
            SawingRecipes.addRecipe(colorless, new ItemStack(ModBlocks.FRAMED_GLASS_PANE, 1, 1));
            SawingRecipes.addRecipe(colorless, new ItemStack(ModBlocks.FRAMED_GLASS_PANE, 1, 2));
            SawingRecipes.addRecipe(colorless, new ItemStack(ModBlocks.FRAMED_GLASS_PANE, 1, 3));
        }

        SawingRecipes.addRecipe(new ItemStack(ModItems.INGREDIENT, 1, 0), new ItemStack(ModBlocks.ROTATOR, 6, 0));

        DisplayLink.debug("Recipe init complete in %s ms!", System.currentTimeMillis() - startTime);
        initialized = true;
    }
}
