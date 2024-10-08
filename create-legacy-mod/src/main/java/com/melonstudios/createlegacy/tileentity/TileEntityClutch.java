package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.block.kinetic.BlockKineticUtility;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKinetic;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import net.minecraft.util.EnumFacing;

public class TileEntityClutch extends AbstractTileEntityKinetic {
    @Override
    protected String namePlate() {
        return "Clutch";
    }

    public final EnumFacing.Axis axis() {
        return getState().getValue(BlockKineticUtility.AXIS);
    }
    public final boolean active() {
        return getState().getValue(BlockKineticUtility.ACTIVE);
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        if (side.getAxis() != axis()) return connection(0);
        if (!getState().getValue(BlockKineticUtility.SHIFT)) {
            if (getState().getValue(BlockKineticUtility.ACTIVE)) {
                return connection(0);
            }
        }
        return connection(1);
    }

    @Override
    protected void tick() {
        boolean red = false;
        for (EnumFacing dir : EnumFacing.VALUES) {
            if (world.getRedstonePower(pos, dir) > 0) red = true;
        }
        if (active() != red) {
            world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockKineticUtility.ACTIVE, red), 3);
            validate();
            world.setTileEntity(pos, this);
        }
    }
}
