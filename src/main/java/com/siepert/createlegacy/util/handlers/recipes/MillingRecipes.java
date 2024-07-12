package com.siepert.createlegacy.util.handlers.recipes;

import com.google.common.collect.Maps;
import com.siepert.createlegacy.blocks.kinetic.BlockFan;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Config;

import javax.annotation.Nullable;
import java.util.Map;

public class MillingRecipes {
    private static final MillingRecipes MILLING_BASE = new MillingRecipes();
    private final Map<ItemStack, ItemStack> resultList = Maps.<ItemStack, ItemStack>newHashMap();
    private final Map<ItemStack, ItemStack> optionalResultList = Maps.<ItemStack, ItemStack>newHashMap();
    private final Map<ItemStack, Integer> timingsList = Maps.<ItemStack, Integer>newHashMap();
    private final Map<ItemStack, Integer> percentageList = Maps.<ItemStack, Integer>newHashMap();
    public static final int MILLING_TIME_DEFAULT = 100; //Milling time at 64 RPM

    public Map<ItemStack, ItemStack> getResultList() {
        return resultList;
    }

    public Map<ItemStack, ItemStack> getOptionalResultList() {
        return optionalResultList;
    }

    public Map<ItemStack, Integer> getTimingsList() {
        return timingsList;
    }

    public Map<ItemStack, Integer> getPercentageList() {
        return percentageList;
    }

    public static MillingRecipes instance() {
        return MILLING_BASE;
    }

    private MillingRecipes() {
        this.addMillingRecipe(new ItemStack(Blocks.NETHERRACK),
                new ItemStack(ModItems.INGREDIENT, 1, 16));
        this.addMillingRecipe(new ItemStack(Blocks.OBSIDIAN),
                new ItemStack(ModItems.INGREDIENT, 1, 17),
                new ItemStack(Blocks.OBSIDIAN), MILLING_TIME_DEFAULT * 5, 75);
        this.addMillingRecipe(new ItemStack(Items.WHEAT),
                new ItemStack(ModItems.SCRUMPTIOUS_FOOD, 1, 0),
                new ItemStack(Items.WHEAT_SEEDS), MILLING_TIME_DEFAULT / 4);
        this.addMillingRecipe(new ItemStack(Blocks.GLOWSTONE),
                new ItemStack(Items.GLOWSTONE_DUST, 3),
                new ItemStack(Items.GLOWSTONE_DUST), MILLING_TIME_DEFAULT, 50);

        this.addMillingRecipe(new ItemStack(Blocks.COBBLESTONE),
                new ItemStack(Blocks.GRAVEL));
        this.addMillingRecipe(new ItemStack(Blocks.GRAVEL),
                new ItemStack(Blocks.SAND, 1, 0), new ItemStack(Items.FLINT));
        this.addMillingRecipe(new ItemStack(Blocks.HARDENED_CLAY),
                new ItemStack(Blocks.SAND, 1, 1));

        for (int meta = 0; meta < 16; meta++) {
            this.addMillingRecipe(new ItemStack(Blocks.WOOL, 1, meta),
                    new ItemStack(Items.STRING, 2),
                    new ItemStack(Items.STRING, 2),
                    MILLING_TIME_DEFAULT / 4, 50);
        }
    }

    public void addMillingRecipe(ItemStack input, ItemStack stack, ItemStack stackOptional) {
        this.addMillingRecipe(input, stack, stackOptional, MILLING_TIME_DEFAULT);
    }

    public void addMillingRecipe(ItemStack input, ItemStack stack) {
        this.addMillingRecipe(input, stack, ItemStack.EMPTY, MILLING_TIME_DEFAULT, 100);
    }
    public void addMillingRecipe(ItemStack input, ItemStack stack, int millingTime) {
        this.addMillingRecipe(input, stack, ItemStack.EMPTY, millingTime, 100);
    }

    public void addMillingRecipe(Block input, ItemStack stack, ItemStack stackOptional, int millingTime) {
        this.addMillingRecipe(Item.getItemFromBlock(input), stack, stackOptional, millingTime);
    }

