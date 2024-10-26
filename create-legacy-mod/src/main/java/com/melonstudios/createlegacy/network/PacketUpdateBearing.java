package com.melonstudios.createlegacy.network;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityBearing;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;

public class PacketUpdateBearing implements IMessage {
    public static void sendToPlayersNearby(AbstractTileEntityBearing te, int range) {
        List<EntityPlayer> players = te.getWorld().getEntitiesWithinAABB(EntityPlayer.class,
                new AxisAlignedBB(
                        te.getPos().add(-range, -range, -range),
                        te.getPos().add(range, range, range)
                )
        );

        for (EntityPlayer player : players) {
            if (player instanceof EntityPlayerMP) {
                CreateLegacy.getNetworkWrapper().sendTo(new PacketUpdateBearing(te), (EntityPlayerMP) player);
            }
        }
    }
    private BlockPos pos;
    private int stateID;
    private float previousAngle;
    private float angle;

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        stateID = buf.readInt();
        previousAngle = buf.readFloat();
        angle = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeInt(stateID);
        buf.writeFloat(previousAngle);
        buf.writeFloat(angle);
    }

    public PacketUpdateBearing() {

    }

    public PacketUpdateBearing(AbstractTileEntityBearing te) {
        pos = te.getPos();
        if (te.getStructure() != null) {
            stateID = Block.getStateId(te.getStructure());
        } else stateID = 0;
        previousAngle = te.getPreviousAngle();
        angle = te.getAngle();
    }

    public static class Handler implements IMessageHandler<PacketUpdateBearing, IMessage> {

        @Override
        public IMessage onMessage(PacketUpdateBearing message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                AbstractTileEntityBearing te = (AbstractTileEntityBearing) Minecraft.getMinecraft().world.getTileEntity(message.pos);
                te.overrideStructure(message.stateID == 0 ? null : Block.getStateById(message.stateID));
                te.setPreviousAngle(message.previousAngle);
                te.setAngle(message.angle);
            });
            return null;
        }
    }
}
