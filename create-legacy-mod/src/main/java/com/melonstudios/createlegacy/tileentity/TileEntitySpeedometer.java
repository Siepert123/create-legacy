package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import net.minecraft.util.EnumFacing;

public class TileEntitySpeedometer extends AbstractTileEntityKinetic {
    @Override
    protected String namePlate() {
        return "Speedometer";
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        return null;
    }
}
