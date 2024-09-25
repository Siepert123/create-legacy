package com.melonstudios.createlegacy.tileentity;

import net.minecraft.util.EnumFacing;

public class TileEntityShaft extends AbstractTileEntityKinetic {
    @Override
    public ConnectionType getConnectionType(EnumFacing dir) {
        return dir.getAxis().isHorizontal() ? ConnectionType.SHAFT : ConnectionType.NONE;
    }
}
