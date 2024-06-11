package com.siepert.createlegacy.tileentity;

import com.siepert.createlegacy.blocks.kinetic.BlockBlazeBurner;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityBlazeBurner extends TileEntity implements ITickable {
    private int remainingBurnTime;
    private int remainingSeetheTime;
    private enum CookLevel {
        PASSIVE, HEATED, SEETHING
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        remainingBurnTime = compound.getInteger("burnTime");
        remainingSeetheTime = compound.getInteger("seetheTime");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("burnTime", remainingBurnTime);
        compound.setInteger("seetheTime", remainingSeetheTime);
        return super.writeToNBT(compound);
    }

    /**
     * Adds burn time to the blaze burner.
     * @param amount The amount of ticks that must be added to the burn time.
     * @return True if the fuel must be consumed.
     */
    public boolean appendFuel(int amount) {
        if (remainingSeetheTime == 0) {
            if (remainingBurnTime < 1000) {
                remainingBurnTime += amount;
            }
            if (remainingBurnTime > 1000) remainingBurnTime = 1000;
            return true;
        } return false;
    }

    /**
     * Makes the blaze go into cope seethe mald mode (superheating).
     * @return True if the blaze cake must be consumed.
     */
    public boolean makeSeethe() {
        if (remainingSeetheTime > 0) return false;
        remainingSeetheTime = 500;
        remainingBurnTime = 1000;
        return true;
    }

    @Override
    public void update() {
        IBlockState myState = world.getBlockState(pos);
        IBlockState myNewState = null;
        CookLevel heat = CookLevel.PASSIVE;
        boolean didBurn = false;
        if (remainingSeetheTime > 0) {
            remainingSeetheTime--;
            heat = CookLevel.HEATED;
            didBurn = true;
        }
        if (remainingBurnTime > 0 && !didBurn) {
            remainingBurnTime--;
            heat = CookLevel.SEETHING;
            didBurn = true;
        }

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
        world.setBlockState(pos, myNewState, 0);
        world.markBlockRangeForRenderUpdate(pos.down().north().east(), pos.up().south().west());
    }


}
