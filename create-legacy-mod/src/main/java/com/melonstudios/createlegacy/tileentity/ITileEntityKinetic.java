package com.melonstudios.createlegacy.tileentity;

import net.minecraft.util.EnumFacing;

public interface ITileEntityKinetic {
    ConnectionType getConnectionType(EnumFacing dir);

    int getSpeed();
}
