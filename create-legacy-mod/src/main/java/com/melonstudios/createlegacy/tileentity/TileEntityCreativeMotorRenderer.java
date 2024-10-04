package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKineticRenderer;
import net.minecraft.block.state.IBlockState;

public class TileEntityCreativeMotorRenderer extends AbstractTileEntityKineticRenderer<TileEntityCreativeMotor> {
    @Override
    public void render(TileEntityCreativeMotor te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        IBlockState shaft = te.getAssociatedShaftPart();

        spinModel(te, x, y, z, partialTicks, te.facing().getAxis(), shaft);
    }
}
