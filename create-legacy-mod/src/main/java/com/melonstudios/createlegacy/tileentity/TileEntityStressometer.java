package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.block.kinetic.BlockNetworkInspector;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKinetic;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import com.melonstudios.createlegacy.util.INetworkLogger;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;

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

    @Override
    public String queryData() {
        return String.format("Stress: %s/%s SU", lastSU, lastMaxSU);
    }

    @Nullable
    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(String.format("%s (%s RPM, %s/%s SU)", namePlate(), speed(), lastSU, lastMaxSU));
    }

    public float getDegreesPart(boolean inv) {
        float percent = lastMaxSU > 0 ? lastSU / lastMaxSU : 0;
        float inaccuracy = percent >= 1 ? random().nextFloat() * 2 - 1 : 0;
        if (inv) {
            float degreesMax = 270.0f;
            return degreesMax - degreesMax * percent + 90 + inaccuracy;
        }
        float degreesMax = 270.0f;
        return degreesMax * percent + inaccuracy;
    }


}
