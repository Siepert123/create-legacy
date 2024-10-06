package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.network.PacketUpdateMillstone;
import com.melonstudios.createlegacy.recipe.MillingRecipes;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKinetic;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;

import java.util.Random;

public class TileEntityMillstone extends AbstractTileEntityKinetic implements ISidedInventory {
    ItemStack currentlyMilling = ItemStack.EMPTY;
    ItemStack outputMain = ItemStack.EMPTY;
    ItemStack additionalOutput = ItemStack.EMPTY;
    ItemStack otherAdditionalOutput = ItemStack.EMPTY;

    private static final int[] topFaceSlots = new int[]{0};
    private static final int[] sideFaceSlots = new int[]{1,2,3};

    public boolean renderParticles() {
        return !currentlyMilling.isEmpty() && speed() != 0 && MillingRecipes.hasRecipe(currentlyMilling);
    }

    protected int progress;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setInteger("progress", progress);

        NBTTagCompound inventory = new NBTTagCompound();

        saveItemStack(inventory, currentlyMilling, "CurrentlyMilling");
        saveItemStack(inventory, outputMain, "OutputMain");
        saveItemStack(inventory, additionalOutput, "AdditionalOutput");
        saveItemStack(inventory, otherAdditionalOutput, "OtherAdditionalOutput");

        if (!inventory.hasNoTags()) compound.setTag("Inventory", inventory);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        progress = compound.getInteger("progress");

        if (compound.hasKey("Inventory")) {
            NBTTagCompound inventory = compound.getCompoundTag("Inventory");

            currentlyMilling = readItemStack(inventory, "CurrentlyMilling");
            outputMain = readItemStack(inventory, "OutputMain");
            additionalOutput = readItemStack(inventory, "AdditionalOutput");
            otherAdditionalOutput = readItemStack(inventory, "OtherAdditionalOutput");
        }
    }

    @Override
    protected void tick() {
        if (world.isRemote) {
            if (renderParticles()) {
                Random random = world.rand;
                world.spawnParticle(EnumParticleTypes.CRIT,
                        pos.getX() + random.nextFloat(),
                        pos.getY() + 0.5,
                        pos.getZ() + random.nextFloat(),
                        0, 0, 0);
            }
        } else {
            if (renderParticles()) {
                if (world.getTotalWorldTime() % 10 == 0) {
                    world.playSound(null, pos,
                            SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS,
                            1.0f, 1.0f);
                }
            }
            PacketUpdateMillstone.sendToPlayersNearby(this, 32);
        }
    }

    @Override
    protected String namePlate() {
        return "Millstone";
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        return side == EnumFacing.DOWN ? connection(1) : side.getAxis() != EnumFacing.Axis.Y ? connection(2) : connection(0);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return side == EnumFacing.UP ? topFaceSlots : sideFaceSlots;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return index == 0 && direction != EnumFacing.DOWN && MillingRecipes.hasRecipe(itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return index != 0 && direction != EnumFacing.UP;
    }

    @Override
    public int getSizeInventory() {
        return 4;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return null;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        switch (index) {
            case 0: return currentlyMilling.splitStack(count);
            case 1: return outputMain.splitStack(count);
            case 2: return additionalOutput.splitStack(count);
            case 3: return otherAdditionalOutput.splitStack(count);
            default: throw new IllegalArgumentException("Invalid slot index: " + index);
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack;
        switch (index) {
            case 0: stack = currentlyMilling; currentlyMilling = ItemStack.EMPTY; break;
            case 1: stack = outputMain; outputMain = ItemStack.EMPTY; break;
            case 2: stack = additionalOutput; additionalOutput = ItemStack.EMPTY; break;
            case 3: stack = otherAdditionalOutput; otherAdditionalOutput = ItemStack.EMPTY; break;
            default: throw new IllegalArgumentException("Invalid slot index: " + index);
        }
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        switch (index) {
            case 0: currentlyMilling = stack; break;
            case 1: outputMain = stack; break;
            case 2: additionalOutput = stack; break;
            case 3: otherAdditionalOutput = stack; break;
            default: throw new IllegalArgumentException("Invalid slot index: " + index);
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index == 0 && MillingRecipes.hasRecipe(stack);
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        currentlyMilling = ItemStack.EMPTY;
        outputMain = ItemStack.EMPTY;
        additionalOutput = ItemStack.EMPTY;
        otherAdditionalOutput = ItemStack.EMPTY;
    }

    @Override
    public String getName() {
        return "Millstone";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }
}
