package com.siepert.createlegacy.tileentity;

import com.siepert.createlegacy.blocks.kinetic.BlockChute;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockContainer;
import net.minecraft.entity.item.EntityItem;
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

public class TileEntityChute extends TileEntity implements ITickable {
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

        visualizeStack();

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
                } else if (blockDown instanceof BlockContainer) {
                    //TODO: make this not suck ;-;
                    TileEntity entity = world.getTileEntity(pos.down());
                    if (entity != null) {
                        if (entity instanceof IInventory) {
                            boolean consumed = false;
                            for (int slot = 0; slot < ((IInventory) entity).getSizeInventory(); slot++) {
                                if (!consumed) {
                                    if (((IInventory) entity).getStackInSlot(slot) == ItemStack.EMPTY
                                            || ((IInventory) entity).getStackInSlot(slot).isItemEqual(currentStack)) {
                                        int h = ((IInventory) entity).getStackInSlot(slot).getCount();
                                        int remain = 64 - h;
                                        if (remain > currentStack.getCount()) {
                                            ItemStack stackToDo = currentStack.copy();
                                            stackToDo.setCount(stackToDo.getCount() + h);
                                            ((IInventory) entity).setInventorySlotContents(slot, stackToDo);
                                            currentStack = ItemStack.EMPTY;
                                            consumed = true;
                                        } else {
                                            if (h < 64) {
                                                ItemStack stackToDo = currentStack.copy();
                                                stackToDo.setCount(64);
                                                currentStack.setCount(currentStack.getCount() - remain);
                                                ((IInventory) entity).setInventorySlotContents(slot, stackToDo);
                                            }
                                        }
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
}
