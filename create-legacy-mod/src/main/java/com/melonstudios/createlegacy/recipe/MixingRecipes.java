package com.melonstudios.createlegacy.recipe;

import com.melonstudios.createlegacy.util.ItemStackSorter;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MixingRecipes {
    private static final MixingRecipes instance = new MixingRecipes();
    public static MixingRecipes getInstance() {
        return instance;
    }

    private final Map<List<ItemStack>, List<ItemStack>> recipes = new HashMap<>();

    public void addRecipe(List<ItemStack> inputs, List<ItemStack> results) {
        inputs.sort(new ItemStackSorter());
        results.sort(new ItemStackSorter());

        List<ItemStack> inputCheck = new ArrayList<>();
        for (ItemStack stack : inputs) {
            for (ItemStack check : inputCheck) {
                if (stack.isItemEqual(check)) return;
                inputCheck.add(stack);
            }
        }

        recipes.put(inputs, results);
    }

    public ItemStack getResults(List<ItemStack> inputs) {
        inputs.sort(new ItemStackSorter());

        for (List<ItemStack> stackList : recipes.keySet()) {

        }

        return ItemStack.EMPTY;
    }
}
