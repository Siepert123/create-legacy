package com.melonstudios.createlegacy.block;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.util.IMetaName;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
public class BlockWindow extends Block implements IMetaName {
    public static final PropertyEnum<BlockPlanks.EnumType> WOOD_TYPE = BlockPlanks.VARIANT;

    private final boolean metal;

    public BlockWindow(boolean isMetal) {
        super(isMetal ? Material.IRON : Material.WOOD);

        this.metal = isMetal;

        setRegistryName(isMetal ? "window_iron" : "window_wood");
        setUnlocalizedName(isMetal ? "create.window_iron" : "create.window_wood");
        if (!isMetal) {
            this.setDefaultState(this.blockState.getBaseState()
                    .withProperty(WOOD_TYPE, BlockPlanks.EnumType.OAK));
        } else {
            this.setDefaultState(this.blockState.getBaseState());
        }

        setHardness(isMetal ? 5 : 2);
        setResistance(isMetal ? 10 : 5);

        setLightOpacity(0);

        setCreativeTab(CreateLegacy.TAB_DECORATIONS);

        setHarvestLevel(isMetal ? "pickaxe" : "axe", isMetal ? 1 : -1);
    }

    public static void createItemModels() {
        CreateLegacy.setItemModel(ModBlocks.WINDOW_WOOD, 0, "window/oak");
        CreateLegacy.setItemModel(ModBlocks.WINDOW_WOOD, 1, "window/spruce");
        CreateLegacy.setItemModel(ModBlocks.WINDOW_WOOD, 2, "window/birch");
        CreateLegacy.setItemModel(ModBlocks.WINDOW_WOOD, 3, "window/jungle");
        CreateLegacy.setItemModel(ModBlocks.WINDOW_WOOD, 4, "window/acacia");
        CreateLegacy.setItemModel(ModBlocks.WINDOW_WOOD, 5, "window/dark_oak");

        CreateLegacy.setItemModel(ModBlocks.WINDOW_IRON, "window/iron");
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isTopSolid(IBlockState state) {
        return true;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        if (metal) {
            return new BlockStateContainer(this);
        } else {
            return new BlockStateContainer(this, WOOD_TYPE);
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return metal ?
                "tile.create.window_iron" :
                "tile.create.window_" + BlockPlanks.EnumType.byMetadata(stack.getMetadata()).getUnlocalizedName();
    }

    @Override
    public int damageDropped(IBlockState state) {
        return metal ? 0 : state.getValue(WOOD_TYPE).getMetadata();
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        if (metal) {
            items.add(new ItemStack(this, 1));
        } else {
            for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
                items.add(new ItemStack(this, 1, type.getMetadata()));
            }
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return metal ? getDefaultState() : getDefaultState().withProperty(WOOD_TYPE, BlockPlanks.EnumType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return metal ? 0 : state.getValue(WOOD_TYPE).getMetadata();
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return metal ? MapColor.IRON : state.getValue(WOOD_TYPE).getMapColor();
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        return metal ? SoundType.METAL : SoundType.WOOD;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing,
                                            float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return metal ? getDefaultState() :
                getDefaultState().withProperty(WOOD_TYPE, BlockPlanks.EnumType.byMetadata(meta));
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        IBlockState state = blockAccess.getBlockState(pos.offset(side));
        if (state == blockState) return false;
        else return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }
}