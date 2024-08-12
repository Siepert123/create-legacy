package com.siepert.createlegacy.tileentity;

import com.siepert.createapi.network.IKineticTE;
import com.siepert.createapi.network.KineticBlockInstance;
import com.siepert.createapi.network.NetworkContext;
import com.siepert.createlegacy.CreateLegacyConfigHolder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

import static com.siepert.createlegacy.blocks.kinetic.BlockWaterWheel.AXIS;

public class TileEntityWaterWheel extends TileEntity implements ITickable, IKineticTE {
    boolean updated = true;

    @Override
    public void update() {
        IBlockState myState = world.getBlockState(pos);

        boolean liquid = world.getBlockState(pos.down()).getMaterial().isLiquid();

        if (liquid) {
            EnumFacing facing1 = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, myState.getValue(AXIS));
            EnumFacing facing2 = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.NEGATIVE, myState.getValue(AXIS));

            TileEntity axPos = world.getTileEntity(pos.offset(facing1));
            TileEntity axNeg = world.getTileEntity(pos.offset(facing2));

            NetworkContext context = new NetworkContext();

            if (axPos instanceof IKineticTE) {
                ((IKineticTE) axPos).passNetwork(context, facing1.getOpposite(),
                        false, false, false);
            }
            if (axNeg instanceof IKineticTE) {
                ((IKineticTE) axNeg).passNetwork(context, facing2.getOpposite(),
                        false, false, false);
            }

            context.runThroughPhases(world);
        }
    }


    @Override
    public double getStressImpact() {
        return 0;
    }

    @Override
    public double getStressCapacity() {
        return CreateLegacyConfigHolder.kineticConfig.waterWheelStressCapacity;
    }

    @Override
    public int getProducedSpeed() {
        if (world.getBlockState(pos.down()).getMaterial().isLiquid()) return 8;
        return 0;
    }

    @Override
    public boolean isGenerator() {
        return true;
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

        if (source.getAxis() == state.getValue(AXIS)) {
            context.addKineticBlockInstance(new KineticBlockInstance(pos, inverted));

            TileEntity entity = world.getTileEntity(pos.offset(source.getOpposite()));

            if (entity instanceof IKineticTE) {
                ((IKineticTE) entity).passNetwork(context, source, false, false, inverted);
            }
        }
    }
}
