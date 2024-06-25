package com.siepert.createlegacy.tileentity;

import com.siepert.createapi.IKineticActor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

import static com.siepert.createlegacy.blocks.kinetic.BlockWaterWheel.AXIS;

public class TileEntityWaterWheel extends TileEntity implements ITickable {
    @Override
    public void update() {
        if (world.getTotalWorldTime() % 100 == 0) {
            IBlockState myState = world.getBlockState(pos);

            Block water = world.getBlockState(pos.down()).getBlock();

            if (water == Blocks.WATER || (water instanceof BlockLiquid && world.getTotalWorldTime() % 200 == 0)) {
                EnumFacing facing1 = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, myState.getValue(AXIS));
                EnumFacing facing2 = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.NEGATIVE, myState.getValue(AXIS));

                Block axPos = world.getBlockState(pos.offset(facing1)).getBlock();
                Block axNeg = world.getBlockState(pos.offset(facing2)).getBlock();

                List<BlockPos> iteratedBlocks = new ArrayList<>();
                if (axPos instanceof IKineticActor) {
                    ((IKineticActor) axPos).passRotation(world, pos.offset(facing1),
                            facing1.getOpposite(),
                            iteratedBlocks, false, false, false);
                }
                if (axNeg instanceof IKineticActor) {
                    ((IKineticActor) axNeg).passRotation(world, pos.offset(facing2),
                            facing2.getOpposite(),
                            iteratedBlocks, false, false, false);
                }
            }
        }
    }


}
