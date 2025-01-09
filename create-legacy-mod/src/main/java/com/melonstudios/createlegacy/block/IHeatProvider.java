package com.melonstudios.createlegacy.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * An interface for blocks that may provide heat. (e.g. blaze burners)
 */
public interface IHeatProvider {
    int getHeatLevel(World world, BlockPos pos);
}
