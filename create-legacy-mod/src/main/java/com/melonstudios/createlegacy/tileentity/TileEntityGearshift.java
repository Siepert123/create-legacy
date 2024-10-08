package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createapi.kinetic.IKineticTileEntity;
import com.melonstudios.createapi.network.NetworkContext;
import com.melonstudios.createlegacy.block.BlockRender;
import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.block.kinetic.BlockKineticUtility;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKinetic;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class TileEntityGearshift extends AbstractTileEntityKinetic {
    @Override
    protected String namePlate() {
        return "Gearshift";
    }

    public final EnumFacing.Axis axis() {
        return getState().getValue(BlockKineticUtility.AXIS);
    }
    public final boolean active() {
        return getState().getValue(BlockKineticUtility.ACTIVE);
    }

    boolean invertedSide = false;

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
        return side.getAxis() == axis() ? connection(1) : connection(0);
    }

    @Override
    protected void tick() {
        boolean red = false;
        for (EnumFacing dir : EnumFacing.VALUES) {
            if (world.isSidePowered(pos, dir)) red = true;
        }
        if (active() != red) {
            world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockKineticUtility.ACTIVE, red), 3);
            validate();
            world.setTileEntity(pos, this);
        }
    }

    @Override
    public void passNetwork(IKineticTileEntity src, EnumFacing srcDir, NetworkContext context, boolean inverted) {
        if (!active()) super.passNetwork(src, srcDir, context, inverted);
        else {
            if (context.checked(this)) {
            } else {
                context.add(this, inverted != active());
                invertedSide = srcDir.getOpposite().getAxisDirection() == EnumFacing.AxisDirection.POSITIVE;
                EnumFacing dir = srcDir.getOpposite();

                IKineticTileEntity te = getTE(pos.offset(dir));
                if (mayConnect(te, dir, dir.getOpposite())) {
                    if (getConnectionType(dir).inverts()) {
                        te.passNetwork(this, dir.getOpposite(), context, inverted == active());
                    } else {
                        te.passNetwork(this, dir.getOpposite(), context, inverted != active());
                    }
                }
            }
        }
    }
}
