package com.melonstudios.createlegacy.util;

public enum EnumKineticConnectionType {
    NONE,
    SHAFT,
    COG, //Horizontal, or along X-axis
    COG_ALT, //Vertical, or along Z-axis
    BELT;

    public boolean compare(EnumKineticConnectionType other) {
        return this == other;
    }
    public boolean inverts() {
        return this == COG || this == COG_ALT;
    }
}
