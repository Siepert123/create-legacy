package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.block.kinetic.BlockRotator;
import net.minecraft.block.state.IBlockState;

public class TileEntityShaftRenderer extends AbstractTileEntityKineticRenderer<TileEntityShaft> {
    public TileEntityShaftRenderer() {
        super();
    }
    @Override
    public void render(TileEntityShaft te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        IBlockState state = te.getWorld().getBlockState(te.getPos());
        spinModel(te, x, y, z, partialTicks, state.getValue(BlockRotator.AXIS), state);
    }
}
