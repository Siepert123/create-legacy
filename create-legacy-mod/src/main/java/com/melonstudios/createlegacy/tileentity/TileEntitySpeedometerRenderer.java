package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.block.BlockRender;
import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.block.kinetic.BlockNetworkInspector;
import com.melonstudios.createlegacy.block.kinetic.BlockRotator;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKineticRenderer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class TileEntitySpeedometerRenderer extends AbstractTileEntityKineticRenderer<TileEntitySpeedometer> {
    @Override
    public void render(TileEntitySpeedometer te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        IBlockState myState = te.getState();

        EnumFacing.Axis axis = myState.getValue(BlockNetworkInspector.AXIS);
        IBlockState shaft = ModBlocks.ROTATOR.getDefaultState()
                        .withProperty(BlockRotator.VARIANT, BlockRotator.Variant.SHAFT)
                                .withProperty(BlockRotator.AXIS, axis);
        IBlockState gauge1 = ModBlocks.RENDER.getDefaultState().withProperty(BlockRender.TYPE, axis == EnumFacing.Axis.X ? BlockRender.Type.GAUGE_E
                : BlockRender.Type.GAUGE_N);
        IBlockState gauge2 = ModBlocks.RENDER.getDefaultState().withProperty(BlockRender.TYPE, axis == EnumFacing.Axis.X ? BlockRender.Type.GAUGE_W
                : BlockRender.Type.GAUGE_S);

        spinModel(te, x, y, z, partialTicks, axis, shaft);

        rotateModel(te.getDegreesPart(true), x, y, z, axis == EnumFacing.Axis.X ? EnumFacing.Axis.Z : EnumFacing.Axis.X, gauge1);
        rotateModel(te.getDegreesPart(false), x, y, z, axis == EnumFacing.Axis.X ? EnumFacing.Axis.Z : EnumFacing.Axis.X, gauge2);
    }
}
