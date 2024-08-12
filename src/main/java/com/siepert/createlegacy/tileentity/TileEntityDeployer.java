package com.siepert.createlegacy.tileentity;

import com.siepert.createapi.network.IKineticTE;
import com.siepert.createapi.network.KineticBlockInstance;
import com.siepert.createapi.network.NetworkContext;
import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.CreateLegacyConfigHolder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import static com.siepert.createlegacy.blocks.kinetic.BlockDeployer.FACING;

public class TileEntityDeployer extends TileEntity implements IKineticTE {
    int cooldown = 0;

    ItemStack useStack;
    ItemStack trashStack;

    public TileEntityDeployer() {
        useStack = ItemStack.EMPTY;
        trashStack = ItemStack.EMPTY;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setInteger("Cooldown", cooldown);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        cooldown = compound.getInteger("Cooldown");
    }

    @Override
    public double getStressImpact() {
        return CreateLegacyConfigHolder.kineticConfig.deployerStressImpact;
    }

    @Override
    public boolean isConsumer() {
        return true;
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
        EnumFacing facing = world.getBlockState(pos).getValue(FACING).toVanillaFacing();

        BlockPos actPos = pos.offset(facing, 2);

        boolean mayPlace = world.getBlockState(actPos).getMaterial().isReplaceable();
    }

    @Override
    public void setUpdated() {

    }

    @Override
    public void passNetwork(NetworkContext context, EnumFacing source, boolean srcIsCog, boolean srcCogIsHorizontal, boolean inverted) {
        IBlockState state = world.getBlockState(pos);

        if (srcIsCog) return;

        if (source.getAxis() == state.getValue(FACING).toVanillaFacing().rotateAround(EnumFacing.Axis.Y).getAxis()) {
            context.addKineticBlockInstance(new KineticBlockInstance(pos, inverted));

            TileEntity entity = world.getTileEntity(pos.offset(source.getOpposite()));

            if (entity instanceof IKineticTE) {
                ((IKineticTE) entity).passNetwork(context, source, false, false, inverted);
            }
        }
    }
}
