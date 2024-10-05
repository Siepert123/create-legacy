package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKineticRenderer;

public class TileEntityPressRenderer extends AbstractTileEntityKineticRenderer<TileEntityPress> {
    @Override
    public void render(TileEntityPress te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        spinShaftModel(te, x, y, z, partialTicks, te.axis());

        renderModel(x, y + te.getPressYOffset(partialTicks), z, te.getAssociatedPressPart());
    }
}
