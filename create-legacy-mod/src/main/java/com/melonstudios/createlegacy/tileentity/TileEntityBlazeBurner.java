package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.util.EnumBlazeLevel;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class TileEntityBlazeBurner extends TileEntity implements ITickable {
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setInteger("ticksRemaining", ticksRemaining);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        ticksRemaining = compound.getInteger("ticksRemaining");
    }

    protected int ticksRemaining = 0;

    protected EnumBlazeLevel blazeLevel = EnumBlazeLevel.PASSIVE;
    public EnumBlazeLevel getBlazeLevel() {
        if (ticksRemaining > 10000) {
            return EnumBlazeLevel.SUPERHEATED;
        } else if (ticksRemaining > 0) {
            return EnumBlazeLevel.HEATED;
        } else {
            return EnumBlazeLevel.NONE;
        }
    }
    public IBlockState getAssociatedBlazePart() {
        final IBlockState render = ModBlocks.RENDER.getDefaultState();
        return render;
    }
    @SideOnly(Side.CLIENT)
    public float lookAtNearestPlayer() {
        EntityPlayer player = world.getClosestPlayer(
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                8, true);

        if (player != null)
            return player.getRotationYawHead() + 180;
        return 0;
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            if (ticksRemaining > 0) ticksRemaining--;
        }
    }

    public boolean superheat() {
        if (ticksRemaining < 10000) {
            ticksRemaining = 15000;
            return true;
        }
        return false;
    }

    public void addTicks(int ticks) {
        this.ticksRemaining = Math.min(ticksRemaining + ticks, 10000);
    }
}
