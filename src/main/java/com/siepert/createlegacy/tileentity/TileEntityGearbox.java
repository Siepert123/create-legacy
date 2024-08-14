package com.siepert.createlegacy.tileentity;

import com.siepert.createapi.network.IKineticTE;
import com.siepert.createapi.network.KineticBlockInstance;
import com.siepert.createapi.network.NetworkContext;
import com.siepert.createlegacy.blocks.kinetic.BlockKineticUtility;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileEntityGearbox extends TileEntity implements IKineticTE {
    @Override
    public double getStressImpact() {
        return 0;
    }

    @Override
    public double getStressCapacity() {
        return 0;
    }

    @Override
    public int getProducedSpeed() {
        return 0;
    }

    @Override
    public void kineticTick(NetworkContext context) {

    }

    @Override
    public void setUpdated() {

    }

    @Override
    public void passNetwork(NetworkContext context, EnumFacing source, boolean srcIsCog, boolean srcCogIsHorizontal, boolean inverted) {
        IBlockState state = world.getBlockState(pos);

        if (srcIsCog) return;

        if (source.getAxis() == state.getValue(BlockKineticUtility.AXIS)) return;

        context.addKineticBlockInstance(new KineticBlockInstance(pos, inverted));

        EnumFacing facing1 = source
                .rotateAround(state.getValue(BlockKineticUtility.AXIS));
        EnumFacing facing2 = source
                .rotateAround(state.getValue(BlockKineticUtility.AXIS))
                .rotateAround(state.getValue(BlockKineticUtility.AXIS));
        EnumFacing facing3 = source
                .rotateAround(state.getValue(BlockKineticUtility.AXIS))
                .rotateAround(state.getValue(BlockKineticUtility.AXIS))
                .rotateAround(state.getValue(BlockKineticUtility.AXIS));

        TileEntity entity1 = world.getTileEntity(pos.offset(facing1));
        TileEntity entity2 = world.getTileEntity(pos.offset(facing2));
        TileEntity entity3 = world.getTileEntity(pos.offset(facing3));

        if (entity1 instanceof IKineticTE) {
            ((IKineticTE) entity1).passNetwork(context, facing1.getOpposite(),
                    false, false, !inverted);
        }

        if (entity2 instanceof IKineticTE) {
            ((IKineticTE) entity2).passNetwork(context, facing2.getOpposite(), false, false, !inverted);
        }

        if (entity3 instanceof IKineticTE) {
            ((IKineticTE) entity3).passNetwork(context, facing3.getOpposite(),
                    false, false, inverted);
        }
    }
}
