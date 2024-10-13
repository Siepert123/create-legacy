package com.melonstudios.createlegacy.network;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.tileentity.TileEntityPress;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;

public class PacketUpdatePress implements IMessage {
    public static void sendToPlayersNearby(TileEntityPress te, int range) {
        List<EntityPlayer> players = FMLCommonHandler.instance().getMinecraftServerInstance()
                .getWorld(te.getWorld().provider.getDimension())
                .getEntitiesWithinAABB(EntityPlayer.class,
                        new AxisAlignedBB(te.getPos().add(-range, -range, -range),
                                te.getPos().add(range, range, range)));

        for (EntityPlayer player : players) {
            if (player instanceof EntityPlayerMP) {
                CreateLegacy.getNetworkWrapper().sendTo(new PacketUpdatePress(te), (EntityPlayerMP) player);
            }
        }
    }

    private int previousProgress;
    private int progress;
    private BlockPos pos;

    public PacketUpdatePress() {
    }

    public PacketUpdatePress(TileEntityPress te) {
        this.previousProgress = te.getPreviousProgress();
        this.progress = te.getProgress();
        this.pos = te.getPos();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        previousProgress = buf.readInt();
        progress = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeInt(previousProgress);
        buf.writeInt(progress);
    }

    public static class Handler implements IMessageHandler<PacketUpdatePress, IMessage> {

        @Override
        public IMessage onMessage(PacketUpdatePress message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                TileEntityPress te = (TileEntityPress) Minecraft.getMinecraft().world.getTileEntity(message.pos);
                te.setPreviousProgress(message.previousProgress);
                te.setProgress(message.progress);
            });
            return null;
        }
    }
}
