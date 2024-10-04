package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.block.kinetic.BlockRotator;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKineticRenderer;
import net.minecraft.block.state.IBlockState;

public class TileEntityCogRenderer extends AbstractTileEntityKineticRenderer<TileEntityCog> {
    public TileEntityCogRenderer() {
        super();
    }
    @Override
    public void render(TileEntityCog te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        IBlockState state = te.getWorld().getBlockState(te.getPos());
        spinModel(te, x, y, z, partialTicks, state.getValue(BlockRotator.AXIS), state);
    }
}
