package com.siepert.createlegacy.util;

import net.minecraft.block.properties.PropertyInteger;

public interface IHasRotation {
    public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 3);
}
