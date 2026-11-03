package nl.melonstudios.create.tileentity.generator;

import com.melonstudios.melonlib.network.TrackedByteBuf;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import nl.melonstudios.create.block.generator.BlockFurnaceEngine;
import nl.melonstudios.create.tileentity.TileEntityOptimizedBase;

import java.io.IOException;

public class TileEntityFurnaceEngine extends TileEntityOptimizedBase {
	public TileEntityFurnaceEngine() {

	}

	@Override
	public void destroy() {
		EnumFacing facing = EnumFacing.getHorizontal(this.getBlockMetadata() & 0b0011);
		BlockPos flywheelPos = this.pos.offset(facing, 2);
		IBlockState flywheel = this.world.getBlockState(flywheelPos);
		if (flywheel.getBlock() == this.getBlockType()
				&& flywheel.getValue(BlockFurnaceEngine.VARIANT) == BlockFurnaceEngine.Variant.FLYWHEEL
				&& flywheel.getValue(BlockFurnaceEngine.FACING) == facing.rotateY()) {
			TileEntityFurnaceFlywheel te = (TileEntityFurnaceFlywheel) this.world.getTileEntity(flywheelPos);
			te.updatePoweredState(false);
			te.updateEngineState(false);
		}
	}

	@Override
	public void tick() {

	}
	@Override
	public void tickLazy() {

	}

	@Override
	public void writePacket(TrackedByteBuf buf) throws IOException {

	}
	@Override
	public void readPacket(ByteBuf buf) throws IOException {

	}
}
