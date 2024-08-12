package com.siepert.createlegacy.tileentity;

import com.siepert.createapi.network.IKineticTE;
import com.siepert.createapi.network.KineticBlockInstance;
import com.siepert.createapi.network.NetworkContext;
import com.siepert.createlegacy.CreateLegacyConfigHolder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

import static com.siepert.createlegacy.blocks.kinetic.BlockCreativeMotor.FACING;

public class TileEntityCreativeMotor extends TileEntity implements ITickable, IKineticTE {

    boolean updated = false;

    @Override
    public void update() {
        IBlockState myState = world.getBlockState(pos);

        NetworkContext context = new NetworkContext();

        context.addKineticBlockInstance(new KineticBlockInstance(pos, false));

        TileEntity entity = world.getTileEntity(pos.offset(myState.getValue(FACING)));

        if (entity instanceof IKineticTE) {
            ((IKineticTE) entity).passNetwork(context, myState.getValue(FACING),
                    false, false, false);
        }

        context.runThroughPhases(world);
    }


    @Override
    public double getStressImpact() {
        return 0;
    }

    @Override
    public double getStressCapacity() {
        return CreateLegacyConfigHolder.kineticConfig.creativeMotorStressCapacity;
    }

    @Override
    public int getProducedSpeed() {
        return 256;
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
        updated = true;
    }

    @Override
    public void passNetwork(NetworkContext context, EnumFacing source, boolean srcIsCog, boolean srcCogIsHorizontal, boolean inverted) {
        IBlockState state = world.getBlockState(pos);

        if (source != state.getValue(FACING)) return;
        if (srcIsCog) return;

        context.addKineticBlockInstance(new KineticBlockInstance(pos, inverted));
    }
}
