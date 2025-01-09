package com.melonstudios.createlegacy.util;

import net.minecraft.util.IStringSerializable;

public enum HeatLevel implements IStringSerializable {
    NONE, PASSIVE, HEATED, SUPERHEATED;

    private final String registry = ordinal() == 0 ? "none" : (ordinal() == 1 ? "passive" : (ordinal() == 2 ? "heated" : (ordinal() == 3 ? "superheated" : null)));

    public boolean isHotterOrEqual(HeatLevel level) {
        return level.ordinal() >= this.ordinal();
    }

    @Override
    public String getName() {
        return registry;
    }
}
