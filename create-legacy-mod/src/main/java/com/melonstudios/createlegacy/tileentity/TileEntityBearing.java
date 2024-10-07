package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityBearing;

public class TileEntityBearing extends AbstractTileEntityBearing {
    @Override
    protected String namePlate() {
        return "Bearing";
    }

    @Override
    public float consumedStressMarkiplier() {
        return 4.0f;
    }
}
