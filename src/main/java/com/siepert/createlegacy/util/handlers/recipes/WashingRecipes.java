package com.siepert.createlegacy.util.handlers.recipes;

import com.google.common.collect.Maps;
import com.siepert.createlegacy.mainRegistry.ModItems;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
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
                new ItemStack(Items.GOLD_NUGGET, 9), new ItemStack(Items.QUARTZ));
        this.addWashingRecipe(new ItemStack(ModItems.INGREDIENT, 1, 14),
                new ItemStack(ModItems.INGREDIENT, 9, 4), new ItemStack(Items.CLAY_BALL));
        this.addWashingRecipe(new ItemStack(ModItems.INGREDIENT, 1, 15),
                new ItemStack(ModItems.INGREDIENT, 9, 7), new ItemStack(Items.GUNPOWDER));

        this.addWashingRecipe(new ItemStack(Blocks.GRAVEL, 1),
                new ItemStack(Items.FLINT, 1), new ItemStack(Items.IRON_NUGGET, 1));
        this.addWashingRecipe(new ItemStack(Blocks.SAND, 1, 0),
                ItemStack.EMPTY, new ItemStack(Items.CLAY_BALL, 1));
        this.addWashingRecipe(new ItemStack(Blocks.SAND, 1, 1),
                new ItemStack(Blocks.DEADBUSH, 1), new ItemStack(Items.GOLD_NUGGET, 4));
        this.addWashingRecipe(new ItemStack(Blocks.SOUL_SAND, 1),
                ItemStack.EMPTY, new ItemStack(Items.QUARTZ, 2));

        for (int c = 0; c < 16; c++) {
            this.addWashingRecipe(new ItemStack(Blocks.CONCRETE_POWDER, 1, c),
                    new ItemStack(Blocks.CONCRETE, 1, c));
            if (c != 0) this.addWashingRecipe(new ItemStack(Items.BED, 1, c),
                    new ItemStack(Items.BED, 1, 0));
            if (c != 15) this.addWashingRecipe(new ItemStack(Items.BANNER, 1, c),
                    new ItemStack(Items.BANNER, 1, 15));
            if (c != 0) this.addWashingRecipe(new ItemStack(Blocks.CARPET, 1, c),
                    new ItemStack(Blocks.CARPET, 1, 0));
            if (c != 0) this.addWashingRecipe(new ItemStack(Blocks.WOOL, 1, c),
                    new ItemStack(Blocks.WOOL, 1, 0));
        }

        this.addWashingRecipe(new ItemStack(Blocks.SPONGE, 1, 0),
                new ItemStack(Blocks.SPONGE, 1, 1));
    }
    public void addWashingRecipe(ItemStack input, ItemStack stack) {
        this.addWashingRecipe(input, stack, ItemStack.EMPTY);
    }

    public void addWashingRecipe(Block input, ItemStack stack, ItemStack stackOptional) {
        this.addWashingRecipe(Item.getItemFromBlock(input), stack, stackOptional);
    }

    public void addWashingRecipe(Item input, ItemStack stack, ItemStack stackOptional) {
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

    public Map<ItemStack, ItemStack> getWashingListOptional()
    {
        return this.optionalResultList;
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
