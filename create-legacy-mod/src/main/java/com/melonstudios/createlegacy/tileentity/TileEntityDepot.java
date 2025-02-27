package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.network.PacketUpdateDepot;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class TileEntityDepot extends TileEntity implements ISidedInventory, ITickable {
    protected ItemStack stack = ItemStack.EMPTY;
    protected ItemStack output = ItemStack.EMPTY;
    protected ItemStack output2 = ItemStack.EMPTY;
    public ItemStack getStack() {
        return stack;
    }
    public ItemStack getOutput() {
        return output;
    }
    public ItemStack getOutput2() {
        return output2;
    }
    public void setStack(ItemStack stack) {
        this.stack = stack;
        processingProgress = 0;
        markDirty();
    }
    public void setOutput(ItemStack stack) {
        output = stack;
        markDirty();
    }
    public void setOutput2(ItemStack stack) {
        output2 = stack;
        markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey("Stack")) {
            stack = new ItemStack(compound.getCompoundTag("Stack"));
        }
        if (compound.hasKey("Output")) {
            output = new ItemStack(compound.getCompoundTag("Output"));
            output.setCount(compound.getInteger("outputOverride"));
        }
        if (compound.hasKey("Output2")) {
            output2 = new ItemStack(compound.getCompoundTag("Output2"));
            output2.setCount(compound.getInteger("outputOverride2"));
        }
        processingProgress = compound.getInteger("processingProgress");
    }

    public int processingProgress = 0;

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
            compound.setInteger("outputOverride", output.getCount());
        }
        if (!output2.isEmpty()) {
            NBTTagCompound nbt = new NBTTagCompound();
            output2.writeToNBT(nbt);
            compound.setTag("Output2", nbt);
            compound.setInteger("outputOverride2", output2.getCount());
        }
        compound.setInteger("processingProgress", processingProgress);

        return compound;
    }

    @Override
    public int getSizeInventory() {
        return 64;
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty() && output.isEmpty() && output2.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        markDirty();
        if (index == 0) return stack;
        if (index == 1) return output;
        if (index == 2) return output2;
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        markDirty();
        processingProgress = 0;
        return index == 0 ? stack.splitStack(count) : (index == 1 ? output.splitStack(count) : output2.splitStack(count));
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack = index == 0 ? this.stack : (index == 1 ? output : output2);
        if (index == 0) this.stack = ItemStack.EMPTY;
        else if (index == 1) this.output = ItemStack.EMPTY;
        else this.output2 = ItemStack.EMPTY;
        markDirty();
        processingProgress = 0;
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index == 0) this.stack = stack;
        else if (index == 1) this.output = stack;
        else this.output2 = stack;
        processingProgress = 0;
        markDirty();
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
    public void markDirty() {
        super.markDirty();
        if (!world.isRemote) PacketUpdateDepot.sendToPlayersNearby(this, 64);
    }

    @Override
    public void clear() {
        setStack(ItemStack.EMPTY);
        setOutput(ItemStack.EMPTY);
        setOutput2(ItemStack.EMPTY);
        processingProgress = 0;
        markDirty();
    }

    @Override
    public String getName() {
        return "Depot";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[] {0, 1, 2};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return index == 0;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return index == 1 || index == 2;
    }
    protected boolean notify = false;
    @Override
    public void update() {
        if (!world.isRemote && notify) {
            if ((world.getTotalWorldTime() & 0xf) == 0xf) PacketUpdateDepot.sendToPlayersNearby(this, 32);
        }
        if (!world.isRemote && getStack().getCount() < getStack().getMaxStackSize()) {
            if (world.getBlockState(pos.up()).getBlock().isAir(world.getBlockState(pos.up()), world, pos.up())) {
                List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class,
                        new AxisAlignedBB(
                                pos.getX(), pos.getY() + 0.8, pos.getZ(),
                                pos.getX() + 1, pos.getY() + 1.2, pos.getZ() + 1
                        )
                );
                if (!items.isEmpty()) {
                    if (stack.isEmpty()) {
                        EntityItem item = items.get(0);
                        setStack(item.getItem().copy());
                        item.setDead();
                    } else {
                        for (EntityItem item : items) {
                            if (item.getItem().isItemEqual(getStack())) {
                                getStack().grow(item.getItem().splitStack(stack.getMaxStackSize() - getStack().getCount()).getCount());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onLoad() {
        notify = true;
    }
}
