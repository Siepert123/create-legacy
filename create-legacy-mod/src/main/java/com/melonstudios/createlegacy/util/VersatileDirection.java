package com.melonstudios.createlegacy.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.Objects;

public final class VersatileDirection {
    private final int x, y, z;

    public VersatileDirection(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getXOffset() {
        return x;
    }

    public int getYOffset() {
        return y;
    }

    public int getZOffset() {
        return z;
    }

    public BlockPos offsetBlockPos(BlockPos origin, int n) {
        return origin.add(x * n, y * n, z * n);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VersatileDirection)) return false;
        VersatileDirection that = (VersatileDirection) o;
        return x == that.x && y == that.y && z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
    public static VersatileDirection fromEnumFacing(EnumFacing src) {
        return new VersatileDirection(src.getFrontOffsetX(), src.getFrontOffsetY(), src.getFrontOffsetZ());
    }

    public static final VersatileDirection NULL = new VersatileDirection(0, 0, 0);
    
    public static final VersatileDirection UP = new VersatileDirection(0, 1, 0);
    public static final VersatileDirection DOWN = new VersatileDirection(0, -1, 0);
    public static final VersatileDirection NORTH = new VersatileDirection(0, 0, -1);
    public static final VersatileDirection SOUTH = new VersatileDirection(0, 0, 1);
    public static final VersatileDirection WEST = new VersatileDirection(-1, 0, 0);
    public static final VersatileDirection EAST = new VersatileDirection(1, 0, 0);
}
