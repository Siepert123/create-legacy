package com.siepert.createlegacy.tileentity.converter;

import com.siepert.createapi.network.IKineticTE;
import com.siepert.createapi.network.NetworkContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

/**
 * TileEntity to convert between different kinetic systems
 *
 * @author moddingforreal
 * */
public class TileEntityConverter extends TileEntity implements IKineticTE {
    @Override
    public double getStressImpact() {
        return 0;
    }

    @Override
    public int getMinimalSpeed() {
        return IKineticTE.super.getMinimalSpeed();
    }

    @Override
    public boolean isConsumer() {
        return IKineticTE.super.isConsumer();
    }

    @Override
    public double getStressCapacity() {
        return 0;
    }

    @Override
    public int getProducedSpeed() {
        return 0;
    }

    @Override
    public boolean isGenerator() {
        return IKineticTE.super.isGenerator();
    }

    @Override
    public boolean ignoreOverstress() {
        return IKineticTE.super.ignoreOverstress();
    }

    @Override
    public void kineticTick(NetworkContext context) {
        lastKineticTick = world.getTotalWorldTime();
        lastSpeed = context.networkSpeed;
    }

    long lastKineticTick = 0;
    int lastSpeed = 0;

    @Override
    public int getRS() {
        return world.getTotalWorldTime() == lastKineticTick + 1 ? lastSpeed : 0;
    }

    @Override
    public void setUpdated() {

    }

    @Override
    public void passNetwork(NetworkContext context, EnumFacing source, boolean srcIsCog, boolean srcCogIsHorizontal, boolean inverted) {

    }
}
