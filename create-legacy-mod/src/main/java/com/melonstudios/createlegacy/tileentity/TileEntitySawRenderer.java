package com.melonstudios.createlegacy.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TileEntitySawRenderer extends AbstractTileEntityKineticRenderer<TileEntitySaw> {
    public TileEntitySawRenderer() {
        super();
    }

    @Override
    public void render(TileEntitySaw te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        BlockPos pos = te.getPos();
        IBlockState state = te.getWorld().getBlockState(pos);

        spinModel(te, x, y, z, partialTicks, EnumFacing.Axis.X, state);
    }
}
