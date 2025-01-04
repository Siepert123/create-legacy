package com.melonstudios.createlegacy.copycat;

import com.melonstudios.createlegacy.CreateLegacy;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * The TileEntity every copycat block uses.
 * @see BlockCopycat
 * @see TileEntityCopycatRenderer
 * @since 0.1.2
 * @author Siepert
 */
public final class TileEntityCopycat extends TileEntity {
    public IBlockState copyState = null;
    public boolean requestUpdate = true;

    public boolean mustRender() {
        return world.getBlockState(pos).getValue(BlockCopycat.COPYCATTING) && copyState != null;
    }

    public void updateClients() {
        if (!world.isRemote) {
            PacketUpdateCopycat.sendToNearbyPlayers(this, 128);
        }
    }

    @Override
    public void onLoad() {
        if (requestUpdate && world.isRemote && world.getBlockState(pos).getValue(BlockCopycat.COPYCATTING)) {
            CreateLegacy.getNetworkWrapper().sendToServer(new PacketUpdateCopycat(this));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        if (copyState != null) {
            compound.setInteger("copyState", Block.getStateId(copyState));
        }

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey("copyState")) {
            copyState = Block.getStateById(compound.getInteger("copyState"));
        }
    }
}
