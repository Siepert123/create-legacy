package com.siepert.createlegacy.util;

import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IHasRotation {
    public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 3);
}
