package com.melonstudios.createlegacy.contraption;

import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityBearing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContraptionBearing implements IBlockAccess {
    public void assemble() {

    }

    public void disassemble() {

    }

    protected void updateAllStates() {
        for (int x = 0; x < sizeX; x++) {
            for (int z = 0; z < sizeZ; z++) {
                for (int y = 0; y < sizeY; y++) {
                    IBlockState state = structure[x][y][z];
                    if (state != null) {
                        BlockPos pos = new BlockPos(x, y, z);
                        structure[x][y][z] = state.getActualState(this, pos);
                    }
                }
            }
        }
    }

    List<TileEntity> tileEntities = new ArrayList<>();
    Map<BlockPos, TileEntity> teMap = new HashMap<>();

    IBlockState[][][] structure;

    int sizeX, sizeY, sizeZ;
    int offsetX, offsetY, offsetZ;
    AbstractTileEntityBearing te;

    public ContraptionBearing(int sizeX, int sizeY, int sizeZ, int offsetX, int offsetY, int offsetZ, AbstractTileEntityBearing te) {
        this.sizeX  = sizeX;
        this.sizeY  = sizeY;
        this.sizeZ  = sizeZ;
        this.offsetX= offsetX;
        this.offsetY= offsetY;
        this.offsetZ= offsetZ;
        this.te     = te;
        structure   = new IBlockState[sizeX][sizeY][sizeZ];
    }

    boolean posInBounds(BlockPos pos) {
        if (pos.getX() < 0 || pos.getY() < 0 || pos.getZ() < 0) return false;
        return pos.getX() < sizeX && pos.getY() < sizeY && pos.getZ() < sizeZ;
    }

    @Nullable
    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        return teMap.get(pos);
    }

    @Override
    public int getCombinedLight(BlockPos pos, int lightValue) {
        return 15;
    }

    @Override
    public IBlockState getBlockState(BlockPos pos) {
        return posInBounds(pos) ? structure[pos.getX()][pos.getY()][pos.getZ()] : Blocks.AIR.getDefaultState();
    }

    @Override
    public boolean isAirBlock(BlockPos pos) {
        return !posInBounds(pos) || getBlockState(pos).getBlock().isAir(getBlockState(pos), this, pos);
    }

    @Override
    public Biome getBiome(BlockPos pos) {
        return te.getWorld().getBiome(pos.add(te.getPos()).add(offsetX, offsetY, offsetZ));
    }

    @Override
    public int getStrongPower(BlockPos pos, EnumFacing direction) {
        return getBlockState(pos).getStrongPower(this, pos, direction);
    }

    @Override
    public WorldType getWorldType() {
        return te.getWorld().getWorldType();
    }

    @Override
    public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
        return isAirBlock(pos) ? _default : getBlockState(pos).isSideSolid(this, pos, side);
    }
}
