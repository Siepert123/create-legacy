package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKineticRenderer;
import net.minecraft.util.EnumFacing;

public class TileEntityGearboxRenderer extends AbstractTileEntityKineticRenderer<TileEntityGearbox> {
    @Override
    public void render(TileEntityGearbox te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        EnumFacing.Axis axis = te.axis();
        if (axis == EnumFacing.Axis.Y) {

        }
    }
}
