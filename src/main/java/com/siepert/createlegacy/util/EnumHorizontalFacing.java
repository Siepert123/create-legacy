package com.siepert.createlegacy.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;

public enum EnumHorizontalFacing implements IStringSerializable {
    NORTH(0, EnumFacing.NORTH, EnumFacing.NORTH.getAxis()),
    EAST(1, EnumFacing.EAST, EnumFacing.EAST.getAxis()),
    SOUTH(2, EnumFacing.SOUTH, EnumFacing.SOUTH.getAxis()),
    WEST(3, EnumFacing.WEST, EnumFacing.WEST.getAxis());

    private final EnumFacing connectedVanillaFacing;
    private final int index;
    private final EnumFacing.Axis connectedVanillaAxis;
    public EnumFacing toVanillaFacing() {
        return this.connectedVanillaFacing;
    }

    public int index() {return index;}
    public static EnumHorizontalFacing fromIndex(int index) {
        for (EnumHorizontalFacing facing : EnumHorizontalFacing.values()) {
            if (facing.index == index) return facing;
        }
        return fromIndex(0);
    }

    public EnumHorizontalFacing cycle() {
        switch (this) {
            case NORTH:
                return EAST;
            case EAST:
                return SOUTH;
            case SOUTH:
                return WEST;
            case WEST:
                return NORTH;
        }
        return NORTH;
    }

    public EnumFacing.Axis axis() {
        return this.connectedVanillaAxis;
    }
    public static EnumHorizontalFacing fromVanillaFacing(EnumFacing facing) {
        switch (facing) {
            case NORTH:
                return EnumHorizontalFacing.NORTH;
            case EAST:
                return EnumHorizontalFacing.EAST;
            case SOUTH:
                return EnumHorizontalFacing.SOUTH;
            case WEST:
                return EnumHorizontalFacing.WEST;
        }
        return EnumHorizontalFacing.NORTH;
    }

    EnumHorizontalFacing(int index, EnumFacing connectedVanillaFacing, EnumFacing.Axis connectedVanillaAxis) {
        this.index = index;
        this.connectedVanillaFacing = connectedVanillaFacing;
        this.connectedVanillaAxis = connectedVanillaAxis;
    }

    @Override
    public String getName() {
        return this.toString().toLowerCase();
    }

    public EnumHorizontalFacing getOpposite() {
        switch (this) {
            case NORTH:
                return SOUTH;
            case EAST:
                return WEST;
            case SOUTH:
                return NORTH;
            case WEST:
                return EAST;
        }
        throw new IllegalStateException("Erm what the chassis");
    }
}
