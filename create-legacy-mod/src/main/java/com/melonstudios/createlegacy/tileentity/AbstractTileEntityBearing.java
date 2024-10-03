package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.block.kinetic.AbstractBlockBearing;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import static com.melonstudios.createlegacy.block.BlockRender.*;
import static com.melonstudios.createlegacy.block.kinetic.AbstractBlockBearing.ACTIVE;

import javax.annotation.Nullable;

public abstract class AbstractTileEntityBearing extends AbstractTileEntityKinetic {

    protected @Nullable IBlockState structure;
    @Nullable
    public IBlockState getStructure() {
        return structure;
    }
    public boolean isAssembled() {
        return structure != null;
    }
    public void assemble() {
        if (structure == null) {
            structure = world.getBlockState(pos.offset(facing()));
            world.setBlockToAir(pos.offset(facing()));
            toggleActive(true);
        }
    }
    public void disassemble() {
        if (structure != null) {
            world.setBlockState(pos.offset(facing()), structure, 3);
            structure = null;
            toggleActive(false);
        }
    }
    protected void check() {
        if (structure == null) {
            toggleActive(false);
        }
    }

    @Override
    protected void tick() {
        check();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        if (structure != null) {
            int i = Block.getIdFromBlock(structure.getBlock());
            byte b = (byte) structure.getBlock().getMetaFromState(structure);

            NBTTagCompound structureTag = new NBTTagCompound();
            structureTag.setInteger("blockID", i);
            structureTag.setByte("blockMeta", b);
            compound.setTag("structureNBT", structureTag);
        }

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey("structureNBT")) {
            NBTTagCompound structureTag = compound.getCompoundTag("structureNBT");

            structure = Block.getBlockById(structureTag.getInteger("blockID"))
                    .getStateFromMeta(structureTag.getByte("blockMeta"));
        }
    }

    public boolean shouldRenderSpinning() {
        boolean f1 = getState().getValue(ACTIVE);
        boolean f2 = structure != null;
        return f1 && f2;
    }

    public EnumFacing facing() {
        return getState().getValue(AbstractBlockBearing.FACING);
    }
    public IBlockState getAssociatedBearingPart() {
        final IBlockState render = ModBlocks.RENDER.getDefaultState();
        switch (facing()) {
            case UP: return render.withProperty(TYPE, Type.BEARING_U);
            case DOWN: return render.withProperty(TYPE, Type.BEARING_D);
            case NORTH: return render.withProperty(TYPE, Type.BEARING_N);
            case EAST: return render.withProperty(TYPE, Type.BEARING_E);
            case SOUTH: return render.withProperty(TYPE, Type.BEARING_S);
            case WEST: return render.withProperty(TYPE, Type.BEARING_W);
        }
        return render.withProperty(TYPE, Type.BEARING_U);
    }
    protected void toggleActive(boolean active) {
        if (getState().getValue(ACTIVE) != active) {
            world.setBlockState(pos, getState().withProperty(ACTIVE, active));
            this.validate();
            world.setTileEntity(pos, this);
        }
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        return getState().getValue(AbstractBlockBearing.FACING).getOpposite() == side
                ? EnumKineticConnectionType.SHAFT : EnumKineticConnectionType.NONE;
    }
}
