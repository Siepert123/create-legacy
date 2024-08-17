package com.siepert.createlegacy.tileentity;

import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.CreateLegacyModData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.ArrayList;
import java.util.List;

public class TileEntityBasin extends TileEntity implements IInventory {
    private final AxisAlignedBB itemBB;
    public TileEntityBasin() {
        super();
        itemBB = new AxisAlignedBB(pos);
    }

    private List<EntityItem> getOutputItems() {
        AxisAlignedBB itemBB = new AxisAlignedBB(pos);
        List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, itemBB);

        List<EntityItem> returnList = new ArrayList<>();

        for (EntityItem item : items) {
            if (item.getTags().contains(CreateLegacyModData.ITEM_OUTPUT_TAG)) {
                returnList.add(item);
            }
        }
        return returnList;
    }
    @Override
    public int getSizeInventory() {
        return getOutputItems().size();
    }

    @Override
    public boolean isEmpty() {
        return getOutputItems().isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (!(index > getOutputItems().size() - 1)) {
            return getOutputItems().get(index).getItem();
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (!(index > getOutputItems().size() - 1)) {
            return getOutputItems().get(index).getItem().splitStack(count);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (!(index > getOutputItems().size() - 1)) {
            List<EntityItem> items = getOutputItems();
            ItemStack stack = items.get(index).getItem().copy();
            items.get(index).setDead();
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        EntityItem item = new EntityItem(world, pos.getX() + 0.5,
                pos.getY() + 0.5, pos.getZ() + 0.5, stack);
        item.setNoDespawn();
        item.setVelocity(0, 0, 0);
        item.setPickupDelay(100);
        world.spawnEntity(item);
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
        return false;
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
        for (EntityItem item : getOutputItems()) {
            item.setDead();
        }
    }

    @Override
    public String getName() {
        return "Basin";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }
}
