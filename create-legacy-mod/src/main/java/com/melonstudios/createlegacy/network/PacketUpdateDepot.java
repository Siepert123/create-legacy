package com.melonstudios.createlegacy.network;

import com.melonstudios.createlegacy.tileentity.TileEntityDepot;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateDepot implements IMessage {
    private BlockPos pos;
    private ItemStack stack;
    private ItemStack output;

    public PacketUpdateDepot(BlockPos pos, ItemStack stack, ItemStack output) {
        this.pos = pos;
        this.stack = stack;
        this.output = output;
    }

    public PacketUpdateDepot(TileEntityDepot te) {
        this(te.getPos(), te.getStack(), te.getOutput());
    }

    public PacketUpdateDepot() {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        stack = ByteBufUtils.readItemStack(buf);
        output = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        ByteBufUtils.writeItemStack(buf, stack);
        ByteBufUtils.writeItemStack(buf, output);
    }

    public static class Handler implements IMessageHandler<PacketUpdateDepot, IMessage> {

        @Override
        public IMessage onMessage(PacketUpdateDepot message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                TileEntityDepot te = (TileEntityDepot) Minecraft.getMinecraft().world.getTileEntity(message.pos);
                te.setStack(message.stack);
                te.setOutput(message.output);
            });
            return null;
        }
    }
}
