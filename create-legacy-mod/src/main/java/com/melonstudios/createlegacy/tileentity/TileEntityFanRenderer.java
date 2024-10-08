package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKineticRenderer;

public class TileEntityFanRenderer extends AbstractTileEntityKineticRenderer<TileEntityFan> {
    @Override
    public void render(TileEntityFan te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        spinModel(te, x, y, z, partialTicks, te.facing().getAxis(), te.getAssociatedPropellerPart());
    }
}
