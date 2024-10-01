package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.block.BlockRender;
import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.block.kinetic.BlockWaterWheel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class TileEntityWaterWheelRenderer extends AbstractTileEntityKineticRenderer<TileEntityWaterWheel> {
    @Override
    public void render(TileEntityWaterWheel te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        IBlockState state = te.getWorld().getBlockState(te.getPos());

        EnumFacing.Axis axis = state.getValue(BlockWaterWheel.AXIS);

        IBlockState render = ModBlocks.RENDER.getDefaultState().withProperty(BlockRender.TYPE,
                axis == EnumFacing.Axis.X ? BlockRender.Type.WATERWHEEL_X
                : BlockRender.Type.WATERWHEEL_Z);

        spinModel(te, x, y, z, partialTicks, axis, render);
    }
}
