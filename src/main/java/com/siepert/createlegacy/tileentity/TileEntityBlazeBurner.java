package com.siepert.createlegacy.tileentity;

import com.siepert.createlegacy.blocks.kinetic.BlockBlazeBurner;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityBlazeBurner extends TileEntity implements ITickable {
    private int remainingBurnTime;

    public enum CookLevel {
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
        IBlockState myState = world.getBlockState(this.getPos());
        if (myState.getValue(BlockBlazeBurner.STATE).getMeta() != 0) {
            if (myState.getValue(BlockBlazeBurner.SCHEDULE) == 1) {
                remainingBurnTime = 1200;
            } else if (myState.getValue(BlockBlazeBurner.SCHEDULE) == 2) {
                remainingBurnTime = 3600;
            }
            if (!world.isRemote) {
                IBlockState myNewState = world.getBlockState(pos).withProperty(BlockBlazeBurner.SCHEDULE, 0);
                if (remainingBurnTime > 2400) {
                    remainingBurnTime--;
                    BlockBlazeBurner.setState(myNewState.withProperty(BlockBlazeBurner.STATE, BlockBlazeBurner.State.COPE_SEETHE_MALD), world, pos);
                } else if (remainingBurnTime > 0) {
                    remainingBurnTime--;
                    BlockBlazeBurner.setState(myNewState.withProperty(BlockBlazeBurner.STATE, BlockBlazeBurner.State.HEATED), world, pos);
                } else {
                    BlockBlazeBurner.setState(myNewState.withProperty(BlockBlazeBurner.STATE, BlockBlazeBurner.State.PASSIVE), world, pos);
                }
            }
            world.markBlockRangeForRenderUpdate(pos.east().north().down(), pos.west().south().up());
        }
    }
}
