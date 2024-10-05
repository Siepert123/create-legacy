package com.melonstudios.createlegacy.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileEntityDepot extends TileEntity implements ISidedInventory {
    protected ItemStack stack = ItemStack.EMPTY;
    protected ItemStack output = ItemStack.EMPTY;


    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey("Stack")) {
            stack = new ItemStack(compound.getCompoundTag("Stack"));
        }
        if (compound.hasKey("Output")) {
            output = new ItemStack(compound.getCompoundTag("Output"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        if (!stack.isEmpty()) {
            NBTTagCompound nbt = new NBTTagCompound();
            stack.writeToNBT(nbt);
            compound.setTag("Stack", nbt);
        }
        if (!output.isEmpty()) {
            NBTTagCompound nbt = new NBTTagCompound();
            output.writeToNBT(nbt);
            compound.setTag("Output", nbt);
        }

        return compound;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[]{0, 1};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return index == 0;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return index == 1;
    }

    @Override
    public int getSizeInventory() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty() && output.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return index == 0 ? stack : output;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return index == 0 ? stack.splitStack(count) : output.splitStack(count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (index == 0) {
            ItemStack toReturn = stack;
            stack = ItemStack.EMPTY;
            return toReturn;
        }
        ItemStack toReturn = output;
        output = ItemStack.EMPTY;
        return toReturn;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index == 0) this.stack = stack;
        else this.output = stack;
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
        return index == 0;
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
        stack = ItemStack.EMPTY;
        output = ItemStack.EMPTY;
    }

    @Override
    public String getName() {
        return "Depot";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }
}
