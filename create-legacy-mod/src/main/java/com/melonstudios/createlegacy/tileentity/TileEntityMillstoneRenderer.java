package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.block.BlockRender;
import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKineticRenderer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class TileEntityMillstoneRenderer extends AbstractTileEntityKineticRenderer<TileEntityMillstone> {
    @Override
    public void render(TileEntityMillstone te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        IBlockState state = ModBlocks.RENDER.getDefaultState().withProperty(BlockRender.TYPE, BlockRender.Type.MILLSTONE);

        spinModel(te, x, y, z, partialTicks, EnumFacing.Axis.Y, state);
    }
}
