package com.melonstudios.createlegacy.network;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.tileentity.TileEntityChute;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;

public class PacketUpdateChute implements IMessage {
    public static void sendToNearbyPlayers(TileEntityChute chute, int range) {
        List<EntityPlayer> players = FMLCommonHandler.instance().getMinecraftServerInstance()
                .getWorld(chute.getWorld().provider.getDimension())
                .getEntitiesWithinAABB(EntityPlayer.class,
                        new AxisAlignedBB(chute.getPos().add(-range, -range, -range),
                                chute.getPos().add(range, range, range)));

        for (EntityPlayer player : players) {
            if (player instanceof EntityPlayerMP) {
                CreateLegacy.getNetworkWrapper().sendTo(new PacketUpdateChute(chute), (EntityPlayerMP) player);
            }
        }
    }
    private BlockPos pos;
    private ItemStack stack;
    private int transferCooldown;
    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        stack = ByteBufUtils.readItemStack(buf);
        transferCooldown = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        ByteBufUtils.writeItemStack(buf, stack);
        buf.writeByte(transferCooldown);
    }

    public PacketUpdateChute(TileEntityChute te) {
        this.pos = te.getPos();
        this.stack = te.getStackInSlot(0);
        this.transferCooldown = te.getTransferCooldown();
    }
    public PacketUpdateChute() {

    }

    public static class Handler implements IMessageHandler<PacketUpdateChute, IMessage> {

        @Override
        public IMessage onMessage(PacketUpdateChute message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                TileEntityChute te = (TileEntityChute) Minecraft.getMinecraft().world.getTileEntity(message.pos);
                if (te != null) {
                    te.setStack(message.stack);
                    te.setTransferCooldown(message.transferCooldown);
                }
            });
            return null;
        }
    }
}
