package com.siepert.createlegacy.util.handlers.recipes;

import com.siepert.createlegacy.blocks.kinetic.BlockBlazeBurner;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class AlloyingRecipe {
    private final ItemStack[] ingredients;
    private final ItemStack[] results;
    private final BlockBlazeBurner.State requiredHeat;
    private boolean valid = false;
    public AlloyingRecipe(ItemStack[] ingredients, ItemStack[] results, BlockBlazeBurner.State heat) {
        this.ingredients = ingredients;
        this.results = results;
        this.requiredHeat = heat;

        validate();
    }

    public void validate() {
        List<ItemStack> checks = new ArrayList<>();

        for (ItemStack stack : getIngredients()) {
            ItemStack checkStack = stack.copy();
            checkStack.setCount(1);

            if (checks.contains(checkStack)) return;

            checks.add(checkStack);
        }

        valid = true;
    }


    public ItemStack[] getIngredients() {
        return ingredients;
    }
    public ItemStack[] getResults() {
        return results;
    }
    public BlockBlazeBurner.State getRequiredHeat() {
        return requiredHeat;
    }
    public boolean isValid() {
        return valid;
    }
    public void spawnResultItems(World worldIn, BlockPos pos) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        for (ItemStack stack : getResults()) {
            EntityItem item = new EntityItem(worldIn, x, y, z, stack);
            item.setNoDespawn();
            worldIn.spawnEntity(item);
        }
    }
}
