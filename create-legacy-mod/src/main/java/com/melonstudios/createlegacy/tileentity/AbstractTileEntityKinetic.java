package com.melonstudios.createlegacy.tileentity;

import net.minecraft.tileentity.TileEntity;

public abstract class AbstractTileEntityKinetic extends TileEntity implements ITileEntityKinetic {
    int speed = 64;

    @Override
    public int getSpeed() {
        return speed;
    }
}
