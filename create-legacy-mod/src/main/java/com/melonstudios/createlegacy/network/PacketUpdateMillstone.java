package com.melonstudios.createlegacy.network;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.tileentity.TileEntityMillstone;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;

public class PacketUpdateMillstone implements IMessage {
    public static void sendToPlayersNearby(TileEntityMillstone te, int range) {
        List<EntityPlayer> players = FMLCommonHandler.instance().getMinecraftServerInstance()
                .getWorld(te.getWorld().provider.getDimension())
                .getEntitiesWithinAABB(EntityPlayer.class,
                        new AxisAlignedBB(te.getPos().add(-range, -range, -range),
                                te.getPos().add(range, range, range)));

        for (EntityPlayer player : players) {
            if (player instanceof EntityPlayerMP) {
                CreateLegacy.networkWrapper.sendTo(new PacketUpdateMillstone(te), (EntityPlayerMP) player);
            }
        }
    }

    private BlockPos pos;
    private boolean active;

    public PacketUpdateMillstone(BlockPos pos, boolean active) {
        this.pos = pos;
        this.active = active;
    }
    public PacketUpdateMillstone(TileEntityMillstone te) {
        this(te.getPos(), te.renderParticles());
    }
    public PacketUpdateMillstone() {}

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        active = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeBoolean(active);
    }

    public static class Handler implements IMessageHandler<PacketUpdateMillstone, IMessage> {

        @Override
        public IMessage onMessage(PacketUpdateMillstone message, MessageContext ctx) {

            return null;
        }
    }
}
