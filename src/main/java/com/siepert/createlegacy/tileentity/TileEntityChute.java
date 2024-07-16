package com.siepert.createlegacy.tileentity;

import com.siepert.createlegacy.blocks.kinetic.BlockChute;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockContainer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class TileEntityChute extends TileEntity implements ITickable, IInventory {
    private ItemStack currentStack;

    private void deNullify() {
        if (currentStack == null) {
            currentStack = ItemStack.EMPTY;
        }
    }

    public ItemStack getCurrentStack() {
        deNullify();
        return currentStack;
    }

    public void handleRemoval() {
        List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos));

        for (EntityItem item : items) {
            if (item.getTags().contains("chuteVisualizerStack")) {
                item.setDead();
            }
        }

        if (!currentStack.isEmpty()) {
            EntityItem drop = new EntityItem(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                    currentStack);
            world.spawnEntity(drop);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        deNullify();

        if (!currentStack.isEmpty()) {
            NBTTagCompound stackNBT = new NBTTagCompound();
            currentStack.writeToNBT(stackNBT);
            compound.setTag("CurrentStack", stackNBT);
        }

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey("CurrentStack")) {
            currentStack = new ItemStack(compound.getCompoundTag("CurrentStack"));
        } else {
            currentStack = ItemStack.EMPTY;
        }
    }

    @Override
    public void update() {

        deNullify();

        //visualizeStack(); TODO: Implement the item renderer because having an actual item there causes issues

        if (!world.isRemote && world.getTotalWorldTime() % 5 == 0) {
            boolean mustCheckForEntityItem = !world.getBlockState(pos.up()).getMaterial().blocksMovement();
            boolean drop = !world.getBlockState(pos.down()).getMaterial().blocksMovement();

            if (drop) {
                if (currentStack != ItemStack.EMPTY) {
                    EntityItem item = new EntityItem(world, pos.getX() + 0.5, pos.getY() - 0.25, pos.getZ() + 0.5, currentStack.copy());
                    item.setVelocity(0.0, -0.5, 0.0);
                    world.spawnEntity(item);
                    currentStack = ItemStack.EMPTY;
                    markDirty();
                }
            } else {
                Block blockDown = world.getBlockState(pos.down()).getBlock();
                if (blockDown == ModBlocks.CHUTE) {
                    if (!((BlockChute) ModBlocks.CHUTE).containsItem(world, pos.down())) {
                        ((TileEntityChute) world.getTileEntity(pos.down())).setCurrentStack(currentStack);
                        currentStack = ItemStack.EMPTY;
                        markDirty();
                    }
                } else {
                    TileEntity entity = world.getTileEntity(pos.down());
                    if (entity != null) {
                        if (entity instanceof ISidedInventory) {
                            for (int i = 0; i < ((ISidedInventory)entity).getSizeInventory(); i++) {
                                if (currentStack != ItemStack.EMPTY) {
                                    if (((ISidedInventory) entity).canInsertItem(i, currentStack, EnumFacing.UP)) {
                                        if (((ISidedInventory)entity).getStackInSlot(i).isEmpty()) {
                                            ((ISidedInventory) entity).setInventorySlotContents(i, currentStack.copy());
                                            currentStack = ItemStack.EMPTY;
                                            break;
                                        } /* else {
                                            if (((ISidedInventory)entity).getStackInSlot(i).getCount()
                                                    <= ((ISidedInventory)entity).getInventoryStackLimit() - currentStack.getCount()) {
                                                ItemStack toInsert = currentStack.copy();
                                                toInsert.setCount(currentStack.getCount() + ((ISidedInventory) entity).getStackInSlot(i).getCount());
                                                ((ISidedInventory) entity).setInventorySlotContents(i, toInsert);
                                                currentStack = ItemStack.EMPTY;
                                                break;
                                            }
                                        } */
                                    }
                                }
                            }
                        } else
                        if (entity instanceof IInventory) {
                            for (int i = 0; i < ((IInventory)entity).getSizeInventory(); i++) {
                                if (currentStack != ItemStack.EMPTY) {
                                    if (((IInventory)entity).isItemValidForSlot(i, currentStack)) {
                                        if (((IInventory)entity).getStackInSlot(i).isEmpty()) {
                                            ((IInventory) entity).setInventorySlotContents(i, currentStack.copy());
                                            currentStack = ItemStack.EMPTY;
                                            break;
                                        } /* else {
                                            if (((IInventory)entity).getStackInSlot(i).getCount()
                                                    <= ((IInventory)entity).getInventoryStackLimit() - currentStack.getCount()) {
                                                ItemStack toInsert = currentStack.copy();
                                                toInsert.setCount(currentStack.getCount() + ((IInventory) entity).getStackInSlot(i).getCount());
                                                ((IInventory) entity).setInventorySlotContents(i, toInsert);
                                                currentStack = ItemStack.EMPTY;
                                                break;
                                            }
                                        } */
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (mustCheckForEntityItem) {
                AxisAlignedBB bb = new AxisAlignedBB(pos.up());
                List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, bb);

                if (!items.isEmpty() && currentStack == ItemStack.EMPTY) {
                    currentStack = items.get(0).getItem();
                    items.get(0).setDead();
                }
            } else {
                if (currentStack == ItemStack.EMPTY) {
                    TileEntity entity = world.getTileEntity(pos.up());
                    if (entity != null) {
                        if (world.getTileEntity(pos.up()) instanceof TileEntityFurnace) {
                            if (!((IInventory) entity).isEmpty()) {
                                    if (!((IInventory) entity).getStackInSlot(2).isEmpty()) {
                                        currentStack = ((IInventory) entity).getStackInSlot(2).copy();
                                        ((IInventory) entity).setInventorySlotContents(2, new ItemStack(Items.AIR));
                                    }
                            }
                        } else if (world.getTileEntity(pos.up()) instanceof ISidedInventory) {
                            if (!((IInventory) entity).isEmpty()) {
                                for (int i = 0; i < ((IInventory) entity).getSizeInventory(); i++) {
                                    if (!((IInventory) entity).getStackInSlot(i).isEmpty()) {
                                        if (((ISidedInventory) entity).canExtractItem(i, ((IInventory) entity).getStackInSlot(i), EnumFacing.DOWN)) {
                                            currentStack = ((IInventory) entity).getStackInSlot(i).copy();
                                            ((IInventory) entity).setInventorySlotContents(i, new ItemStack(Items.AIR));
                                            break;
                                        }
                                    }
                                }
                            }
                        } else if (world.getTileEntity(pos.up()) instanceof IInventory) {
                            if (!((IInventory) entity).isEmpty()) {
                                for (int i = 0; i < ((IInventory) entity).getSizeInventory(); i++) {
                                    if (!((IInventory) entity).getStackInSlot(i).isEmpty()) {
                                        currentStack = ((IInventory) entity).getStackInSlot(i).copy();
                                        ((IInventory) entity).setInventorySlotContents(i, new ItemStack(Items.AIR));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void setCurrentStack(ItemStack stack) {
        currentStack = stack;
        markDirty();
    }

    private void visualizeStack() {
        List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos));

        boolean alreadyVisualized = false;

        for (EntityItem item : items) {
            if (item.getTags().contains("chuteVisualizerStack")) {
                alreadyVisualized = true;
                if (currentStack.isEmpty()) {
                    item.setDead();
                }
            }
        }

        if (!currentStack.isEmpty() && !alreadyVisualized) {
            EntityItem item = new EntityItem(world, pos.getX() + 0.5,
                    pos.getY(), pos.getZ() + 0.5, currentStack);
            item.setInfinitePickupDelay();
            item.setVelocity(0, 0, 0);
            item.noClip = true;
            item.setNoDespawn();
            item.setNoGravity(true);
            item.addTag("chuteVisualizerStack");
            world.spawnEntity(item);
        }
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        deNullify();
        return currentStack.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        deNullify();
        return currentStack;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        deNullify();
        return currentStack.splitStack(count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        deNullify();
        ItemStack stack = currentStack.copy();
        currentStack = ItemStack.EMPTY;
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        currentStack = stack;
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
        return true;
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
        currentStack = ItemStack.EMPTY;
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
