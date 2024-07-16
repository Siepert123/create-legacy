package com.siepert.createlegacy.tileentity;

import com.siepert.createlegacy.blocks.kinetic.BlockFunnel;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileEntityFunnelAdvanced extends TileEntity implements ITickable {
    int pickupDelay;
    ItemStack filter;

    public TileEntityFunnelAdvanced() {
        filter = ItemStack.EMPTY;
    }

    public void clearFilter() {
        filter = ItemStack.EMPTY;
    }
    public void setFilter(ItemStack newFilter) {
        filter = newFilter;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setInteger("pickupDelay", pickupDelay);

        if (!filter.isEmpty()) {
            NBTTagCompound filterTag = new NBTTagCompound();
            filter.writeToNBT(filterTag);
            compound.setTag("Filter", filterTag);
        }

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey("Filter")) {
            filter = new ItemStack(compound.getCompoundTag("Filter"));
        } else {
            filter = ItemStack.EMPTY;
        }

        pickupDelay = compound.getInteger("pickupDelay");
    }

    @Override
    public void update() {
        EnumFacing facing = world.getBlockState(pos).getValue(BlockFunnel.FACING).toVanillaFacing();
        boolean extracting = world.getBlockState(pos).getValue(BlockFunnel.EXTRACTING);

        BlockPos target = pos.offset(facing.getOpposite());

        TileEntity targetEntity = world.getTileEntity(target);

        boolean alreadyItem = !world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos)).isEmpty();
        if (!world.isRemote) {
            if (extracting && !alreadyItem && pickupDelay == 0) {
                if (targetEntity != null) {
                    if (targetEntity instanceof TileEntityFurnace) {
                        if (!((ISidedInventory) targetEntity).getStackInSlot(2).isEmpty()) {
                            if (filter.isEmpty() || filter.isItemEqual(((ISidedInventory)targetEntity).getStackInSlot(2))) {
                                ItemStack stack = ((ISidedInventory) targetEntity).removeStackFromSlot(2);
                                EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, stack);
                                entityItem.setVelocity(0, 0, 0);
                                world.spawnEntity(entityItem);
                                pickupDelay = 5;
                            }
                        }
                    } else if (targetEntity instanceof ISidedInventory) {
                        for (int i = 0; i < ((ISidedInventory) targetEntity).getSizeInventory(); i++) {
                            if (((ISidedInventory) targetEntity).canExtractItem(i, ((ISidedInventory) targetEntity).getStackInSlot(i), facing)
                                    && !((ISidedInventory) targetEntity).getStackInSlot(i).isEmpty()) {
                                if (filter.isEmpty() || filter.isItemEqual(((ISidedInventory)targetEntity).getStackInSlot(i))) {
                                    ItemStack stack = ((ISidedInventory) targetEntity).removeStackFromSlot(i);
                                    EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, stack);
                                    entityItem.setVelocity(0, 0, 0);
                                    world.spawnEntity(entityItem);
                                    pickupDelay = 5;
                                    break;
                                }
                            }
                        }
                    } else if (targetEntity instanceof IInventory) {
                        for (int i = 0; i < ((IInventory) targetEntity).getSizeInventory(); i++) {
                            if (!((IInventory) targetEntity).getStackInSlot(i).isEmpty()) {
                                if (filter.isEmpty() || filter.isItemEqual(((IInventory)targetEntity).getStackInSlot(i))) {
                                    ItemStack stack = ((IInventory) targetEntity).removeStackFromSlot(i);
                                    EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, stack);
                                    entityItem.setVelocity(0, 0, 0);
                                    world.spawnEntity(entityItem);
                                    pickupDelay = 5;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (!extracting && alreadyItem && pickupDelay == 0) {
                if (targetEntity != null) {
                    EntityItem entityItem = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos)).get(0);
                    ItemStack currentStack = entityItem.getItem();
                    if (targetEntity instanceof ISidedInventory) {
                        for (int i = 0; i < ((ISidedInventory)targetEntity).getSizeInventory(); i++) {
                            if (((ISidedInventory) targetEntity).canInsertItem(i, currentStack, EnumFacing.UP)) {
                                if (((ISidedInventory)targetEntity).getStackInSlot(i).isEmpty()) {
                                    if (filter.isEmpty() || filter.isItemEqual(((ISidedInventory)targetEntity).getStackInSlot(i))) {
                                        ((ISidedInventory) targetEntity).setInventorySlotContents(i, currentStack.copy());
                                        entityItem.setDead();
                                        pickupDelay = 5;
                                        break;
                                    }
                                }
                            }
                        }
                    } else
                    if (targetEntity instanceof IInventory) {
                        for (int i = 0; i < ((IInventory)targetEntity).getSizeInventory(); i++) {
                            if (((IInventory)targetEntity).isItemValidForSlot(i, currentStack)) {
                                if (((IInventory)targetEntity).getStackInSlot(i).isEmpty()) {
                                    if (filter.isEmpty() || filter.isItemEqual(((IInventory)targetEntity).getStackInSlot(i))) {
                                        ((IInventory) targetEntity).setInventorySlotContents(i, currentStack);
                                        entityItem.setDead();
                                        pickupDelay = 5;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (pickupDelay > 0) pickupDelay--;
        }
    }
}
