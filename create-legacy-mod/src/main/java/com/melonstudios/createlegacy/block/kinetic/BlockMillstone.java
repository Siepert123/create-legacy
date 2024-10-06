package com.melonstudios.createlegacy.block.kinetic;

import com.melonstudios.createlegacy.tileentity.TileEntityMillstone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockMillstone extends AbstractBlockKinetic {
    public BlockMillstone() {
        super("millstone");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityMillstone();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}
