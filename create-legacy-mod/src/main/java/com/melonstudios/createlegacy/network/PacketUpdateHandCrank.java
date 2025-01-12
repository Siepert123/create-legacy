package com.melonstudios.createlegacy.network;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.tileentity.TileEntityHandCrank;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;

public class    PacketUpdateHandCrank implements IMessage {
    public static void sendToPlayersNearby(TileEntityHandCrank te, int range) {
        List<EntityPlayer> players = te.getWorld().getEntitiesWithinAABB(EntityPlayer.class,
                new AxisAlignedBB(
                te.getPos().add(-range, -range, -range),
                te.getPos().add(range, range, range)
                )
        );

        for (EntityPlayer player : players) {
            if (player instanceof EntityPlayerMP) {
                CreateLegacy.getNetworkWrapper().sendTo(new PacketUpdateHandCrank(te), (EntityPlayerMP) player);
            }
        }
    }
    private BlockPos pos;

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
    }

    public PacketUpdateHandCrank(TileEntityHandCrank crank) {
        this.pos = crank.getPos();
    }
    public PacketUpdateHandCrank() {

    }

    public static class Handler implements IMessageHandler<PacketUpdateHandCrank, IMessage> {

        @Override
        public IMessage onMessage(PacketUpdateHandCrank message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                TileEntityHandCrank te = (TileEntityHandCrank) Minecraft.getMinecraft().world.getTileEntity(message.pos);
                if (te != null) te.resetTimer();
            });
            return null;
        }
    }
}
