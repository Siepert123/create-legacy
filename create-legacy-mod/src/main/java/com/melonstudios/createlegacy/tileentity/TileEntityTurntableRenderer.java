package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKineticRenderer;
import net.minecraft.util.EnumFacing;

public class TileEntityTurntableRenderer extends AbstractTileEntityKineticRenderer<TileEntityTurntable> {
    @Override
    public void render(TileEntityTurntable te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        spinModel(te, x, y, z, partialTicks, EnumFacing.Axis.Y, te.getState());
    }
}
