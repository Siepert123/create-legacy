package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.network.PacketRequestUpdateDepot;
import com.melonstudios.createlegacy.network.PacketUpdateDepot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.List;

public class TileEntityDepot extends TileEntity implements ISidedInventory, ITickable {
    protected ItemStack stack = ItemStack.EMPTY;
    protected ItemStack output = ItemStack.EMPTY;
    public ItemStack getStack() {
        return stack;
    }
    public ItemStack getOutput() {
        return output;
    }
    public void setStack(ItemStack stack) {
        this.stack = stack;
        markDirty();
    }
    public void setOutput(ItemStack stack) {
        output = stack;
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
        }
    }

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
        }

        return compound;
    }

    @Override
    public int getSizeInventory() {
        return 64;
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty() && output.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        markDirty();
        return index == 0 ? stack : output;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        markDirty();
        return index == 0 ? stack.splitStack(count) : output.splitStack(count);

    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack = index == 0 ? this.stack : output;
        if (index == 0) this.stack = ItemStack.EMPTY;
        else this.output = ItemStack.EMPTY;
        markDirty();
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index == 0) this.stack = stack;
        else this.output = stack;
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
        notify = true;
    }

    @Override
    public void clear() {
        setStack(ItemStack.EMPTY);
        setOutput(ItemStack.EMPTY);
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
        return new int[] {0, 1};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return index == 0;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return index == 1;
    }
    protected boolean notify = false;
    @Override
    public void update() {
        if (!world.isRemote && notify) {
            notify = false;
            List<EntityPlayer> players =
                    FMLCommonHandler.instance().getMinecraftServerInstance()
                            .getWorld(world.provider.getDimension())
                            .getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos.add(-16, -16, -16),
                                    pos.add(16, 16, 16)));

            for (EntityPlayer player : players) {
                if (player instanceof EntityPlayerMP) {
                    CreateLegacy.networkWrapper.sendTo(new PacketUpdateDepot(this), (EntityPlayerMP) player);
                }
            }
        }
    }

    @Override
    public void onLoad() {
        notify = true;
    }
}
