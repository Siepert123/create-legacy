package com.siepert.createlegacy.util.handlers.recipes;

import com.google.common.collect.Maps;
import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.mainRegistry.ModItems;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class PressingRecipes {
    private static final PressingRecipes PRESSING_BASE = new PressingRecipes();
    private final Map<ItemStack, ItemStack> pressingList = Maps.<ItemStack, ItemStack>newHashMap();

    public static PressingRecipes instance()
    {
        return PRESSING_BASE;
    }

    /**Adds the default pressing recipes.*/
    private PressingRecipes() {
        //Vanilla pressing
        this.addPressing(Items.REEDS, new ItemStack(Items.PAPER, 1));
        this.addPressing(Blocks.GRASS, new ItemStack(Blocks.GRASS_PATH, 1));
        this.addPressing(new ItemStack(ModItems.INCOMPLETE_ITEM, 1, 1),
                new ItemStack(ModItems.INGREDIENT, 1, 18));
    }

    public void addPressing(Block input, ItemStack stack) {
        this.addPressing(Item.getItemFromBlock(input), stack);
    }

    public void addPressing(Item input, ItemStack stack) {
        this.addPressing(new ItemStack(input, 1, 32767), stack);
    }

    public void addPressing(ItemStack input, ItemStack stack)
    {
        if (getPressingResult(input) != ItemStack.EMPTY) {
            CreateLegacy.logger.error("Ignored smelting recipe with conflicting input: {} = {}", input, stack);
            return;
        }
        this.pressingList.put(input, stack);
    }

    public ItemStack getPressingResult(ItemStack stack) {
        for (Map.Entry<ItemStack, ItemStack> entry : this.pressingList.entrySet()) {
            if (this.compareItemStacks(stack, entry.getKey())) {
                return entry.getValue();
            }
        }

        return ItemStack.EMPTY;
    }

    private boolean compareItemStacks(ItemStack stack1, ItemStack stack2)
    {
        return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }

    public Map<ItemStack, ItemStack> getPressingList()
    {
        return this.pressingList;
    }

}
