package com.melonstudios.createlegacy.recipe;

import com.melonstudios.createlegacy.event.MetalTypesQueryEvent;
import com.melonstudios.createlegacy.event.RecipeInitEvent;
import com.melonstudios.createlegacy.item.ModItems;
import com.melonstudios.createlegacy.util.DisplayLink;
import com.melonstudios.createlegacy.util.SimpleTuple;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Initialized all recipes that aren't data-driven.
 * @since 0.1.0
 */
@SuppressWarnings("unchecked")
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


    public static void init() {
        if (initialized) return;
        long startTime = System.currentTimeMillis();

        MinecraftForge.EVENT_BUS.post(new MetalTypesQueryEvent(metals));

        DisplayLink.debug("Initializing recipes");

        MinecraftForge.EVENT_BUS.post(new RecipeInitEvent.Pre());

        for (String metal : metals) {
            if (doesOreDictNameExist(ingot(metal))) {
                if (doesOreDictNameExist(plate(metal))) {
                    for (ItemStack stack : OreDictionary.getOres(ingot(metal))) {
                        PressingRecipes.addRecipe(stack, OreDictionary.getOres(plate(metal)).get(0));
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
            }
        }

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

        MinecraftForge.EVENT_BUS.post(new RecipeInitEvent.Post());

        DisplayLink.debug("Recipe init complete in %s ms!", System.currentTimeMillis() - startTime);
        initialized = true;
    }
}
