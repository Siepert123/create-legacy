package com.siepert.createlegacy.tileentity;

import com.siepert.createapi.network.IKineticTE;
import com.siepert.createapi.network.KineticBlockInstance;
import com.siepert.createapi.network.NetworkContext;
import com.siepert.createlegacy.CreateLegacyConfigHolder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

import static com.siepert.createlegacy.blocks.kinetic.BlockHandCrank.FACING;

public class TileEntityHandCrank extends TileEntity implements ITickable, IKineticTE {
    public int cooldownTicks;
    public boolean updated;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        cooldownTicks = compound.getInteger("cooldown");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("cooldown", cooldownTicks);
        return compound;
    }

    @Override
    public void update() {
        IBlockState state = world.getBlockState(pos);
        if (cooldownTicks > 0 && !updated) {
            NetworkContext context = new NetworkContext();

            context.addKineticBlockInstance(new KineticBlockInstance(pos, false));

            TileEntity entity = world.getTileEntity(pos.offset(state.getValue(FACING)));

            if (entity instanceof IKineticTE) {
                ((IKineticTE) entity).passNetwork(context, state.getValue(FACING).getOpposite(),
                        false, false, false);
            }

            cooldownTicks--;

            context.runThroughPhases(world);
        }
        updated = false;
    }


    @Override
    public double getStressImpact() {
        return 0;
    }

    @Override
    public double getStressCapacity() {
        return CreateLegacyConfigHolder.kineticConfig.handCrankStressCapacity;
    }

    @Override
    public boolean isGenerator() {
        return true;
    }

    @Override
    public int getProducedSpeed() {
        return cooldownTicks > 0 ? 16 : 0;
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
        if (srcIsCog || source != state.getValue(FACING)) return;

        context.addKineticBlockInstance(new KineticBlockInstance(pos, inverted));
    }
}
