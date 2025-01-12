package com.melonstudios.createlegacy.network;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.copycat.TileEntityCopycat;
import com.melonstudios.createlegacy.tileentity.TileEntityCreativeMotor;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

public final class PacketUpdateCreativeMotor implements IMessage {
    public static void sendToNearbyPlayers(TileEntityCreativeMotor te, int range) {
        List<EntityPlayer> players = FMLCommonHandler.instance().getMinecraftServerInstance()
                .getWorld(te.getWorld().provider.getDimension())
                .getEntitiesWithinAABB(EntityPlayer.class,
                        new AxisAlignedBB(te.getPos().add(-range, -range, -range),
                                te.getPos().add(range, range, range)));

        for (EntityPlayer player : players) {
            if (player instanceof EntityPlayerMP) {
                CreateLegacy.getNetworkWrapper().sendTo(new PacketUpdateCreativeMotor(te), (EntityPlayerMP) player);
            }
        }
    }

    private BlockPos pos;
    private int requestedSpeed;

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        requestedSpeed = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeInt(requestedSpeed);
    }

    public PacketUpdateCreativeMotor(TileEntityCreativeMotor te) {
        pos = te.getPos();
        requestedSpeed = te.requestedSpeed;
    }
    public PacketUpdateCreativeMotor() {

    }

    public static class Handler implements IMessageHandler<PacketUpdateCreativeMotor, PacketUpdateCreativeMotor> {
        @Override
        public PacketUpdateCreativeMotor onMessage(PacketUpdateCreativeMotor message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                TileEntity te = ctx.getServerHandler().player.world.getTileEntity(message.pos);
                if (te instanceof TileEntityCreativeMotor) {
                    return new PacketUpdateCreativeMotor((TileEntityCreativeMotor) te);
                }
            }
            if (ctx.side == Side.CLIENT && message != null) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    TileEntityCreativeMotor te = (TileEntityCreativeMotor) Minecraft.getMinecraft().world.getTileEntity(message.pos);
                    if (te != null) te.requestedSpeed = message.requestedSpeed;
                });
            }
            return null;
        }
    }
}
