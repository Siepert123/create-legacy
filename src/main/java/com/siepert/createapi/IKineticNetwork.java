package com.siepert.createapi;

import net.minecraft.util.math.BlockPos;

public interface IKineticNetwork {
    int getTotalSU();
    int getConsumedSU();

    default boolean overstressed() {
        return getConsumedSU() > getTotalSU();
    }

    void joinNetworks(IKineticNetwork other);

    int getNetworkSpeed();

    boolean isRotationInvertedAt(BlockPos pos);
}
