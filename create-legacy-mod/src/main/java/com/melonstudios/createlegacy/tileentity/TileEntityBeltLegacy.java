package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.block.kinetic.BlockBeltLegacy;
import com.melonstudios.createlegacy.recipe.PressingRecipes;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKinetic;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import com.melonstudios.melonlib.misc.AABB;
import com.melonstudios.melonlib.misc.StackUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.Collections;
import java.util.List;

public class TileEntityBeltLegacy extends AbstractTileEntityKinetic implements IInventory {
    @Override
    protected String namePlate() {
        return "Legacy Belt";
    }

    @Override
    protected void tick() {
        shaftAxis = getState().getValue(BlockBeltLegacy.AXIS) == EnumFacing.Axis.X ? EnumFacing.Axis.Z : EnumFacing.Axis.X;
        hasShaft = getState().getValue(BlockBeltLegacy.HAS_SHAFT);
        itemCache = getItemsOnBelt();

        List<Entity> pushableEntities = world.getEntitiesWithinAABB(Entity.class, aabb.offset(pos));
        for (Entity entity : pushableEntities) {
            if (entity instanceof EntityItem) {
                TileEntity te = world.getTileEntity(pos.up(2));
                if (te instanceof TileEntityPress) {
                    if (PressingRecipes.hasResult(((EntityItem)entity).getItem())) {
                        entity.posX = pos.getX() + 0.5;
                        entity.posY = pos.getY() + (14 / 16.0);
                        entity.posZ = pos.getZ() + 0.5;
                    } else pushEntity(entity);
                } else pushEntity(entity);
            } else pushEntity(entity);
        }
    }

    private static final AxisAlignedBB aabb = AABB.create(0, 12, 0, 16, 16, 16);

    private void pushEntity(Entity entity) {
        if (speed() == 0 || entity.isSneaking()) return;
        EnumFacing facing = EnumFacing.getFacingFromAxis(speed > 0 ? EnumFacing.AxisDirection.POSITIVE : EnumFacing.AxisDirection.NEGATIVE, getState().getValue(BlockBeltLegacy.AXIS));

        float dx = facing.getFrontOffsetX() * Math.abs(speed / 512f) * -1;
        float dz = facing.getFrontOffsetZ() * Math.abs(speed / 512f);

        if (dx != 0) entity.motionX = dx;
        if (dz != 0) entity.motionZ = dz;
    }

    private EnumFacing.Axis shaftAxis = EnumFacing.Axis.X;
    public EnumFacing.Axis getShaftAxis() {
        return shaftAxis;
    }

    private boolean hasShaft = false;
    public boolean hasShaft() {
        return hasShaft;
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        if (side.getAxis() == getState().getValue(BlockBeltLegacy.AXIS)) return EnumKineticConnectionType.BELT;
        return (hasShaft && side.getAxis() == shaftAxis) ? EnumKineticConnectionType.SHAFT : EnumKineticConnectionType.NONE;
    }

    protected List<EntityItem> getItemsOnBelt() {
        return world.getEntitiesWithinAABB(EntityItem.class, aabb.offset(pos));
    }
    protected List<EntityItem> itemCache = Collections.emptyList();

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return itemCache.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return isEmpty() ? ItemStack.EMPTY : itemCache.get(0).getItem();
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return isEmpty() ? ItemStack.EMPTY : itemCache.get(0).getItem().splitStack(count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (isEmpty()) return ItemStack.EMPTY;
        EntityItem item = itemCache.get(0);
        ItemStack stack = item.getItem().copy();
        item.setDead();
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (isEmpty()) {
            if (!world.isRemote) {
                StackUtil.spawnItemNoVelocity(world, pos.getX() + 0.5, pos.getY() + (14 / 16.0), pos.getZ() + 0.5, stack);
            }
        } else {
            itemCache.get(0).setItem(stack);
        }
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

    }

    @Override
    public String getName() {
        return "Legacy Belt";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }
}
