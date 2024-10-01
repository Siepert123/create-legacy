package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.block.kinetic.BlockRotator;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import com.melonstudios.createlegacy.util.NetworkContext;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

/**
 * what are you even on about anymore,
 * <p>
 * this is schizo levels,
 * <p>
 * <strong>the cog</strong>
 * <p>
 * <h2>THE COG IS LYING TO ME</h2>
 */
public class TileEntityCog extends AbstractTileEntityKinetic {
    @Override
    protected String namePlate() {
        return "Cog";
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() != ModBlocks.ROTATOR) return EnumKineticConnectionType.NONE;

        if (state.getValue(BlockRotator.AXIS) == side.getAxis()) return EnumKineticConnectionType.SHAFT;

        if (state.getValue(BlockRotator.AXIS).isVertical()) {
            return EnumKineticConnectionType.COG;
        } else {
            return EnumKineticConnectionType.COG_ALT;
        }
    }
}
