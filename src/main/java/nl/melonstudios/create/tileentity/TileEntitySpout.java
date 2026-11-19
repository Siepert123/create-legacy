package nl.melonstudios.create.tileentity;

import com.melonstudios.melonlib.network.TrackedByteBuf;
import com.melonstudios.melonlib.recipe.FluidIngredient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TileEntitySpout extends TileEntityOptimizedBase {
	public final FluidTank tank = new FluidTank(1000);

	public TileEntitySpout() {
		this.tank.setTileEntity(this);
	}

	@Override
	public void tick() {

	}
	@Override
	public void tickLazy() {

	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		this.tank.readFromNBT(compound.getCompoundTag("Tank"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		compound.setTag("Tank", this.tank.writeToNBT(new NBTTagCompound()));

		return compound;
	}

	@Override
	public void writePacket(TrackedByteBuf buf) throws IOException {
		{
			FluidStack tank = this.tank.getFluid();
			if (tank != null) {
				buf.writeBoolean(true);
				String id = FluidRegistry.getFluidName(tank.getFluid());
				int amount = tank.amount;
				NBTTagCompound tag = tank.tag;
				buf.writeInt(id.length());
				buf.internal().writeCharSequence(id, StandardCharsets.UTF_8);
				buf.append(id.length());
				buf.writeInt(amount);
				if (tag != null) {
					buf.writeBoolean(true);
					ByteBuf temp = Unpooled.buffer();
					new PacketBuffer(temp).writeCompoundTag(tag);
					buf.writeBytes(temp);
				} else {
					buf.writeBoolean(false);
				}
			} else buf.writeBoolean(false);
		}
	}
	@Override
	public void readPacket(ByteBuf buf) throws IOException {
		{
			FluidStack tank;
			if (buf.readBoolean()) {
				int len = buf.readInt();
				String id = buf.readCharSequence(len, StandardCharsets.UTF_8).toString();
				int amount = buf.readInt();
				NBTTagCompound nbt = buf.readBoolean() ? new PacketBuffer(buf).readCompoundTag() : null;
				tank = new FluidStack(FluidRegistry.getFluid(id), amount, nbt);
			} else tank = null;
			this.tank.setFluid(tank);
		}
	}
}
