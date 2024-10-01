package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.recipe.SawingRecipes;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import com.melonstudios.createlegacy.util.NetworkContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;

public class TileEntitySaw extends AbstractTileEntityKinetic implements IInventory, ITickable {
    private int index = 0;
    private ItemStack processing = ItemStack.EMPTY;
    private int progress = 0;
    private final int maxProgress = 2560;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setInteger("index", index);
        compound.setInteger("progress", progress);

        if (!processing.isEmpty()) {
            NBTTagCompound nbt = new NBTTagCompound();
            processing.writeToNBT(nbt);
            compound.setTag("ProcessingStack", nbt);
        }

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        index = compound.getInteger("index");
        progress = compound.getInteger("progress");

        if (compound.hasKey("ProcessingStack")) {
            processing = new ItemStack(compound.getCompoundTag("ProcessingStack"));
        }
    }

    public int getIndex() {
        return index;
    }
    public void increaseIndex() {
        index++;
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return processing.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return processing;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return processing.splitStack(index);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack = processing.copy();
        processing = ItemStack.EMPTY;
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        processing = stack;
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

    }

    @Override
    public String getName() {
        return "Saw";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public void update() {
        if (progress < maxProgress && SawingRecipes.hasResult(processing)) {
            progress += speed();
        } else if (progress >= maxProgress) {
            processing.shrink(1);
        }
    }

    @Override
    protected String namePlate() {
        return "Saw";
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        return side == EnumFacing.DOWN ? EnumKineticConnectionType.SHAFT : EnumKineticConnectionType.NONE;
    }
}
