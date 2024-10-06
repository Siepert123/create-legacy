package com.melonstudios.createlegacy.network;

import com.melonstudios.createlegacy.tileentity.TileEntityDepot;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRequestUpdateDepot implements IMessage {
    private BlockPos pos;
    private int dimension;

    public PacketRequestUpdateDepot(BlockPos pos, int dimension) {
        this.pos = pos;
        this.dimension = dimension;
    }

    public PacketRequestUpdateDepot(TileEntityDepot te) {
        this(te.getPos(), te.getWorld().provider.getDimension());
    }

    public PacketRequestUpdateDepot() {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeInt(dimension);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        dimension = buf.readInt();
    }

    public static class Handler implements IMessageHandler<PacketRequestUpdateDepot, PacketUpdateDepot> {

        @Override
        public PacketUpdateDepot onMessage(PacketRequestUpdateDepot message, MessageContext ctx) {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            TileEntityDepot te = (TileEntityDepot) world.getTileEntity(message.pos);
            if (te != null) {
                return new PacketUpdateDepot(te);
            } else return null;
        }
    }
}
