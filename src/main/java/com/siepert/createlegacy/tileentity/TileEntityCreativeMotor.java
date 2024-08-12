package com.siepert.createlegacy.tileentity;

import com.siepert.createapi.IKineticActor;
import com.siepert.createapi.IKineticTE;
import com.siepert.createapi.KineticBlockInstance;
import com.siepert.createapi.NetworkContext;
import com.siepert.createlegacy.CreateLegacyConfigHolder;
import com.siepert.createlegacy.blocks.kinetic.BlockCreativeMotor;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

import static com.siepert.createlegacy.blocks.kinetic.BlockCreativeMotor.FACING;

public class TileEntityCreativeMotor extends TileEntity implements ITickable, IKineticTE {

    boolean updated = false;

    @Override
    public void update() {
        IBlockState myState = world.getBlockState(pos);

        NetworkContext context = new NetworkContext();

        context.addKineticBlockInstance(new KineticBlockInstance(pos, false));

        TileEntity entity = world.getTileEntity(pos.offset(myState.getValue(FACING).getOpposite()));

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
