package com.melonstudios.createlegacy.util;

import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityBearing;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Class allowing blocks on a Mechanical Bearing to allow blocks to read other blocks on the bearing.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
public class BearingStructureHelper implements IBlockAccess {
    public BearingStructureHelper(AbstractTileEntityBearing bearingTE) {
        this.bearingTE = bearingTE;
    }

    protected BlockPos offset;
    protected IBlockState[][][] structure;
    AbstractTileEntityBearing bearingTE;

    public IBlockState[][][] getStructure() {
        return structure;
    }
    public void setStructure(IBlockState[][][] structure) {
        this.structure = structure;
    }

    @Nullable
    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        return null;
    }

    @Override
    public int getCombinedLight(BlockPos pos, int lightValue) {
        return 15;
    }

    /**
     * Finds the blockstate in the structure.
     * @param pos Position of the block.
     * @return The blockstate at that position, or air if it is out of bounds.
     */
    @Override
    public IBlockState getBlockState(BlockPos pos) {
        if (isPosInBounds(pos))
            return structure[pos.getX()][pos.getY()][pos.getZ()];
        else return Blocks.AIR.getDefaultState();
    }

    /**
     * Sets a blockstate in the structure.
     * Does nothing if the position is out of bounds.
     * @param pos Position to set block in.
     * @param state The state to set.
     * @param update Update the structure?
     */
    public void setBlockState(BlockPos pos, IBlockState state, boolean update) {
        if (isPosInBounds(pos)) {
            structure[pos.getX()][pos.getY()][pos.getZ()] = state;
            if (update) updateStructure();
        }
    }

    /**
     * Removes a block from the structure.
     * @param pos Position to set to air.
     */
    public void setBlockToAir(BlockPos pos) {
        setBlockState(pos, Blocks.AIR.getDefaultState(), true);
    }

    /**
     * Breaks a block with particles at the given position.
     * Does nothing if the position is out of bounds.
     * @param pos Position of the block to break.
     */
    public void breakBlock(BlockPos pos) {
        if (isAirBlock(pos)) return;
        if (isPosInBounds(pos)) {
            IBlockState state = getBlockState(pos);
            getActualWorld().playEvent(2001, getActualPos(pos), Block.getStateId(state));
            setBlockToAir(pos);
        }
    }

    /**
     * @return The actual world, obtained from the bearing block.
     */
    public World getActualWorld() {
        return bearingTE.getWorld();
    }

    /**
     * @param pos Position offset.
     * @return The actual position relative to the bearing.
     * @implNote May not work as intended
     */
    public BlockPos getActualPos(BlockPos pos) {
        return bearingTE.getPos().add(pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * Checks if a position is inside the structure like it should.
     * @param pos Block position to check.
     * @return True if the position is inside the structure.
     */
    public boolean isPosInBounds(BlockPos pos) {
        boolean flag1 = pos.getX() < structure.length && pos.getY() < structure[0].length && pos.getZ() < structure[0][0].length;
        boolean flag2 = pos.getX() >= 0 && pos.getY() >= 0 && pos.getZ() >= 0;
        return flag1 && flag2;
    }

    @Override
    public boolean isAirBlock(BlockPos pos) {
        return getBlockState(pos).getBlock().isAir(getBlockState(pos), this, pos);
    }

    @Override
    public Biome getBiome(BlockPos pos) {
        return bearingTE.getWorld().getBiome(bearingTE.getPos().add(pos.getX(), pos.getY(), pos.getZ()));
    }

    @Override
    public int getStrongPower(BlockPos pos, EnumFacing direction) {
        return 0;
    }

    @Override
    public WorldType getWorldType() {
        return bearingTE.getWorld().getWorldType();
    }

    @Override
    public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
        return getBlockState(pos.offset(side)).getBlock().isSideSolid(getBlockState(pos.offset(side)), this, pos.offset(side), side);
    }

    /**
     * Updates all blockstates in the structure to become their actual states.
     * Useful for stuff like fences.
     */
    public void updateStructure() {
        for (int x = 0; x < structure.length; x++) {
            for (int y = 0; y < structure[x].length; y++) {
                for (int z = 0; z < structure[x][y].length; z++) {
                    final BlockPos pos = new BlockPos(x, y, z);
                    if (isPosInBounds(pos)) {
                        structure[x][y][z] = getBlockState(pos).getBlock().getActualState(getBlockState(pos), this, pos);
                    }
                }
            }
        }
    }
}