    public void addMillingRecipe(Item input, ItemStack stack, ItemStack stackOptional, int millingTime) {
        this.addMillingRecipe(new ItemStack(input, 1), stack, stackOptional, millingTime);
    }

    public void addMillingRecipe(ItemStack input, ItemStack stack, ItemStack stackOptional, int millingTime) {
        this.addMillingRecipe(input, stack, stackOptional, millingTime, 25);
    }
    public void addMillingRecipe(ItemStack input, ItemStack stack, ItemStack stackOptional, int millingTime, int percentage) {
        if (getMillingResult(input) != ItemStack.EMPTY) { net.minecraftforge.fml.common.FMLLog.log.info("Ignored milling recipe with conflicting input: {} = {}", input, stack); return; }
        this.resultList.put(input, stack);
        this.optionalResultList.put(input, stackOptional);
        this.timingsList.put(input, millingTime);
        this.percentageList.put(input, percentage);
    }

    public ItemStack getMillingResult(ItemStack stack) {
        for (Map.Entry<ItemStack, ItemStack> entry : this.resultList.entrySet()) {
            if (this.compareItemStacks(stack, entry.getKey())) {
                return entry.getValue();
            }
        }

        return ItemStack.EMPTY;
    }

    private boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
        return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }

    public ItemStack getOptionalResult(ItemStack stack) {
        for (Map.Entry<ItemStack, ItemStack> entry : this.optionalResultList.entrySet()) {
            if (this.compareItemStacks(stack, entry.getKey())) {
                return (entry.getValue());
            }
        }

        return ItemStack.EMPTY;
    }

    public int getMillingTime(ItemStack stack) {
        for (Map.Entry<ItemStack, Integer> entry : this.timingsList.entrySet()) {
            if (this.compareItemStacks(stack, entry.getKey())) {
                return (entry.getValue());
            }
        }

        return MILLING_TIME_DEFAULT;
    }
    public int getOptionalChance(ItemStack stack) {
        for (Map.Entry<ItemStack, Integer> entry : this.percentageList.entrySet()) {
            if (this.compareItemStacks(stack, entry.getKey())) {
                return (entry.getValue());
            }
        }

        return 100;
    }

    public static class ResultSet {
        ItemStack stack, stackOptional;
        boolean hasRecipe, hasOptional;
        int millTime, percentage;

        private ResultSet(ItemStack stack, boolean hasRecipe, int millTime, int percentage) {
            this.stack = stack;
            this.stackOptional = ItemStack.EMPTY;
            this.hasOptional = false;
            this.hasRecipe = hasRecipe;
            this.millTime = millTime;
            this.percentage = percentage;
        }

        private ResultSet(ItemStack stack, ItemStack stackOptional, boolean hasRecipe, int millTime, int percentage) {
            this.stack = stack;
            this.stackOptional = stackOptional;
            this.hasOptional = true;
            this.hasRecipe = hasRecipe;
            this.millTime = millTime;
            this.percentage = percentage;
        }

        public boolean hasRecipe() {
            return hasRecipe;
        }

        public ItemStack getResult() {
            return stack;
        }
        public ItemStack getResultOptional() {
            return stackOptional;
        }
        public boolean hasOptional() {
            return hasOptional;
        }
        public int getMillTime() {
            return millTime;
        }
        public int getPercentage() {
            return percentage;
        }
    }

    public static ResultSet apply(ItemStack stack) {
        if (stack.isEmpty()) {
            return new ResultSet(stack, false, -1, -1);
        }
        else {
            ItemStack itemstack = instance().getMillingResult(stack);
            ItemStack stackOpt = instance().getOptionalResult(stack);

            if (itemstack.isEmpty() && stackOpt.isEmpty()) {
                return new ResultSet(stack, false, -1, -1);
            } else {
                ItemStack itemstack1 = itemstack.copy();
                itemstack1.setCount(itemstack.getCount());
                ItemStack itemstack2 = stackOpt.copy();
                itemstack2.setCount(stackOpt.getCount());

                return new ResultSet(itemstack1, itemstack2, true,
                        instance().getMillingTime(stack), instance().getOptionalChance(stack));
            }
        }
    }
}
