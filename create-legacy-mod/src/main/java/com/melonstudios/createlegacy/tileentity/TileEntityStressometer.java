package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import com.melonstudios.createlegacy.util.INetworkLogger;
import net.minecraft.util.EnumFacing;

public class TileEntityStressometer extends AbstractTileEntityKinetic implements INetworkLogger {
    @Override
    protected String namePlate() {
        return "Stressometer";
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        return null;
    }

    protected int lastSU = 0;
    protected int lastMaxSU = 0;

    @Override
    public void setSU(int su) {
        lastSU = su;
    }

    @Override
    public void setMaxSU(int su) {
        lastMaxSU = su;
    }
}
