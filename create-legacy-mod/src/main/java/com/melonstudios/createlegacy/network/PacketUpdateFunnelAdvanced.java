package com.melonstudios.createlegacy.network;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.tileentity.TileEntityFunnelAdvanced;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;

public class PacketUpdateFunnelAdvanced implements IMessage {
    public static void sendToPlayersNearby(TileEntityFunnelAdvanced te, int range) {
        List<EntityPlayer> players = te.getWorld().getEntitiesWithinAABB(EntityPlayer.class,
                new AxisAlignedBB(
                        te.getPos().add(-range, -range, -range),
                        te.getPos().add(range, range, range)
                ));

        for (EntityPlayer player : players) {
            if (player instanceof EntityPlayerMP) {
                CreateLegacy.getNetworkWrapper().sendTo(new PacketUpdateFunnelAdvanced(te), (EntityPlayerMP) player);
            }
        }
    }

    private BlockPos pos;
    private ItemStack filter;

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        filter = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        ByteBufUtils.writeItemStack(buf, filter);
    }

    public PacketUpdateFunnelAdvanced(TileEntityFunnelAdvanced te) {
        pos = te.getPos();
        filter = te.getFilter();
    }

    public PacketUpdateFunnelAdvanced() {

    }

    public static class Handler implements IMessageHandler<PacketUpdateFunnelAdvanced, IMessage> {

        @Override
        public IMessage onMessage(PacketUpdateFunnelAdvanced message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                TileEntityFunnelAdvanced te = (TileEntityFunnelAdvanced) Minecraft.getMinecraft().world.getTileEntity(message.pos);
                if (te != null) te.setFilter(message.filter);
            });
            return null;
        }
    }
}
