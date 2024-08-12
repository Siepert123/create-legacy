package com.siepert.createapi;

import net.minecraft.util.EnumFacing;

/**
 * The IKineticTE is an interface for TileEntities.
 * If you want to add a kinetic block (eg. a centrifuge or something idk) it is REQUIRED that this interface is implemented.
 *
 * @author Siepert123
 * */
public interface IKineticTE {

    double getStressImpact();

    default int getMinimalSpeed() {
        return 0;
    }

    default boolean isConsumer() {
        return getStressImpact() != 0;
    }

    double getStressCapacity();

    int getProducedSpeed();

    default boolean isGenerator() {
        return getStressCapacity() != 0;
    }

    default boolean ignoreOverstress() {
        return false;
    }

    void kineticTick(NetworkContext context);

    void setUpdated();

    void passNetwork(NetworkContext context, EnumFacing source, boolean srcIsCog, boolean srcCogIsHorizontal, boolean inverted);
}
