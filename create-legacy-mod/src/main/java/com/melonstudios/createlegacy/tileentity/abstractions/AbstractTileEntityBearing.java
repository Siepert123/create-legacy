package com.melonstudios.createlegacy.tileentity.abstractions;

import com.melonstudios.createlegacy.block.BlockRender;
import com.melonstudios.createlegacy.block.BlockRenderBearingAnchor;
import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.block.kinetic.AbstractBlockBearing;
import com.melonstudios.createlegacy.contraption.ContraptionBearing;
import com.melonstudios.createlegacy.network.PacketUpdateBearing;
import com.melonstudios.createlegacy.util.BearingStructureHelper;
import com.melonstudios.createlegacy.util.EnumContraptionAssemblyMode;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

import static com.melonstudios.createlegacy.block.kinetic.AbstractBlockBearing.ACTIVE;
import static com.melonstudios.createlegacy.block.kinetic.AbstractBlockBearing.FACING;

public abstract class AbstractTileEntityBearing extends AbstractTileEntityKinetic {

    protected ContraptionBearing contraption;

    public ContraptionBearing getContraption() {
        return contraption;
    }
    public void overrideContraption(ContraptionBearing contraption) {
        this.contraption = contraption;
    }

    protected @Nullable IBlockState structure;
    protected @Nullable TileEntity structureTE;
    @Nullable
    public IBlockState getStructure() {
        return structure;
    }
    @Nullable
    public TileEntity getStructureTE() {
        return structureTE;
    }

    public void overrideStructure(@Nullable IBlockState structure) {
        this.structure = structure;
    }
    public boolean isAssembled() {
        return structure != null;
    }
    public void assemble() {
        toggleActive(true);
        if (contraption != null) contraption.assemble();
    }
    public void disassemble() {
        toggleActive(false);
        if (contraption != null) contraption.disassemble();
        angle = 0.0f;
        previousAngle = 0.0f;
    }
    protected void check() {
        if (contraption == null) {
            toggleActive(false);
        } if (contraption != null) {
            toggleActive(true);
        }

        previousAngle %= 360;
        angle %= 360;
    }

    @Override
    protected void tick() {
        if ((world.getTotalWorldTime() & 0xff) == 0xff) PacketUpdateBearing.sendToPlayersNearby(this, 128);
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

    public void setPreviousAngle(float angle) {
        this.previousAngle = angle;
    }
    public void setAngle(float angle) {
        this.angle = angle;
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
