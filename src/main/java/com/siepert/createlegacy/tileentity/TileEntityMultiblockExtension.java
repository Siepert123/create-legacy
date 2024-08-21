package com.siepert.createlegacy.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityMultiblockExtension extends TileEntity {
    private BlockPos corePos;

    public void setCorePos(BlockPos pos) {
        corePos = pos;
    }

    public BlockPos getCorePos() {
        return corePos;
    }
}
