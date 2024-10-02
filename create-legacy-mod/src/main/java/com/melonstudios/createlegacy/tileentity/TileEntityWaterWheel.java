package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createapi.network.NetworkContext;
import com.melonstudios.createlegacy.block.kinetic.BlockWaterWheel;
import com.melonstudios.createlegacy.util.DisplayLink;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class TileEntityWaterWheel extends AbstractTileEntityKinetic {
    public TileEntityWaterWheel() {
        super();
    }
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
    protected void tick() {
        if (isUpdated()) return;
        NetworkContext context = new NetworkContext(world);

        passNetwork(null, null, context, false);

        context.start();
    }
}
