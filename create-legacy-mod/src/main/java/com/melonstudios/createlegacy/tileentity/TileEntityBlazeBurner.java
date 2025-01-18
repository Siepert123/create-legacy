package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.block.BlockRender;
import com.melonstudios.createlegacy.network.PacketUpdateBlazeBurner;
import com.melonstudios.createlegacy.util.EnumBlazeLevel;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class TileEntityBlazeBurner extends TileEntity implements ITickable {
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        if (lockedState) {
            switch (blazeLevel) {
                case PASSIVE: ticksRemaining = -1; break;
                case HEATED: ticksRemaining = -2; break;
                case SUPERHEATED: ticksRemaining = -3; break;
            }
        }
        compound.setInteger("ticksRemaining", ticksRemaining);
        compound.setBoolean("lockedState", lockedState);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        ticksRemaining = compound.getInteger("ticksRemaining");
        if (lockedState = compound.getBoolean("lockedState")) {
            if (ticksRemaining == -1) blazeLevel = EnumBlazeLevel.PASSIVE;
            if (ticksRemaining == -2) blazeLevel = EnumBlazeLevel.HEATED;
            if (ticksRemaining == -3) blazeLevel = EnumBlazeLevel.SUPERHEATED;
        }
    }

    protected int ticksRemaining = 0;
    public int getTicksRemaining() {
        return ticksRemaining;
    }
    public void setTicksRemaining(int ticks) {
        ticksRemaining = ticks;
    }

    public boolean isLockedState() {
        return lockedState;
    }

    public void enforceState(EnumBlazeLevel level) {
        lockedState = true;
        blazeLevel = level;
    }

    protected boolean lockedState = false;
    public void creativeCake() {
        lockedState = true;
        switch (blazeLevel) {
            case NONE:
            case SUPERHEATED: blazeLevel = EnumBlazeLevel.PASSIVE; break;
            case PASSIVE: blazeLevel = EnumBlazeLevel.HEATED; break;
            case HEATED: blazeLevel = EnumBlazeLevel.SUPERHEATED; break;
        }
    }

    protected EnumBlazeLevel blazeLevel = EnumBlazeLevel.PASSIVE;
    public EnumBlazeLevel getBlazeLevel() {
        if (lockedState) return blazeLevel;
        if (ticksRemaining > 10000) {
            return blazeLevel = EnumBlazeLevel.SUPERHEATED;
        } else if (ticksRemaining > 0) {
            return blazeLevel = EnumBlazeLevel.HEATED;
        } else {
            return blazeLevel = EnumBlazeLevel.PASSIVE;
        }
    }
    public IBlockState getAssociatedBlazePart() {
        switch (getBlazeLevel()) {
            case PASSIVE: {
                return BlockRender.getRenderPart(BlockRender.Type.BLAZEHEAD_PASSIVE);
            }
            case HEATED: {
                return BlockRender.getRenderPart(BlockRender.Type.BLAZEHEAD_HEATED);
            }
            case SUPERHEATED: {
                return BlockRender.getRenderPart(BlockRender.Type.BLAZEHEAD_SUPERHEATED);
            }
            default: return BlockRender.getRenderPart(BlockRender.Type.SAWBLADE_Y);
        }
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
            if (ticksRemaining > 0) {
                ticksRemaining--;
                if (world.rand.nextFloat() < 0.01f) {
                    world.playSound(null, pos, SoundEvents.ENTITY_BLAZE_BURN, SoundCategory.BLOCKS, 1, 0.9f + world.rand.nextFloat() * 0.2f);
                }
            }
            if ((world.getTotalWorldTime() & 0xf) == 0) {
                PacketUpdateBlazeBurner.sendToNearbyPlayers(this, 64);
            }
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
