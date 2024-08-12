package com.siepert.createapi.network;

import net.minecraft.util.EnumFacing;

/**
 * The IKineticTE is an interface for TileEntities.
 * If you want to add a kinetic block (e.g. a centrifuge or something IDK) it is REQUIRED that this interface is implemented.
 *
 * @author Siepert123
 * */
public interface IKineticTE {

    /**
     * @return The stress impact. Final SU consumption is calculated by multiplying this number with the network speed.
     */
    double getStressImpact();

    default int getMinimalSpeed() {
        return 0;
    }

    default boolean isConsumer() {
        return false;
    }

    /**
     * @return The stress capacity. Final SU production is calculated by multiplying this number with the produced speed.
     */
    double getStressCapacity();

    int getProducedSpeed();

    default boolean isGenerator() {
        return false;
    }

    /**
     * @return True if the machine should still be activated when the network is overstressed.
     */
    default boolean ignoreOverstress() {
        return false;
    }

    void kineticTick(NetworkContext context);

    void setUpdated();

    /**
     * Similar to passRotation, except the kinetic actions are not performed.
     * Only add a kinetic block instance if it is actually going to be activated.
     *
     * @param context NetworkContext. Main use is to check if a block is already added to the network,
     *                and to add this block instance to the network.
     * @param source The direction the signal came from.
     * @param srcIsCog Whether the input is from a cogwheel.
     * @param srcCogIsHorizontal If the cogwheel was horizontal.
     * @param inverted If the rotation rotates the other way.
     */
    void passNetwork(NetworkContext context, EnumFacing source, boolean srcIsCog, boolean srcCogIsHorizontal, boolean inverted);
}
