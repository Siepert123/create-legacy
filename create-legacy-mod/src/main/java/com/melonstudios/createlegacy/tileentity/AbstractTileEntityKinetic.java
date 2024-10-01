package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.CreateConfig;
import com.melonstudios.createlegacy.util.VersatileDirection;
import net.minecraft.tileentity.TileEntity;

public abstract class AbstractTileEntityKinetic extends TileEntity {
    protected int speed = 0;
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

    protected boolean renderName() {
        return true;
    }
}
