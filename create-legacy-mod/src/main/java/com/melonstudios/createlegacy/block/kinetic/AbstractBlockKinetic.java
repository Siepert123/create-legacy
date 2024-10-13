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

/**
 * Base class for kinetic blocks cuz I am lazy :p
 */
@SuppressWarnings("deprecation")
public abstract class AbstractBlockKinetic extends Block implements ITileEntityProvider {
    public AbstractBlockKinetic(String registry) {
        super(Material.ROCK);

        setRegistryName(registry);
        setUnlocalizedName("create." + registry);

        setHarvestLevel("pickaxe", 0);

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
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.KINETIC_UTILITY),
                0, "kinetic_utility/clutch");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.KINETIC_UTILITY),
                1, "kinetic_utility/gearshift");

        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.SAW));
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.BEARING),
                "bearing/normal");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.PRESS));
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.MILLSTONE));
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.FAN));
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.DRILL));

        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.NETWORK_INSPECTOR),
                0, "network_inspector/speed");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.NETWORK_INSPECTOR),
                1, "network_inspector/stress");

        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.HAND_CRANK));
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.WATER_WHEEL));
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.FURNACE_ENGINE),
                0, "furnace_engine/engine");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.FURNACE_ENGINE),
                1, "furnace_engine/flywheel");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.CREATIVE_MOTOR));

        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.CHIGWANKER));
    }
}
