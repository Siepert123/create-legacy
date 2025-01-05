package com.melonstudios.createlegacy.block;

import com.melonstudios.createlegacy.CreateLegacy;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockIndustrialIronGlass extends Block {
    public BlockIndustrialIronGlass() {
        super(Material.IRON, MapColor.GRAY);
        setCreativeTab(CreateLegacy.TAB_DECORATIONS);
        setRegistryName("industrial_iron_glass");
        setUnlocalizedName("create.industrial_iron_glass");
        setHardness(15f);
        setResistance(50f);

        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        IBlockState state = blockAccess.getBlockState(pos.offset(side));
        if (state.isSideSolid(blockAccess, pos.offset(side), side.getOpposite())) return false;
        return !(state.getBlock() instanceof BlockIndustrialIronGlass);
    }



    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 0;
    }

    @Override
    public boolean isTranslucent(IBlockState state) {
        return true;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
}
