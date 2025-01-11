package com.melonstudios.createlegacy.network;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.tileentity.TileEntityBlazeBurner;
import com.melonstudios.createlegacy.util.EnumBlazeLevel;
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

public final class PacketUpdateBlazeBurner implements IMessage {
    public static void sendToNearbyPlayers(TileEntityBlazeBurner te, int range) {
        List<EntityPlayer> players = FMLCommonHandler.instance().getMinecraftServerInstance()
                .getWorld(te.getWorld().provider.getDimension())
                .getEntitiesWithinAABB(EntityPlayer.class,
                        new AxisAlignedBB(te.getPos().add(-range, -range, -range),
                                te.getPos().add(range, range, range)));

        for (EntityPlayer player : players) {
            if (player instanceof EntityPlayerMP) {
                CreateLegacy.getNetworkWrapper().sendTo(new PacketUpdateBlazeBurner(te), (EntityPlayerMP) player);
            }
        }
    }

    private BlockPos pos;
    private int fuel;

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        fuel = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeInt(fuel);
    }

    public PacketUpdateBlazeBurner(TileEntityBlazeBurner te) {
        pos = te.getPos();
        if (te.isLockedState()) {
            if (te.getBlazeLevel() == EnumBlazeLevel.PASSIVE) fuel = -1;
            if (te.getBlazeLevel() == EnumBlazeLevel.HEATED) fuel = -2;
            if (te.getBlazeLevel() == EnumBlazeLevel.SUPERHEATED) fuel = -3;
        } else fuel = te.getTicksRemaining();
    }
    public PacketUpdateBlazeBurner() {

    }

    public static class Handler implements IMessageHandler<PacketUpdateBlazeBurner, IMessage> {
        @Override
        public PacketUpdateCreativeMotor onMessage(PacketUpdateBlazeBurner message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                TileEntityBlazeBurner te = (TileEntityBlazeBurner) Minecraft.getMinecraft().world.getTileEntity(message.pos);
                if (message.fuel < 0) {
                    switch (message.fuel) {
                        case -1: te.enforceState(EnumBlazeLevel.PASSIVE); break;
                        case -2: te.enforceState(EnumBlazeLevel.HEATED); break;
                        case -3: te.enforceState(EnumBlazeLevel.SUPERHEATED); break;
                    }
                } else {
                    te.setTicksRemaining(message.fuel);
                }
            });
            return null;
        }
    }
}
