package com.siepert.createlegacy.util.handlers;

import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RecipeHandler {
    public static void registerOreSmelting() {
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


}
