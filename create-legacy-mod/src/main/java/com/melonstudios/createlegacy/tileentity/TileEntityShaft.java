package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.block.kinetic.BlockRotator;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import com.melonstudios.createlegacy.util.NetworkContext;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileEntityShaft extends AbstractTileEntityKinetic {
    @Override
    protected String namePlate() {
        return "Shaft";
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() != ModBlocks.ROTATOR) return EnumKineticConnectionType.NONE;
        if (state.getValue(BlockRotator.AXIS) == side.getAxis()) return EnumKineticConnectionType.SHAFT;
        return EnumKineticConnectionType.NONE;
    }
}
