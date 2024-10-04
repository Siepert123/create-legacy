package com.melonstudios.createlegacy.util;

public enum EnumContraptionAssemblyMode {
    DISASSEMBLE_ALWAYS,
    DISASSEMBLE_NEAR_INITIAL_ANGLE,
    NEVER_DISASSEMBLE;

    public int toId() {
        return ordinal();
    }
    public static EnumContraptionAssemblyMode fromId(int id) {
        return values()[id % values().length];
    }
}
