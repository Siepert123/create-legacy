package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createapi.network.NetworkContext;
import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.block.kinetic.BlockFurnaceEngine;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKinetic;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;

public class TileEntityFlywheel extends AbstractTileEntityKinetic {
    @Override
    protected String namePlate() {
        return "Flywheel";
    }

    public IBlockState getAssociatedFlywheelPart() {
        switch (facing()) {
            case NORTH: return renderingPart(17);
            case EAST: return renderingPart(14);
            case SOUTH: return renderingPart(15);
            case WEST: return renderingPart(16);
        }
        return renderingPart(14);
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        return getState().getValue(BlockFurnaceEngine.FACING).getOpposite() == side ? connection(1) : connection(0);
    }

    @Override
    protected void tick() {
        if (generatedRPM() == 0 || isUpdated()) return;
        NetworkContext context = new NetworkContext(world);
        passNetwork(null, null, context, false);
        context.start();

        if (world.getTotalWorldTime() % 20 == 0) {
            world.playSound(null, pos.offset(facing().rotateY()).getX() + 0.5, pos.offset(facing().rotateY()).getY() + 0.5, pos.offset(facing().rotateY()).getZ() + 0.5,
                    SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.01f, 1.0f);
        }
        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                pos.offset(facing().rotateY()).getX() + world.rand.nextFloat(),
                pos.offset(facing().rotateY()).getY() + world.rand.nextFloat(),
                pos.offset(facing().rotateY()).getZ() + world.rand.nextFloat(),
                0, 0.1f, 0);
    }

    @Override
    public float generatedSUMarkiplier() {
        return 256.0f;
    }

    protected EnumFacing facing() {
        return getState().getValue(BlockFurnaceEngine.FACING);
    }

    @Override
    public float generatedRPM() {
        return validGenerator() ? 64.0f : 0.0f;
    }

    protected boolean validGenerator() {
        IBlockState state = world.getBlockState(pos.offset(facing().rotateY()));
        if (state.getBlock() instanceof BlockFurnaceEngine) {
            if (state.getValue(BlockFurnaceEngine.VARIANT) == BlockFurnaceEngine.Variant.ENGINE) {
                if (state.getValue(BlockFurnaceEngine.FACING) == facing().rotateYCCW()) {
                    if (world.getBlockState(pos.offset(facing().rotateY(), 2)).getBlock() == Blocks.LIT_FURNACE) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
