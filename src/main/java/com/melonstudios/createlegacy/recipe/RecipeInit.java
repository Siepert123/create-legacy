package com.melonstudios.createlegacy.recipe;

import com.melonstudios.createlegacy.util.DisplayLink;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public final class RecipeInit implements Runnable {
    private static boolean initialized = false;
    private static void init() {
        if (initialized) return;
        DisplayLink.debug("Initializing recipes");

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

    @Override
    public void run() {
        init();
    }
}
