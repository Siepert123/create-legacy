package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.block.kinetic.BlockNetworkInspector;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import net.minecraft.util.EnumFacing;

public class TileEntitySpeedometer extends AbstractTileEntityKinetic {
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
            float percent = speed() / 256.0f;
            return degreesMax - degreesMax * percent - 90;
        }
        float degreesMax = 270.0f;
        float percent = speed() / 256.0f;
        return degreesMax * percent;
    }
}
