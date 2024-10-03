package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.block.BlockRender;
import com.melonstudios.createlegacy.block.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

public abstract class AbstractTileEntityBearingRenderer<T extends AbstractTileEntityBearing> extends AbstractTileEntityKineticRenderer<T> {
    @Override
    public void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        IBlockState bearingPart = te.getAssociatedBearingPart();
        IBlockState shaftPart = te.getAssociatedShaftPart();
        EnumFacing.Axis axis = te.facing().getAxis();

        if (te.shouldRenderSpinning()) {
            IBlockState structure = te.getStructure();
            spinModel(te, x, y, z, partialTicks, axis, bearingPart);
            if (structure != null) {
                if (structure.getBlock() != Blocks.AIR) {
                    double[] place = offsetPos(x, y, z, te.facing());
                    spinModel(te, place[0], place[1], place[2], partialTicks, axis, structure);
                }
            }
        } else {
            rotateModel(0.0f, x, y, z, axis, bearingPart);
        }
    }

    protected double[] offsetPos(double x, double y, double z, EnumFacing dir) {
        return new double[] {
                x + dir.getFrontOffsetX(),
                y + dir.getFrontOffsetY(),
                z + dir.getFrontOffsetZ()
        };
    }
}
