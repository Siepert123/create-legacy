package com.siepert.createlegacy.tileentity;

import com.siepert.createlegacy.util.IKineticActor;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

import static com.siepert.createlegacy.blocks.kinetic.BlockWaterWheel.AXIS;

public class TileEntityWaterWheel extends TileEntity implements ITickable {
    @Override
    public void update() {
        IBlockState myState = world.getBlockState(pos);
    }


}
