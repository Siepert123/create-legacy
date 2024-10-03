package com.melonstudios.createlegacy.block.kinetic;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

@SuppressWarnings("deprecation")
public abstract class AbstractBlockKinetic extends Block implements ITileEntityProvider {
    public AbstractBlockKinetic(String registry) {
        super(Material.ROCK);

        setRegistryName(registry);
        setUnlocalizedName("create." + registry);

        setSoundType(SoundType.WOOD);

        setHardness(2.5f);
        setResistance(5.0f);

        setCreativeTab(CreateLegacy.TAB_KINETICS);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 0;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isTranslucent(IBlockState state) {
        return true;
    }

    @Override
    public boolean isNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public static void setItemModels() {
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ROTATOR),
                0, "rotator/shaft");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ROTATOR),
                1, "rotator/cog");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.SHAFT_ENCASED),
                0, "encased_shaft/andesite");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.SHAFT_ENCASED),
                1, "encased_shaft/brass");

        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.SAW));

        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.NETWORK_INSPECTOR),
                0, "network_inspector/speed");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.NETWORK_INSPECTOR),
                1, "network_inspector/stress");

        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.WATER_WHEEL));

        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.CHIGWANKER));
    }
}
