package com.siepert.createlegacy.tileentity;

import com.siepert.createlegacy.blocks.kinetic.BlockBlazeBurner;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IInteractionObject;

public class TileEntityBlazeBurner extends TileEntity implements ITickable {
    private int remainingBurnTime;

    private enum CookLevel {
        PASSIVE, HEATED, SEETHING
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("burnTime", remainingBurnTime);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        remainingBurnTime = compound.getInteger("burnTime");
    }

    @Override
    public void update() {
        NBTTagCompound myData = serializeNBT();
        readFromNBT(myData);
        IBlockState myState = world.getBlockState(this.getPos());
        if (myState.getValue(BlockBlazeBurner.STATE).getMeta() != 0) {
            if (myState.getValue(BlockBlazeBurner.SCHEDULE) == 1) {
                remainingBurnTime = 800;
                world.setBlockState(pos, myState.withProperty(BlockBlazeBurner.STATE, BlockBlazeBurner.State.HEATED));
            } else if (myState.getValue(BlockBlazeBurner.SCHEDULE) == 2) {
                remainingBurnTime = 1500;
                world.setBlockState(pos, myState.withProperty(BlockBlazeBurner.STATE, BlockBlazeBurner.State.COPE_SEETHE_MALD));
            }
        }

        deserializeNBT(writeToNBT(myData));
    }
}
