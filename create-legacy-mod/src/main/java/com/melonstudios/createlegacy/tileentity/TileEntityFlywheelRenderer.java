package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKineticRenderer;

public class TileEntityFlywheelRenderer extends AbstractTileEntityKineticRenderer<TileEntityFlywheel> {
    @Override
    public void render(TileEntityFlywheel te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        spinModel(te, x, y, z, partialTicks, te.facing().getAxis(), te.getAssociatedFlywheelPart());
    }
}
