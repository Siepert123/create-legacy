package com.siepert.createlegacy.util.handlers.recipes;

import com.google.common.collect.Maps;
import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.blocks.kinetic.BlockBlazeBurner;
import com.siepert.createlegacy.mainRegistry.ModItems;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class CompactingRecipes {
    private static final CompactingRecipes COMPACTING_BASE = new CompactingRecipes();
    private final Map<ItemStack, ItemStack> compactingList = Maps.<ItemStack, ItemStack>newHashMap();
    private final Map<ItemStack, BlockBlazeBurner.State> heatRequirements = Maps.<ItemStack, BlockBlazeBurner.State>newHashMap();


    public static CompactingRecipes instance() {
        return COMPACTING_BASE;
    }

    /**Adds the default compacting recipes.*/
    private CompactingRecipes() {
        this.addCompacting(new ItemStack(Items.COAL, 9),
                new ItemStack(Blocks.COAL_BLOCK, 1));
        this.addCompacting(new ItemStack(Items.REDSTONE, 9),
                new ItemStack(Blocks.REDSTONE_BLOCK, 1));
        this.addCompacting(new ItemStack(Items.DYE, 9, 4),
                new ItemStack(Blocks.LAPIS_BLOCK, 1));
        this.addCompacting(new ItemStack(Items.DIAMOND, 9),
                new ItemStack(Blocks.DIAMOND_BLOCK, 1));
        this.addCompacting(new ItemStack(Items.EMERALD, 9),
                new ItemStack(Blocks.EMERALD_BLOCK, 1));

        //Misc compact
        this.addCompacting(new ItemStack(Items.DYE, 9, 15),
                new ItemStack(Blocks.BONE_BLOCK, 1));
        this.addCompacting(new ItemStack(Items.CLAY_BALL, 4),
                new ItemStack(Blocks.CLAY, 1));
        this.addCompacting(new ItemStack(Items.BRICK, 4),
                new ItemStack(Blocks.BRICK_BLOCK, 1));
        this.addCompacting(new ItemStack(Items.NETHERBRICK, 4),
                new ItemStack(Blocks.NETHER_BRICK, 1));
        this.addCompacting(new ItemStack(Items.SNOWBALL, 4),
                new ItemStack(Blocks.SNOW, 1));
        this.addCompacting(new ItemStack(Items.SLIME_BALL, 9),
                new ItemStack(Blocks.SLIME_BLOCK, 1));
        this.addCompacting(new ItemStack(Items.GLOWSTONE_DUST, 4),
                new ItemStack(Blocks.GLOWSTONE, 1));

        this.addCompacting(new ItemStack(ModItems.INGREDIENT, 1, 17),
                new ItemStack(ModItems.INCOMPLETE_ITEM, 1, 1), BlockBlazeBurner.State.HEATED);
    }

    public void addCompacting(Block input, ItemStack stack) {
        this.addCompacting(Item.getItemFromBlock(input), stack);
    }

    public void addCompacting(Item input, ItemStack stack) {
        this.addCompacting(new ItemStack(input, 1, 32767), stack);
    }

    public void addCompacting(ItemStack input, ItemStack stack) {
        this.addCompacting(input, stack, BlockBlazeBurner.State.EMPTY);
    }

    public void addCompacting(ItemStack input, ItemStack stack, BlockBlazeBurner.State heatRequirement) {
        if (getCompactingResult(input) != ItemStack.EMPTY) {
            CreateLegacy.logger.error("Ignored compacting recipe with conflicting input: {} = {}", input, stack);
            return;
        }
        this.compactingList.put(input, stack);
        this.heatRequirements.put(input, heatRequirement);
    }

    public ItemStack getCompactingResult(ItemStack stack) {
        for (Map.Entry<ItemStack, ItemStack> entry : this.compactingList.entrySet()) {
            if (this.compareItemStacks(stack, entry.getKey())) {
                return entry.getValue();
            }
        }

        return ItemStack.EMPTY;
    }
    public BlockBlazeBurner.State getHeatRequirement(ItemStack stack) {
        for (Map.Entry<ItemStack, BlockBlazeBurner.State> entry : this.heatRequirements.entrySet()) {
            if (this.compareItemStacks(stack, entry.getKey())) {
                return entry.getValue();
            }
        }

        return BlockBlazeBurner.State.EMPTY;
    }
    public int getCompactingCost(ItemStack stack) {
        for (Map.Entry<ItemStack, ItemStack> entry : this.compactingList.entrySet()) {
            if (this.compareItemStacks(stack, entry.getKey())) {
                return entry.getKey().getCount();
            }
        }

        return 1;
    }

    private boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
        return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }

    public Map<ItemStack, ItemStack> getCompactingList() {
        return this.compactingList;
    }
    public Map<ItemStack, BlockBlazeBurner.State> getHeatRequirementsList() {
        return this.heatRequirements;
    }

}
