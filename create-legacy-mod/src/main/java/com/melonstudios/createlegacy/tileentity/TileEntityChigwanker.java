package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import com.melonstudios.createlegacy.util.NetworkContext;
import net.minecraft.util.EnumFacing;

public class TileEntityChigwanker extends AbstractTileEntityKinetic {
    @Override
    protected String namePlate() {
        return "Chigwanker";
    }

    @Override
    public void frontier(EnumFacing src, NetworkContext context, boolean inv, EnumKineticConnectionType connectionType) {
        context.setInfiniteSU();
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        return EnumKineticConnectionType.SHAFT;
    }
}
