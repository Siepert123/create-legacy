package com.siepert.createapi;

import net.minecraft.util.math.BlockPos;

/**
 * @author Siepert123
 * */
public class KineticBlockInstance {
    public final BlockPos pos;
    public final boolean inverted;

    public KineticBlockInstance(BlockPos pos, boolean inv) {
        this.pos = pos;
        this.inverted = inv;
    }
}
