package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.block.kinetic.BlockWaterWheel;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import com.melonstudios.createlegacy.util.NetworkContext;
import net.minecraft.util.EnumFacing;

public class TileEntityWaterWheel extends AbstractTileEntityKineticGenerator {
    @Override
    protected String namePlate() {
        return "Water wheel";
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        EnumFacing.Axis axis = world.getBlockState(pos).getValue(BlockWaterWheel.AXIS);
        return side.getAxis() == axis ? EnumKineticConnectionType.SHAFT : EnumKineticConnectionType.NONE;
    }

    @Override
    public int generatesSU() {
        return 32;
    }

    @Override
    public int generatesRS() {
        return world.getBlockState(pos.down()).getMaterial().isLiquid() ? 8 : 0;
    }

    @Override
    protected void start() {
        NetworkContext context = new NetworkContext();
    }
}
