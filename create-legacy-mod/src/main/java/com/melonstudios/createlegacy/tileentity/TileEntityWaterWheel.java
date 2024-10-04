package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createapi.network.NetworkContext;
import com.melonstudios.createlegacy.block.kinetic.BlockWaterWheel;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKinetic;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
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
        if (isUpdated() || generatedRPM() == 0) return;
        NetworkContext context = new NetworkContext(world);

        passNetwork(null, null, context, false);

        context.start();
    }

    @Override
    public float generatedRPM() {
        return world.getBlockState(pos.down()).getMaterial().isLiquid() ? 8.0f : 0.0f;
    }

    @Override
    public float generatedSUMarkiplier() {
        return 32.0f;
    }
}
