package com.melonstudios.createlegacy.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IGoggleInfo {
    default NonNullList<String> getGoggleInformation(World world, BlockPos pos, IBlockState state) {
        return NonNullList.create();
    }
}
