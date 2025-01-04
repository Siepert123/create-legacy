package com.melonstudios.createlegacy.copycat;

import com.melonstudios.createlegacy.CreateLegacy;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

/**
 * Packet that handles both server update and client request message.
 * Every copycat block utilizes this; no need to make one yourself!
 * @since 0.1.2
 * @author Siepert
 */
public final class PacketUpdateCopycat implements IMessage {
    public static void sendToNearbyPlayers(TileEntityCopycat te, int range) {
        List<EntityPlayer> players = FMLCommonHandler.instance().getMinecraftServerInstance()
                .getWorld(te.getWorld().provider.getDimension())
                .getEntitiesWithinAABB(EntityPlayer.class,
                        new AxisAlignedBB(te.getPos().add(-range, -range, -range),
                                te.getPos().add(range, range, range)));

        for (EntityPlayer player : players) {
            if (player instanceof EntityPlayerMP) {
                CreateLegacy.getNetworkWrapper().sendTo(new PacketUpdateCopycat(te), (EntityPlayerMP) player);
            }
        }
    }

    private BlockPos pos;
    private IBlockState copyState;

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        int stateId = buf.readInt();
        copyState = stateId != 0 ? Block.getStateById(stateId) : null;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeInt(copyState != null ? Block.getStateId(copyState) : 0);
    }

    public PacketUpdateCopycat(TileEntityCopycat te) {
        pos = te.getPos();
        copyState = te.copyState;
    }
    public PacketUpdateCopycat() {

    }

    public static class Handler implements IMessageHandler<PacketUpdateCopycat, PacketUpdateCopycat> {
        @Override
        public PacketUpdateCopycat onMessage(PacketUpdateCopycat message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                TileEntity te = ctx.getServerHandler().player.world.getTileEntity(message.pos);
                if (te instanceof TileEntityCopycat) {
                    return new PacketUpdateCopycat((TileEntityCopycat) te);
                }
            }
            if (ctx.side == Side.CLIENT && message != null) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    TileEntityCopycat te = (TileEntityCopycat) Minecraft.getMinecraft().world.getTileEntity(message.pos);
                    te.copyState = message.copyState;
                });
            }
            return null;
        }
    }
}
