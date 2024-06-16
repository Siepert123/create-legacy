package com.siepert.createlegacy.util.compat;

import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.util.handlers.recipes.CompactingRecipes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;

public class TinkersCompat {
    public static void registerCompact() {
        if (Loader.isModLoaded("tconstruct")) {
            CreateLegacy.logger.info("Tinker's Construct appears to be loaded!");

            Item edible = Item.getByNameOrId("tconstruct:edible");
            Item congealedSlime = Item.getByNameOrId("tconstruct:congealed_slime");
            boolean congealValidation = edible != null && congealedSlime != null;
            if (congealValidation) {
                Item slimeBlock = Item.getByNameOrId("tconstruct:slime");
                if (slimeBlock != null) {
                    addCompacting(new ItemStack(edible, 9, 1),
                            new ItemStack(slimeBlock, 1, 1));
                    addCompacting(new ItemStack(edible, 9, 2),
                            new ItemStack(slimeBlock, 1, 2));
                    addCompacting(new ItemStack(edible, 9, 3),
                            new ItemStack(slimeBlock, 1, 3));
                    addCompacting(new ItemStack(edible, 9, 4),
                            new ItemStack(slimeBlock, 1, 4));
                } else CreateLegacy.logger.error("Item tconstruct:slime appears to be null");
                addCompacting(new ItemStack(Items.SLIME_BALL, 4),
                        new ItemStack(congealedSlime, 1, 0));
                addCompacting(new ItemStack(edible, 4, 1),
                        new ItemStack(congealedSlime, 1, 1));
                addCompacting(new ItemStack(edible, 4, 2),
                        new ItemStack(congealedSlime, 1, 2));
                addCompacting(new ItemStack(edible, 4, 3),
                        new ItemStack(congealedSlime, 1, 3));
                addCompacting(new ItemStack(edible, 4, 4),
                        new ItemStack(congealedSlime, 1, 4));
            } else {
                CreateLegacy.logger.error("Item tconstruct:edible or tconstruct:congealed_slime appears to be null");
            }

            Item brickSeared = Item.getByNameOrId("tconstruct:seared");

            if (brickSeared != null) {
                for (int i = 0; i < OreDictionary.getOres("brickSeared").size(); i++) {
                    ItemStack brickStack = OreDictionary.getOres("brickSeared").get(i).copy();
                    brickStack.setCount(4);
                    addCompacting(brickStack, new ItemStack(brickSeared, 1, 3));
                }
            } else {
                CreateLegacy.logger.error("The seared brick item is NULL. Can we get that checked, please?");
            }
        }
    }
    private static void addCompacting(@Nonnull ItemStack input, @Nonnull ItemStack result) {
        CompactingRecipes.instance().addCompacting(input, result);
    }
}
