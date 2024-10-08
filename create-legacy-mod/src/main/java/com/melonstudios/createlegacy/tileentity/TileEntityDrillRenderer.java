package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKineticRenderer;

public class TileEntityDrillRenderer extends AbstractTileEntityKineticRenderer<TileEntityDrill> {
    @Override
    public void render(TileEntityDrill te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        spinModel(te, x, y, z, partialTicks, te.facing().getAxis(), te.getAssociatedDrillPart());
    }
}
