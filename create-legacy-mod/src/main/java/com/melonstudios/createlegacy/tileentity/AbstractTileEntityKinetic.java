package com.melonstudios.createlegacy.tileentity;

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
}
