package com.siepert.createlegacy.tileentity;

import com.siepert.createapi.network.IKineticTE;
import com.siepert.createapi.network.KineticBlockInstance;
import com.siepert.createapi.network.NetworkContext;
import com.siepert.createlegacy.CreateLegacyConfigHolder;
import com.siepert.createlegacy.blocks.kinetic.BlockKineticUtility;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import static com.siepert.createlegacy.blocks.kinetic.BlockKineticUtility._BOOLEAN0;

public class TileEntityClutch extends TileEntity implements IKineticTE {
    @Override
    public double getStressImpact() {
        return CreateLegacyConfigHolder.kineticConfig.clutchStressImpact;
    }

    @Override
    public boolean isConsumer() {
        return getStressImpact() > 0;
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
        lastKineticTick = world.getTotalWorldTime();
        lastSpeed = context.networkSpeed;
    }

    @Override
    public void setUpdated() {

    }

    @Override
    public void passNetwork(NetworkContext context, EnumFacing source, boolean srcIsCog, boolean srcCogIsHorizontal, boolean inverted) {
        IBlockState state = world.getBlockState(pos);

        boolean red = false;

        for (EnumFacing facing : EnumFacing.VALUES) {
            if (world.getRedstonePower(pos, facing) > 0) red = true;
        }

        if (red) {
            if (!state.getValue(_BOOLEAN0)) {
                world.setBlockState(pos, state.withProperty(_BOOLEAN0, true), 3);
            }
        } else {
            if (state.getValue(_BOOLEAN0)) {
                world.setBlockState(pos, state.withProperty(_BOOLEAN0, false), 3);
            }
        }

        if (srcIsCog) return;

        if (source.getAxis() == state.getValue(BlockKineticUtility.AXIS)) {

            if (!red) {

                context.addKineticBlockInstance(new KineticBlockInstance(pos, inverted));

                TileEntity entity = world.getTileEntity(pos.offset(source.getOpposite()));

                if (entity instanceof IKineticTE && !context.hasBlockBeenChecked(pos.offset(source.getOpposite()))) {
                    ((IKineticTE) entity).passNetwork(context, source, false, false, inverted);
                }

            }
        }
    }

    long lastKineticTick = 0;
    int lastSpeed = 0;

    @Override
    public int getRS() {
        return world.getTotalWorldTime() == lastKineticTick + 1 ? lastSpeed : 0;
    }
}
