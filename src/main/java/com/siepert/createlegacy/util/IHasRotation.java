package com.siepert.createlegacy.util;

import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public interface IHasRotation {
    PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 3);

    EnumFacing.Axis rotateAround(IBlockState state);
}
