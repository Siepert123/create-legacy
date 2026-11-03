package nl.melonstudios.create.tileentity.generator;

import com.melonstudios.melonlib.misc.AABB;
import com.melonstudios.melonlib.network.TrackedByteBuf;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import nl.melonstudios.create.tileentity.TileEntityKineticGeneratorBase;

import java.io.IOException;

public class TileEntityFurnaceFlywheel extends TileEntityKineticGeneratorBase {
	private boolean powered = false;
	public boolean engine = false;

	public TileEntityFurnaceFlywheel() {

	}

	public void updatePoweredState(boolean powered) {
		this.powered = powered;
		this.updateGeneratedRotation();
	}
	public void updateEngineState(boolean engine) {
		this.engine = engine;
		this.sync();
	}
	@Override
	public float getGeneratedSpeed() {
		return this.powered ? 64.0F : 0.0F;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		compound.setBoolean("powered", this.powered);
		compound.setBoolean("engine", this.engine);

		return compound;
	}
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		this.powered = compound.getBoolean("powered");
		this.engine = compound.getBoolean("engine");
	}

	@Override
	public void writePacket(TrackedByteBuf buf) throws IOException {
		super.writePacket(buf);

		buf.writeBoolean(this.powered);
		buf.writeBoolean(this.engine);
	}
	@Override
	public void readPacket(ByteBuf buf) throws IOException {
		super.readPacket(buf);

		this.powered = buf.readBoolean();
		this.engine = buf.readBoolean();
	}

	public EnumFacing getOutputFacing() {
		return EnumFacing.getHorizontal(this.getBlockMetadata() & 0b0011).getOpposite();
	}

	@Override
	protected AxisAlignedBB createRenderBoundingBox() {
		return AABB.wrap(this.pos, 3);
	}
}
