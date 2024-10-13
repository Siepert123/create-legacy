package com.melonstudios.createlegacy.tileentity;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TileEntityChute extends TileEntity implements IInventory, ITickable {
    protected ItemStack stack = ItemStack.EMPTY;
    protected int transportCooldown = 0;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setInteger("transportCooldown", transportCooldown);

        if (!stack.isEmpty()) {
            NBTTagCompound nbt = new NBTTagCompound();
            stack.writeToNBT(nbt);
            compound.setTag("Stack", nbt);
        }

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        transportCooldown = compound.getInteger("transportCooldown");

        if (compound.hasKey("Stack")) {
            stack = new ItemStack(compound.getCompoundTag("Stack"));
        }
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return index == 0 ? stack : ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return index == 0 ? stack.splitStack(count) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (index == 0) {
            try {
                return stack;
            } finally {
                stack = ItemStack.EMPTY;
            }
        } return ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index == 0) this.stack = stack;
    }

    @Override
    public int getInventoryStackLimit() {
        return 16;
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
        return index == 0 && (this.stack.isEmpty() || stack.isItemEqual(this.stack)) && this.stack.getCount() < getInventoryStackLimit();
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0: return transportCooldown;
            default: return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0: transportCooldown = value;
        }
    }

    @Override
    public int getFieldCount() {
        return 1;
    }

    @Override
    public void clear() {
        stack = ItemStack.EMPTY;
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            if (transportCooldown > 0) transportCooldown--;
            else {
                if (!stack.isEmpty()) {
                    TileEntity entity = world.getTileEntity(pos.down());
                    if (entity instanceof IInventory) {
                        if (entity instanceof ISidedInventory) {
                            ISidedInventory inventory = (ISidedInventory) entity;
                            for (int i : inventory.getSlotsForFace(EnumFacing.UP)) {
                                if (inventory.canInsertItem(i, stack, EnumFacing.UP) && inventory.isItemValidForSlot(i, stack)
                                        && (inventory.getStackInSlot(i).isItemEqual(stack) || inventory.getStackInSlot(i).isEmpty())) {
                                    if (inventory.getStackInSlot(i).isEmpty())
                                        inventory.setInventorySlotContents(i, stack);
                                    else {
                                        inventory.getStackInSlot(i).grow(stack.getCount());
                                    }
                                    stack = ItemStack.EMPTY;
                                    transportCooldown = 10;
                                    break;
                                }
                            }
                        } else {
                            IInventory inventory = (IInventory) entity;

                            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                                if (inventory.isItemValidForSlot(i, stack)
                                        && (inventory.getStackInSlot(i).isItemEqual(stack) || inventory.getStackInSlot(i).isEmpty())) {
                                    if (inventory.getStackInSlot(i).isEmpty())
                                        inventory.setInventorySlotContents(i, stack);
                                    else {
                                        inventory.getStackInSlot(i).grow(stack.getCount());
                                    }
                                    stack = ItemStack.EMPTY;
                                    transportCooldown = 10;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (stack.isEmpty()) {
                    TileEntity entity = world.getTileEntity(pos.up());
                    if (entity instanceof IInventory) {
                        if (entity instanceof ISidedInventory) {
                            ISidedInventory inventory = (ISidedInventory) entity;

                            for (int i : inventory.getSlotsForFace(EnumFacing.DOWN)) {
                                if (inventory.canExtractItem(i, inventory.getStackInSlot(i), EnumFacing.DOWN) && !inventory.getStackInSlot(i).isEmpty()) {
                                    stack = inventory.removeStackFromSlot(i);
                                    transportCooldown = 10;
                                    break;
                                }
                            }
                        } else {
                            IInventory inventory = (IInventory) entity;

                            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                                if (!inventory.getStackInSlot(i).isEmpty()) {
                                    stack = inventory.removeStackFromSlot(i);
                                    transportCooldown = 10;
                                    break;
                                }
                            }
                        }
                    } else {
                        List<EntityItem> droppedItems = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.up()));

                        if (!droppedItems.isEmpty()) {
                            stack = droppedItems.get(0).getItem().copy();
                            droppedItems.get(0).setDead();
                            transportCooldown = 10;
                        }
                    }
                }
            }
            markDirty();
        }
    }

    @Override
    public String getName() {
        return "Chute";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }
}
