package com.siepert.createlegacy.tileentity;

import com.siepert.createapi.IKineticTE;
import com.siepert.createlegacy.util.Reference;
import com.siepert.createlegacy.util.handlers.ModSoundHandler;
import com.siepert.createlegacy.util.handlers.recipes.MillingRecipes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class TileEntityMillStone extends TileEntity implements ITickable, IKineticTE {
    private int currentMillProgress;
    private int maxMillProgress;
    private ItemStack currentlyMilling;
    private ItemStack output;
    private ItemStack outputOptional;
    private boolean hasRecipe;
    private int speed;
    @Override
    public void update() {
        MillingRecipes.ResultSet set = MillingRecipes.apply(currentlyMilling);
        speed = 64;
        hasRecipe = set.hasRecipe();
        if (hasRecipe) {
            maxMillProgress = set.getMillTime();
            if (areTheConditionsOK(set)) {
                handleTheRecipeStuff(set);
                currentMillProgress++;
                world.spawnParticle(EnumParticleTypes.CRIT,
                        pos.getX() + Reference.random.nextFloat(),
                        pos.getY() + Reference.random.nextFloat(),
                        pos.getZ() + Reference.random.nextFloat(),
                        0, 0, 0);
                if (world.getTotalWorldTime() % 5 == 0) {
                    world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            ModSoundHandler.BLOCK_MILLSTONE_AMBIENT, SoundCategory.BLOCKS, 0.5f, 1.0f);
                    world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            ModSoundHandler.BLOCK_MILLSTONE_AMBIENT, SoundCategory.BLOCKS, 0.4f, 1.0f);
                    world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            ModSoundHandler.BLOCK_MILLSTONE_AMBIENT, SoundCategory.BLOCKS, 0.4f, 1.0f);
                }
            }
        } else {
            resetAll();
        }

        if (currentlyMilling.isEmpty()) {
            AxisAlignedBB bb = new AxisAlignedBB(pos.up());

            List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, bb);

            for (EntityItem item : items) {
                if (currentlyMilling.isEmpty()) {
                    if (MillingRecipes.apply(item.getItem()).hasRecipe()) {
                        setCurrentlyMilling(item.getItem());
                        item.setDead();
                    }
                }
            }
        }

        markDirty();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("CurrentMillProgress", currentMillProgress);
        compound.setInteger("MaximumMillProgress", maxMillProgress);

        if (!currentlyMilling.isEmpty()) {
            NBTTagCompound inputStackNBT = new NBTTagCompound();
            currentlyMilling.writeToNBT(inputStackNBT);
            compound.setTag("CurrentlyMilling", inputStackNBT);
        }
        if (!output.isEmpty()) {
            NBTTagCompound outputStackNBT = new NBTTagCompound();
            output.writeToNBT(outputStackNBT);
            compound.setTag("Output", outputStackNBT);
        }
        if (!outputOptional.isEmpty()) {
            NBTTagCompound outputOptionalStackNBT = new NBTTagCompound();
            outputOptional.writeToNBT(outputOptionalStackNBT);
            compound.setTag("OutputOptional", outputOptionalStackNBT);
        }
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        currentMillProgress = compound.getInteger("CurrentMillProgress");
        maxMillProgress = compound.getInteger("MaximumMillProgress");
        if (compound.hasKey("CurrentlyMilling")) {
            currentlyMilling = new ItemStack(compound.getCompoundTag("CurrentlyMilling"));
        } else {
            currentlyMilling = ItemStack.EMPTY;
        }

        if (compound.hasKey("Output")) {
            output = new ItemStack(compound.getCompoundTag("Output"));
        } else {
            output = ItemStack.EMPTY;
        }

        if (compound.hasKey("OutputOptional")) {
            outputOptional = new ItemStack(compound.getCompoundTag("OutputOptional"));
        } else {
            outputOptional = ItemStack.EMPTY;
        }
    }

    public boolean hasOutput() {
        return !output.isEmpty();
    }
    public boolean hasOutputOptional() {
        return !outputOptional.isEmpty();
    }
    public boolean isMilling() {
        return !currentlyMilling.isEmpty();
    }


    public ItemStack getCurrentlyMilling() {
        return currentlyMilling;
    }
    public void setCurrentlyMilling(ItemStack currentlyMilling) {
        this.currentlyMilling = currentlyMilling;
        markDirty();
    }

    public ItemStack getOutput() {
        return output;
    }
    public ItemStack getOutputOptional() {
        return outputOptional;
    }

    public void removeItemStacksToPlayer(World worldIn, EntityPlayer player) {
        if (output.isEmpty() && outputOptional.isEmpty()) {
            EntityItem item = new EntityItem(worldIn, player.posX, player.posY, player.posZ);
            item.setNoPickupDelay();
            item.setItem(currentlyMilling.copy());
            currentlyMilling = ItemStack.EMPTY;
            worldIn.spawnEntity(item);
            return;
        }
        if (!output.isEmpty()) {
            EntityItem item = new EntityItem(worldIn, player.posX, player.posY, player.posZ);
            item.setNoPickupDelay();
            item.setItem(output.copy());
            output = ItemStack.EMPTY;
            worldIn.spawnEntity(item);
        }
        if (!outputOptional.isEmpty()) {
            EntityItem item = new EntityItem(worldIn, player.posX, player.posY, player.posZ);
            item.setNoPickupDelay();
            item.setItem(outputOptional.copy());
            outputOptional = ItemStack.EMPTY;
            worldIn.spawnEntity(item);
        }
    }

    private void resetAll() {
        this.maxMillProgress = 0;
        this.currentMillProgress = 0;
    }
    private boolean areTheConditionsOK(MillingRecipes.ResultSet set) {
        boolean flag = output.isEmpty() || set.getResult().isItemEqual(output);
        boolean flag1 = outputOptional.isEmpty() || set.getResultOptional().isItemEqual(outputOptional);
        boolean flag2 = outputOptional.getCount() < outputOptional.getMaxStackSize() && output.getCount() < output.getMaxStackSize();
        return flag && flag1 && hasRecipe && flag2;
    }
    private void handleTheRecipeStuff(MillingRecipes.ResultSet set) {
        if (currentMillProgress >= maxMillProgress) {
            currentMillProgress = 0;

            currentlyMilling.shrink(1);

            if (!output.isEmpty()) {
                output.setCount(output.getCount() + set.getResult().getCount());
            } else {
                output = set.getResult().copy();
            }

            if (set.hasOptional() && Reference.random.nextInt(100) < set.getPercentage()) {
                if (!outputOptional.isEmpty()) {
                    outputOptional.setCount(outputOptional.getCount() + set.getResultOptional().getCount());
                } else {
                    outputOptional = set.getResultOptional().copy();
                }
            }
        }
    }


    public TileEntityMillStone() {
        this.currentlyMilling = ItemStack.EMPTY;
        this.output = ItemStack.EMPTY;
        this.outputOptional = ItemStack.EMPTY;
    }

    public void dropItems() {
        if (!world.isRemote) {
            if (!currentlyMilling.isEmpty()) {
                EntityItem item = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                item.setItem(currentlyMilling.copy());
                world.spawnEntity(item);
            }
            if (!output.isEmpty()) {
                EntityItem item = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                item.setItem(output.copy());
                world.spawnEntity(item);
            }
            if (!outputOptional.isEmpty()) {
                EntityItem item = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                item.setItem(outputOptional.copy());
                world.spawnEntity(item);
            }
        }
    }

    @Override
    public int getConsumedSU() {
        return speed * 4;
    }

    @Override
    public int getSpeed() {
        return speed;
    }
}
