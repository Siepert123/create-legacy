package com.melonstudios.createlegacy.recipe;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public final class RecipeInit {
    private static boolean initialized = false;
    public static void init() {
        if (initialized) return;

        //Test
        PressingRecipes.addRecipe(new ItemStack(Items.REEDS),
                new ItemStack(Items.PAPER));
        WashingRecipes.addRecipe(new ItemStack(Blocks.GRAVEL),
                new ItemStack[]{
                        new ItemStack(Items.FLINT),
                        new ItemStack(Items.IRON_NUGGET, 2)
                },
                new float[]{
                        0.8f
                });

        initialized = true;
    }
}
