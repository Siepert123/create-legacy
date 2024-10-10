package com.melonstudios.createlegacy.tileentity.abstractions;

import com.melonstudios.createlegacy.block.BlockRender;
import com.melonstudios.createlegacy.block.BlockRenderBearingAnchor;
import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.block.kinetic.AbstractBlockBearing;
import com.melonstudios.createlegacy.util.BearingStructureHelper;
import com.melonstudios.createlegacy.util.EnumContraptionAssemblyMode;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import static com.melonstudios.createlegacy.block.kinetic.AbstractBlockBearing.ACTIVE;
import static com.melonstudios.createlegacy.block.kinetic.AbstractBlockBearing.FACING;

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
            if (world.getBlockState(pos.offset(facing())).getBlock() == Blocks.AIR) return;
            if (world.getBlockState(pos.offset(facing())).getMobilityFlag() == EnumPushReaction.BLOCK) return;
            structure = world.getBlockState(pos.offset(facing()));
            world.setBlockToAir(pos.offset(facing()));
            toggleActive(true);
        }
    }
    public void disassemble() {
        if (structure != null) {
            if (!world.getBlockState(pos.offset(facing())).getMaterial().isReplaceable()) return;
            world.setBlockState(pos.offset(facing()), structure, 3);
            structure = null;
        }
        toggleActive(false);
        angle = 0.0f;
        previousAngle = 0.0f;
    }
    protected void check() {
        if (structure == null) {
            toggleActive(false);
        } if (structure != null) {
            toggleActive(true);
        }

        previousAngle %= 360;
        angle %= 360;
    }

    public void delete() {
        if (structure != null) {
            world.setBlockState(pos.offset(facing()), structure, 3);
        }
    }

    @Override
    protected void tick() {
        check();
        patentedRotationTechnology();
    }

    protected float previousAngle = 0.0f;
    protected float angle = 0.0f;
    protected void patentedRotationTechnology() {
        previousAngle = angle;
        angle += speed() * 0.3f;
    }
    public float getPreviousAngle() {
        return previousAngle;
    }
    public float getAngle() {
        return angle;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        if (structure != null) {
            NBTTagCompound structureTag = new NBTTagCompound();
            structureTag.setInteger("stateID", Block.getStateId(structure));
            compound.setTag("structureNBT", structureTag);

            compound.setFloat("angle", angle);
        }

        compound.setInteger("assemblyMode", assemblyMode.toId());

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey("structureNBT")) {
            NBTTagCompound structureTag = compound.getCompoundTag("structureNBT");

            structure = Block.getStateById(structureTag.getInteger("stateID"));
        }

        assemblyMode = EnumContraptionAssemblyMode.fromId(compound.getInteger("assemblyMode"));

        angle = compound.getFloat("angle");
    }

    protected BearingStructureHelper structureHelper = new BearingStructureHelper(this);
    public BearingStructureHelper getStructureHelper() {
        return structureHelper;
    }

    public boolean shouldRenderSpinning() {
        boolean f1 = getState().getValue(ACTIVE);
        boolean f2 = structure != null;
        return f1 && f2;
    }

    public EnumFacing facing() {
        return world.getBlockState(pos).getValue(FACING);
    }

    public void breakBlock() {
        if (structure != null) structure.getBlock().dropBlockAsItem(world, pos, structure, 0);
    }

    public IBlockState getAssociatedBearingPart() {
        final IBlockState render = ModBlocks.RENDER_BEARING_ANCHOR.getDefaultState();
        return render.withProperty(BlockRenderBearingAnchor.FACING, facing());
    }
    public IBlockState getAssociatedShaftPart() {
        final IBlockState render = ModBlocks.RENDER.getDefaultState();
        switch (facing()) {
            case UP: return render.withProperty(BlockRender.TYPE, BlockRender.Type.SHAFT_D);
            case DOWN: return render.withProperty(BlockRender.TYPE, BlockRender.Type.SHAFT_U);
            case NORTH: return render.withProperty(BlockRender.TYPE, BlockRender.Type.SHAFT_S);
            case EAST: return render.withProperty(BlockRender.TYPE, BlockRender.Type.SHAFT_W);
            case SOUTH: return render.withProperty(BlockRender.TYPE, BlockRender.Type.SHAFT_N);
            case WEST: return render.withProperty(BlockRender.TYPE, BlockRender.Type.SHAFT_E);
        }
        return render;
    }

    protected void toggleActive(boolean active) {
        if (getState().getValue(ACTIVE) != active) {
            world.setBlockState(pos, getState().withProperty(ACTIVE, active));
            this.validate();
            world.setTileEntity(pos, this);
        }

        if (!active) angle = 0;
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        return getState().getValue(AbstractBlockBearing.FACING).getOpposite() == side
                ? EnumKineticConnectionType.SHAFT : EnumKineticConnectionType.NONE;
    }

    @Override
    public void onLoad() {
        if (structure != null) {
            toggleActive(true);
        }
    }

    protected EnumContraptionAssemblyMode assemblyMode = EnumContraptionAssemblyMode.DISASSEMBLE_ALWAYS;
    public void setAssemblyMode(EnumContraptionAssemblyMode mode) {
        assemblyMode = mode;
    }
    public EnumContraptionAssemblyMode getAssemblyMode() {
        return assemblyMode;
    }
    public void cycleAssemblyMode() {
        setAssemblyMode(EnumContraptionAssemblyMode.values()[(getAssemblyMode().ordinal() + 1) % 3]);
    }
}
