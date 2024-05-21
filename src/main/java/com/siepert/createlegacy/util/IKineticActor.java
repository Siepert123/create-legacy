package com.siepert.createlegacy.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IKineticActor {
    public void act(World worldIn, BlockPos pos);
}
