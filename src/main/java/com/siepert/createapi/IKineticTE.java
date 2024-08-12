package com.siepert.createapi;

import java.util.List;

/**
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

    void updateTick(NetworkContext context);

    void setUpdated();

    void passNetwork(NetworkContext context);
}
