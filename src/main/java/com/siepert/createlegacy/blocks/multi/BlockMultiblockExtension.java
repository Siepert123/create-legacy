package com.siepert.createlegacy.blocks.multi;

import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.tileentity.TileEntityMultiblockExtension;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockMultiblockExtension extends Block implements ITileEntityProvider {
    public BlockMultiblockExtension() {
        super(Material.IRON);

        setRegistryName("multiblock_extension");
        setUnlocalizedName("create:multiblock_extension");

        ModBlocks.BLOCKS.add(this);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityMultiblockExtension();
    }
}
