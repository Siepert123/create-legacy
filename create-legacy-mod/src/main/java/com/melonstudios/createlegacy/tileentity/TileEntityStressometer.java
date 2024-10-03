package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.block.kinetic.BlockNetworkInspector;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import com.melonstudios.createlegacy.util.INetworkLogger;
import net.minecraft.util.EnumFacing;

public class TileEntityStressometer extends AbstractTileEntityKinetic implements INetworkLogger {
    @Override
    protected String namePlate() {
        return "Stressometer";
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        return getState().getValue(BlockNetworkInspector.AXIS) == side.getAxis() ? EnumKineticConnectionType.SHAFT : EnumKineticConnectionType.NONE;
    }

    @Override
    public void clearInfo() {
        super.clearInfo();
        lastSU = 0;
        lastMaxSU = 0;
    }

    @Override
    protected void tick() {
        super.tick();
    }

    protected float lastSU = 0;
    protected float lastMaxSU = 0;

    @Override
    public void setSU(float su) {
        lastSU = su;
    }

    @Override
    public void setMaxSU(float su) {
        lastMaxSU = su;
    }

    public float getDegreesPart(boolean inv) {
        if (inv) {
            float degreesMax = 270.0f;
            float percent = lastSU / lastMaxSU;
            if (lastMaxSU == 0) percent = 0;
            return degreesMax - degreesMax * percent - 90;
        }
        float degreesMax = 270.0f;
        float percent = lastSU / lastMaxSU;
        if (lastMaxSU == 0) percent = 0;
        return degreesMax * percent;
    }


}
