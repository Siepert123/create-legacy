package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.CreateConfig;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import com.melonstudios.createlegacy.util.NetworkContext;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;

public abstract class AbstractTileEntityKinetic extends TileEntity {
    protected int speed = 64;
    public int speed() {
        return this.speed;
    }
    public void updateSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return CreateConfig.kineticBlocksRenderDistanceSquared;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    protected abstract String namePlate();

    @Nullable
    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(String.format("%s (%s RS)", namePlate(), speed()));
    }

    public final boolean shifted(@Nullable EnumFacing.Axis axis) {
        if (axis == null) return shifted(EnumFacing.Axis.Y);
        switch (axis) {
            case X:
                return Math.abs(pos.getY()) % 2 == Math.abs(pos.getZ()) % 2;
            case Y:
                return Math.abs(pos.getX()) % 2 == Math.abs(pos.getZ()) % 2;
            case Z:
                return Math.abs(pos.getY()) % 2 == Math.abs(pos.getX()) % 2;
        }
        return false;
    }

    public int consumesSU() {
        return 0;
    }
    public int generatesSU() {
        return 0;
    }
    public int generatesRS() {
        return 0;
    }

    public abstract EnumKineticConnectionType getConnectionType(EnumFacing side);

}
