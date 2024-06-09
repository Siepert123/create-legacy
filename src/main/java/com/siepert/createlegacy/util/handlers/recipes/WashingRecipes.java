package com.siepert.createlegacy.util.handlers.recipes;

import com.google.common.collect.Maps;
import com.siepert.createlegacy.mainRegistry.ModItems;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class WashingRecipes {
    private static final WashingRecipes WASHING_BASE = new WashingRecipes();
    private final Map<ItemStack, ItemStack> washingList = Maps.<ItemStack, ItemStack>newHashMap();
    private final Map<ItemStack, ItemStack> optionalResultList = Maps.<ItemStack, ItemStack>newHashMap();


    public static WashingRecipes instance() {
        return WASHING_BASE;
    }

    private WashingRecipes() {
        this.addWashingRecipe(new ItemStack(ModItems.INGREDIENT, 1, 12),
                new ItemStack(Items.IRON_NUGGET, 9), new ItemStack(Items.REDSTONE));
        this.addWashingRecipe(new ItemStack(ModItems.INGREDIENT, 1, 13),
                new ItemStack(Items.GOLD_NUGGET, 9), new ItemStack(Items.REDSTONE));
        this.addWashingRecipe(new ItemStack(ModItems.INGREDIENT, 1, 14),
                new ItemStack(ModItems.INGREDIENT, 9, 4), new ItemStack(Items.REDSTONE));
        this.addWashingRecipe(new ItemStack(ModItems.INGREDIENT, 1, 15),
                new ItemStack(ModItems.INGREDIENT, 9, 7), new ItemStack(Items.GUNPOWDER));
    }

    public void addWashingRecipeForBlock(Block input, ItemStack stack, ItemStack stackOptional) {
        this.addWashing(Item.getItemFromBlock(input), stack, stackOptional);
    }

    public void addWashing(Item input, ItemStack stack, ItemStack stackOptional) {
        this.addWashingRecipe(new ItemStack(input, 1), stack, stackOptional);
    }

    public void addWashingRecipe(ItemStack input, ItemStack stack, ItemStack stackOptional) {
        if (getWashingResult(input) != ItemStack.EMPTY) { net.minecraftforge.fml.common.FMLLog.log.info("Ignored smelting recipe with conflicting input: {} = {}", input, stack); return; }
        this.washingList.put(input, stack);
        this.optionalResultList.put(input, stackOptional);
    }

    public ItemStack getWashingResult(ItemStack stack) {
        for (Map.Entry<ItemStack, ItemStack> entry : this.washingList.entrySet()) {
            if (this.compareItemStacks(stack, entry.getKey())) {
                return entry.getValue();
            }
        }

        return ItemStack.EMPTY;
    }

    private boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
        return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }

    public Map<ItemStack, ItemStack> getWashingList()
    {
        return this.washingList;
    }

    public ItemStack getOptionalResult(ItemStack stack) {
        for (Map.Entry<ItemStack, ItemStack> entry : this.optionalResultList.entrySet()) {
            if (this.compareItemStacks(stack, entry.getKey())) {
                return (entry.getValue());
            }
        }

        return ItemStack.EMPTY;
    }
}
