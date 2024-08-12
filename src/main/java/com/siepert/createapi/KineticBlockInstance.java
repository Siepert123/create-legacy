package com.siepert.createapi;

import net.minecraft.util.math.BlockPos;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KineticBlockInstance instance = (KineticBlockInstance) o;
        return Objects.equals(pos, instance.pos);
    }
}
