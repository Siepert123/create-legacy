package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.CreateConfig;
import com.melonstudios.createlegacy.util.VersatileDirection;
import net.minecraft.tileentity.TileEntity;

public abstract class AbstractTileEntityKinetic extends TileEntity {
    protected int speed = 64;
    public int speed() {
        return speed;
    }

    public boolean allowConnection(VersatileDirection src) {
        return false;
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return CreateConfig.kineticBlocksRenderDistanceSquared;
    }
}
