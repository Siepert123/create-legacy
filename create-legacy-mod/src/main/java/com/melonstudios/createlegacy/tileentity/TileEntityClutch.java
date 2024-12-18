package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createapi.kinetic.IKineticTileEntity;
import com.melonstudios.createlegacy.block.BlockRender;
import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.block.kinetic.BlockKineticUtility;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKinetic;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileEntityClutch extends AbstractTileEntityKinetic {
    @Override
    protected String namePlate() {
        return "Clutch";
    }

    public final EnumFacing.Axis axis() {
        return getState().getValue(BlockKineticUtility.AXIS);
    }
    public final boolean active() {
        return getState().getValue(BlockKineticUtility.ACTIVE);
    }


    public final IBlockState getAssociatedShaftModel(boolean b) {
        final IBlockState render = ModBlocks.RENDER.getDefaultState();
        if (b) {
            switch (EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, axis())) {
                case UP: return render.withProperty(BlockRender.TYPE, BlockRender.Type.SHAFT_U);
                case DOWN: return render.withProperty(BlockRender.TYPE, BlockRender.Type.SHAFT_D);
                case NORTH: return render.withProperty(BlockRender.TYPE, BlockRender.Type.SHAFT_N);
                case EAST: return render.withProperty(BlockRender.TYPE, BlockRender.Type.SHAFT_E);
                case SOUTH: return render.withProperty(BlockRender.TYPE, BlockRender.Type.SHAFT_S);
                case WEST: return render.withProperty(BlockRender.TYPE, BlockRender.Type.SHAFT_W);
            }
        }
        switch (EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.NEGATIVE, axis())) {
            case UP: return render.withProperty(BlockRender.TYPE, BlockRender.Type.SHAFT_U);
            case DOWN: return render.withProperty(BlockRender.TYPE, BlockRender.Type.SHAFT_D);
            case NORTH: return render.withProperty(BlockRender.TYPE, BlockRender.Type.SHAFT_N);
            case EAST: return render.withProperty(BlockRender.TYPE, BlockRender.Type.SHAFT_E);
            case SOUTH: return render.withProperty(BlockRender.TYPE, BlockRender.Type.SHAFT_S);
            case WEST: return render.withProperty(BlockRender.TYPE, BlockRender.Type.SHAFT_W);
        }
        return render;
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        if (side.getAxis() != axis()) return connection(0);
        if (!getState().getValue(BlockKineticUtility.SHIFT)) {
            if (getState().getValue(BlockKineticUtility.ACTIVE)) {
                return connection(0);
            }
        }
        return connection(1);
    }

    public float getSpeed(boolean b) {
        EnumFacing dir1 = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, axis());
        EnumFacing dir2 = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.NEGATIVE, axis());

        if (b) {
            TileEntity entity = world.getTileEntity(pos.offset(dir1));
            if (entity instanceof IKineticTileEntity) {
                return ((IKineticTileEntity)entity).speed();
            }
        } else {
            TileEntity entity = world.getTileEntity(pos.offset(dir2));

            if (entity instanceof IKineticTileEntity) {
                return ((IKineticTileEntity)entity).speed();
            }
        }

        return 0.0f;
    }

    public AbstractTileEntityKinetic getOtherTE(boolean b) {
        EnumFacing dir1 = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, axis());
        EnumFacing dir2 = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.NEGATIVE, axis());

        if (b) {
            TileEntity entity = world.getTileEntity(pos.offset(dir1));
            if (entity instanceof AbstractTileEntityKinetic) {
                return (AbstractTileEntityKinetic) entity;
            }
        } else {
            TileEntity entity = world.getTileEntity(pos.offset(dir2));

            if (entity instanceof AbstractTileEntityKinetic) {
                return (AbstractTileEntityKinetic) entity;
            }
        }

        return this;
    }
}
