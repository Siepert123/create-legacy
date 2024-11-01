package com.melonstudios.createlegacy.contraption;

import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityBearing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.List;

public class ContraptionBearing {
    public void assemble() {

    }

    public void disassemble() {

    }

    List<TileEntity> tileEntities = new ArrayList<>();

    IBlockState[][][] structure;

    int sizeX, sizeY, sizeZ;
    AbstractTileEntityBearing te;

    public ContraptionBearing(int sizeX, int sizeY, int sizeZ, AbstractTileEntityBearing te) {
        this.sizeX  = sizeX;
        this.sizeY  = sizeY;
        this.sizeZ  = sizeZ;
        this.te     = te;
    }
}
