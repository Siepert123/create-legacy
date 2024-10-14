package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createapi.kinetic.IStateFindable;
import com.melonstudios.createlegacy.block.BlockFunnel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class TileEntityFunnel extends TileEntity implements ITickable, IStateFindable {

    protected int transferCooldown = 0;
    public int getTransferCooldown() {
        return transferCooldown;
    }
    public void setTransferCooldown(int cooldown) {
        transferCooldown = cooldown;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        transferCooldown = compound.getInteger("transferCooldown");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setInteger("transferCooldown", transferCooldown);

        return compound;
    }

    @Override
    public IBlockState getState() {
        return world.getBlockState(pos);
    }
    public EnumFacing facing() {
        return getState().getValue(BlockFunnel.FACING);
    }
    public boolean enabled() {
        return !getState().getValue(BlockFunnel.DISABLED);
    }

    @Override
    public void update() {
        if (enabled()) {
            if (transferCooldown > 0) transferCooldown--;
            else {
                List<EntityItem> groundItems = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos));

                if (!groundItems.isEmpty()) {
                    TileEntity entity = world.getTileEntity(pos.offset(facing().getOpposite()));
                    if (entity instanceof IInventory) {
                        if (entity instanceof ISidedInventory) {
                            ISidedInventory inventory = (ISidedInventory) entity;
                            for (int i : inventory.getSlotsForFace(facing())) {
                                for (EntityItem item : groundItems) {
                                    ItemStack stack = item.getItem();

                                    if (inventory.canInsertItem(i, stack, EnumFacing.UP) && inventory.isItemValidForSlot(i, stack)
                                            && (inventory.getStackInSlot(i).isItemEqual(stack) || inventory.getStackInSlot(i).isEmpty())
                                            && inventory.getStackInSlot(i).getCount() <= (inventory.getInventoryStackLimit() - stack.getCount())) {
                                        if (inventory.getStackInSlot(i).isEmpty()) {
                                            inventory.setInventorySlotContents(i, stack.copy());
                                        } else {
                                            inventory.getStackInSlot(i).grow(stack.getCount());
                                        }
                                        item.setItem(ItemStack.EMPTY);
                                        item.setDead();
                                        transferCooldown = 10;
                                        break;
                                    }
                                }
                            }
                        } else {
                            IInventory inventory = (IInventory) entity;

                            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                                for (EntityItem item : groundItems) {
                                    ItemStack stack = item.getItem();

                                    if (inventory.isItemValidForSlot(i, stack)
                                            && (inventory.getStackInSlot(i).isItemEqual(stack) || inventory.getStackInSlot(i).isEmpty())
                                            && inventory.getStackInSlot(i).getCount() <= (inventory.getInventoryStackLimit() - stack.getCount())) {
                                        if (inventory.getStackInSlot(i).isEmpty()) {
                                            inventory.setInventorySlotContents(i, stack.copy());
                                        } else {
                                            inventory.getStackInSlot(i).grow(stack.getCount());
                                        }
                                        item.setItem(ItemStack.EMPTY);
                                        item.setDead();
                                        transferCooldown = 10;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            transferCooldown = 0;
        }
        markDirty();
    }
}
