package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createapi.network.NetworkContext;
import com.melonstudios.createlegacy.block.kinetic.BlockHandCrank;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKinetic;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class TileEntityHandCrank extends AbstractTileEntityKinetic {
    @Override
    protected String namePlate() {
        return "Handcrank";
    }

    protected int ticks = 0;
    public void resetTimer() {
        ticks = 10;
    }
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setInteger("ticks", ticks);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        ticks = compound.getInteger("ticks");
    }

    @Override
    protected void tick() {
        if (ticks > 0) ticks--;

        if (isUpdated() || generatedRPM() == 0) return;
        NetworkContext context = new NetworkContext(world);

        passNetwork(null, null, context, false);

        context.start();
    }

    @Override
    public float generatedRPM() {
        return ticks > 0 ? 16.0f : 0;
    }

    @Override
    public float generatedSUMarkiplier() {
        return 16.0f;
    }

    public EnumFacing output() {
        return world.getBlockState(pos).getValue(BlockHandCrank.FACING).getOpposite();
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        return side == output() ? connection(1) : connection(0);
    }
}
