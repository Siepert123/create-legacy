package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.block.kinetic.BlockEncasedShaft;
import com.melonstudios.createlegacy.block.kinetic.BlockRotator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class TileEntityShaftRenderer extends AbstractTileEntityKineticRenderer<TileEntityShaft> {
    public TileEntityShaftRenderer() {
        super();
    }
    @Override
    public void render(TileEntityShaft te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        IBlockState state = te.getWorld().getBlockState(te.getPos());
        EnumFacing.Axis axis = state.getBlock() instanceof BlockRotator ? state.getValue(BlockRotator.AXIS)
                : state.getValue(BlockEncasedShaft.AXIS);
        spinShaftModel(te, x, y, z, partialTicks, axis);
    }
}
