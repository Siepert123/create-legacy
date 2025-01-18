package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKineticRenderer;

public class TileEntityBeltLegacyRenderer extends AbstractTileEntityKineticRenderer<TileEntityBeltLegacy> {
    @Override
    public void render(TileEntityBeltLegacy te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        if (te.hasShaft()) spinShaftModel(te, x, y, z, partialTicks, te.getShaftAxis());
    }
}
