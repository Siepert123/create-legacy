package com.siepert.createlegacy.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IWrenchable {

    /**Do something when the block is right-clicked with a Wrench.
     * @param side The side that this block is clicked on.
     * @param playerIn The player who clicked.
     * @return True if the wrench actually did something (will trigger hand movement).*/
    boolean onWrenched(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer playerIn);
}
