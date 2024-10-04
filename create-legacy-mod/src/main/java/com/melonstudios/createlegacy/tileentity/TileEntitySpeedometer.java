package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.block.kinetic.BlockNetworkInspector;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKinetic;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import com.melonstudios.createlegacy.util.INetworkLogger;
import net.minecraft.util.EnumFacing;

public class TileEntitySpeedometer extends AbstractTileEntityKinetic implements INetworkLogger {
    @Override
    protected String namePlate() {
        return "Speedometer";
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        return getState().getValue(BlockNetworkInspector.AXIS) == side.getAxis() ? EnumKineticConnectionType.SHAFT : EnumKineticConnectionType.NONE;
    }

    public float getDegreesPart(boolean inv) {
        if (inv) {
            float degreesMax = 270.0f;
            float percent = Math.abs(speed()) / 256.0f;
            return degreesMax - degreesMax * percent + 90;
        }
        float degreesMax = 270.0f;
        float percent = Math.abs(speed()) / 256.0f;
        return degreesMax * percent;
    }

    @Override
    public void setSU(float su) {

    }

    @Override
    public void setMaxSU(float su) {

    }

    @Override
    public String queryData() {
        return String.format("Speed: %s RPM", speed());
    }
}
