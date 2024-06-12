package com.siepert.createlegacy.tileentity;

import com.siepert.createlegacy.blocks.kinetic.BlockBlazeBurner;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityBlazeBurner extends TileEntity implements ITickable {
    private int remainingBurnTime;
    private enum CookLevel {
        PASSIVE, HEATED, SEETHING
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        compound.setInteger("burnTime", remainingBurnTime);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        remainingBurnTime = compound.getInteger("burnTime");
    }

    /**
     * Adds burn time to the blaze burner.
     */
    public void appendFuel(int amount) {
        NBTTagCompound nbt = getTileData();
        remainingBurnTime = nbt.getInteger("burnTime");
        nbt.setInteger("burnTime", 1000);
        deserializeNBT(nbt);
        markDirty();
    }

    @Override
    public void update() {
        IBlockState myState = world.getBlockState(this.getPos());
        if (myState.getValue(BlockBlazeBurner.STATE).getMeta() != 0) {
            IBlockState myNewState = null;
            CookLevel heat;
            if (remainingBurnTime > 0) {
                if (remainingBurnTime > 1000) heat = CookLevel.SEETHING;
                else heat = CookLevel.HEATED;
                remainingBurnTime -= 1;
            } else heat = CookLevel.PASSIVE;

            switch (heat) {
                case PASSIVE:
                    myNewState = myState.withProperty(BlockBlazeBurner.STATE, BlockBlazeBurner.State.PASSIVE);
                    break;
                case HEATED:
                    myNewState = myState.withProperty(BlockBlazeBurner.STATE, BlockBlazeBurner.State.HEATED);
                    break;
                case SEETHING:
                    myNewState = myState.withProperty(BlockBlazeBurner.STATE, BlockBlazeBurner.State.COPE_SEETHE_MALD);
                    break;
            }
            world.setBlockState(this.getPos(), myNewState, 0);
            world.markBlockRangeForRenderUpdate(this.getPos().down().north().east(), this.getPos().up().south().west());
            markDirty();
        }
    }
}
