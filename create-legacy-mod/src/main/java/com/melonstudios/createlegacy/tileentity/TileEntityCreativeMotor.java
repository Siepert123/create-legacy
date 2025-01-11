package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createapi.network.NetworkContext;
import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.block.kinetic.BlockCreativeMotor;
import com.melonstudios.createlegacy.network.PacketUpdateCreativeMotor;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKinetic;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class TileEntityCreativeMotor extends AbstractTileEntityKinetic {
    @Override
    protected String namePlate() {
        return "Creative motor";
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setInteger("requestedRPM", requestedSpeed);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        requestedSpeed = compound.getInteger("requestedRPM");
    }

    public void updateClients() {
        if (!world.isRemote) {
            PacketUpdateCreativeMotor.sendToNearbyPlayers(this, 1024);
        }
    }

    public EnumFacing facing() {
        return world.getBlockState(pos).getValue(BlockCreativeMotor.FACING);
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        return facing() == side ? connection(1) : connection(0);
    }

    @Override
    protected void tick() {
        if ((world.getTotalWorldTime() & 255) == 255) {
            updateClients();
        }

        if (isUpdated() || generatedRPM() == 0) return;
        NetworkContext context = new NetworkContext(world);
        passNetwork(null, null, context, generatedRPM() < 0);
        context.start();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        enforceNoInversion = true;
        if (world.isRemote) {
            CreateLegacy.getNetworkWrapper().sendToServer(new PacketUpdateCreativeMotor(this));
        }
    }

    @Override
    public float generatedRPM() {
        return requestedSpeed;
    }

    public int requestedSpeed = 256;
    public boolean increaseRequestedSpeed() {
        if (requestedSpeed >= 256) return false;
        requestedSpeed = Math.min(256, requestedSpeed + 16);
        return true;
    }
    public boolean decreaseRequestedSpeed() {
        if (requestedSpeed <= -256) return false;
        requestedSpeed = Math.max(-256, requestedSpeed - 16);
        return true;
    }

    @Override
    public float generatedSUMarkiplier() {
        return Short.MAX_VALUE;
    }

    public IBlockState getAssociatedShaftPart() {
        switch (facing()) {
            case UP: return renderingPart(8);
            case DOWN: return renderingPart(9);
            case NORTH: return renderingPart(10);
            case EAST: return renderingPart(11);
            case SOUTH: return renderingPart(12);
            case WEST: return renderingPart(13);
        }
        return renderingPart(10);
    }
}
