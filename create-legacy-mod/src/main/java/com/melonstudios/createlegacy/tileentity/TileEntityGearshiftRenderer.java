package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKineticRenderer;

public class TileEntityGearshiftRenderer extends AbstractTileEntityKineticRenderer<TileEntityGearshift> {
    @Override
    public void render(TileEntityGearshift te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        if (te.active()) {
            spinModel(te, x, y, z, partialTicks, te.axis(), te.getAssociatedShaftModel(false), te.invertedSide ? -1 : 1);
            spinModel(te, x, y, z, partialTicks, te.axis(), te.getAssociatedShaftModel(true), te.invertedSide ? 1 : -1);
        } else {
            spinShaftModel(te, x, y, z, partialTicks, te.axis());
        }
    }
}
