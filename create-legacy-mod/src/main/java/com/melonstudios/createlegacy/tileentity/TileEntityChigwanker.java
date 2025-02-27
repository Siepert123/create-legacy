package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKinetic;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import net.minecraft.util.EnumFacing;

public class TileEntityChigwanker extends AbstractTileEntityKinetic {
    @Override
    protected String namePlate() {
        return "Chigwanker";
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        return EnumKineticConnectionType.SHAFT;
    }
}
