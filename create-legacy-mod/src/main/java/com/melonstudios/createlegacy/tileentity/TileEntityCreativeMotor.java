package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createapi.network.NetworkContext;
import com.melonstudios.createlegacy.block.kinetic.BlockCreativeMotor;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class TileEntityCreativeMotor extends AbstractTileEntityKinetic {
    @Override
    protected String namePlate() {
        return "Creative motor";
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
        if (isUpdated() || generatedRPM() == 0) return;
        NetworkContext context = new NetworkContext(world);
        passNetwork(null, null, context, false);
        context.start();
    }

    @Override
    public float generatedRPM() {
        return 256.0f;
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
