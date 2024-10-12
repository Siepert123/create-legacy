package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createapi.kinetic.IKineticTileEntity;
import com.melonstudios.createapi.network.NetworkContext;
import com.melonstudios.createlegacy.block.kinetic.BlockGearbox;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKinetic;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileEntityGearbox extends AbstractTileEntityKinetic {

    public EnumFacing.Axis axis() {
        return getState().getValue(BlockGearbox.AXIS);
    }



    @Override
    protected String namePlate() {
        return "Gearbox";
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        return side.getAxis() == axis() ? connection(0) : connection(1);
    }

    @Override
    public void passNetwork(IKineticTileEntity src, EnumFacing srcDir, NetworkContext context, boolean inverted) {
        if (context.checked(this)) {
            //nothing happens :3
            return;
        } else {
            context.add(this, inverted);
            EnumFacing dir = srcDir.rotateAround(axis());
            TileEntity entity = world.getTileEntity(pos);
            if (entity instanceof IKineticTileEntity) {
                if (mayConnect((IKineticTileEntity) entity, dir, dir.getOpposite()))
                    ((IKineticTileEntity) entity).passNetwork(this, dir.getOpposite(), context, inverted);
            }
            dir = dir.rotateAround(axis());
            entity = world.getTileEntity(pos);
            if (entity instanceof IKineticTileEntity) {
                if (mayConnect((IKineticTileEntity) entity, dir, dir.getOpposite()))
                    ((IKineticTileEntity) entity).passNetwork(this, dir.getOpposite(), context, !inverted);
            }
            dir = dir.rotateAround(axis());
            entity = world.getTileEntity(pos);
            if (entity instanceof IKineticTileEntity) {
                if (mayConnect((IKineticTileEntity) entity, dir, dir.getOpposite()))
                    ((IKineticTileEntity) entity).passNetwork(this, dir.getOpposite(), context, !inverted);
            }
        }
    }
}
