package com.melonstudios.createlegacy.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

/**
 * ??? why did I make this
 */
public class BlockRenderBearingAnchor extends Block {
    public BlockRenderBearingAnchor() {
        super(Material.ROCK);

        setRegistryName("render_bearing_anchor");
    }

    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }
}
