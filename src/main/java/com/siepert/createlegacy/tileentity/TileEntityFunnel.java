package com.siepert.createlegacy.tileentity;

import com.siepert.createlegacy.blocks.kinetic.BlockFunnel;
import com.siepert.createlegacy.util.EnumHorizontalFacing;
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

public class TileEntityFunnel extends TileEntity implements ITickable {
    int pickupDelay;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setInteger("pickupDelay", pickupDelay);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        pickupDelay = compound.getInteger("pickupDelay");
    }

    private void checkForRedstone(boolean original) {
        boolean redstone = false;

        for (EnumFacing check : EnumFacing.VALUES) {
            if (world.getRedstonePower(pos.offset(check), check.getOpposite()) > 0) {
                redstone = true;
            }
        }
        if (world.getRedstonePower(pos,
                world.getBlockState(pos).getValue(BlockFunnel.FACING)
                        .toVanillaFacing().getOpposite()) > 0) redstone = true;

        if (redstone != original) {
            BlockFunnel.setState(world, pos, world.getBlockState(pos).getValue(BlockFunnel.EXTRACTING), redstone);
        }
    }

    @Override
    public void update() {
        EnumFacing facing = world.getBlockState(pos).getValue(BlockFunnel.FACING).toVanillaFacing();
        boolean extracting = world.getBlockState(pos).getValue(BlockFunnel.EXTRACTING);

        checkForRedstone(world.getBlockState(pos).getValue(BlockFunnel.DISABLED));

        boolean disabled = world.getBlockState(pos).getValue(BlockFunnel.DISABLED);

        BlockPos target = pos.offset(facing.getOpposite());

        TileEntity targetEntity = world.getTileEntity(target);

        boolean alreadyItem = !world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos)).isEmpty();
        if (!world.isRemote && !disabled) {
            if (extracting && !alreadyItem && pickupDelay == 0) {
                if (targetEntity != null) {
                    if (targetEntity instanceof TileEntityFurnace) {
                        if (!((ISidedInventory) targetEntity).getStackInSlot(2).isEmpty()) {
                            ItemStack stack = ((ISidedInventory) targetEntity).removeStackFromSlot(2);
                            EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, stack);
                            entityItem.setVelocity(0, 0, 0);
                            world.spawnEntity(entityItem);
                            pickupDelay = 20;
                        }
                    } else if (targetEntity instanceof ISidedInventory) {
                        for (int i = 0; i < ((ISidedInventory) targetEntity).getSizeInventory(); i++) {
                            if (((ISidedInventory) targetEntity).canExtractItem(i, ((ISidedInventory) targetEntity).getStackInSlot(i), facing)
                                    && !((ISidedInventory) targetEntity).getStackInSlot(i).isEmpty()) {
                                ItemStack stack = ((ISidedInventory) targetEntity).removeStackFromSlot(i);
                                EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, stack);
                                entityItem.setVelocity(0, 0, 0);
                                world.spawnEntity(entityItem);
                                pickupDelay = 20;
                                break;
                            }
                        }
                    } else if (targetEntity instanceof IInventory) {
                        for (int i = 0; i < ((IInventory) targetEntity).getSizeInventory(); i++) {
                            if (!((IInventory) targetEntity).getStackInSlot(i).isEmpty()) {
                                ItemStack stack = ((IInventory) targetEntity).removeStackFromSlot(i);
                                EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, stack);
                                entityItem.setVelocity(0, 0, 0);
                                world.spawnEntity(entityItem);
                                pickupDelay = 20;
                                break;
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
                                    ((ISidedInventory) targetEntity).setInventorySlotContents(i, currentStack.copy());
                                    entityItem.setDead();
                                    pickupDelay = 20;
                                    break;
                                }
                            }
                        }
                    } else
                    if (targetEntity instanceof IInventory) {
                        for (int i = 0; i < ((IInventory)targetEntity).getSizeInventory(); i++) {
                            if (((IInventory)targetEntity).isItemValidForSlot(i, currentStack)) {
                                if (((IInventory)targetEntity).getStackInSlot(i).isEmpty()) {
                                    ((IInventory) targetEntity).setInventorySlotContents(i, currentStack);
                                    entityItem.setDead();
                                    pickupDelay = 20;
                                    break;
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
