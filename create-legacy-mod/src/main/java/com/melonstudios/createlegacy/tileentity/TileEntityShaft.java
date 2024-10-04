package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.block.kinetic.BlockEncasedShaft;
import com.melonstudios.createlegacy.block.kinetic.BlockRotator;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKinetic;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class TileEntityShaft extends AbstractTileEntityKinetic {
    public TileEntityShaft() {
        super();
    }
    @Override
    protected String namePlate() {
        return "Shaft";
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == ModBlocks.ROTATOR) {
            if (state.getValue(BlockRotator.AXIS) == side.getAxis()) return EnumKineticConnectionType.SHAFT;
        } else if (state.getBlock() == ModBlocks.SHAFT_ENCASED) {
            if (state.getValue(BlockEncasedShaft.AXIS) == side.getAxis()) return EnumKineticConnectionType.SHAFT;
        }
        return EnumKineticConnectionType.NONE;
    }
}
