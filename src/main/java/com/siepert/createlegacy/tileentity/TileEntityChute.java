package com.siepert.createlegacy.tileentity;

import com.siepert.createlegacy.blocks.kinetic.BlockChute;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
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
                if (world.getBlockState(pos.down()).getBlock() == ModBlocks.CHUTE) {
                    if (!((BlockChute) ModBlocks.CHUTE).containsItem(world, pos.down())) {
                        ((TileEntityChute) world.getTileEntity(pos.down())).setCurrentStack(currentStack);
                        currentStack = ItemStack.EMPTY;
                        markDirty();
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
            }
        }
    }

    public void setCurrentStack(ItemStack stack) {
        currentStack = stack;
        markDirty();
    }
}
