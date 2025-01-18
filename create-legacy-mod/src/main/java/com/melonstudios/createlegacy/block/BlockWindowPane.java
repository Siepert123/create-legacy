package com.melonstudios.createlegacy.block;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.util.IMetaName;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockWindowPane extends BlockPane implements IMetaName {
    public static final PropertyEnum<BlockPlanks.EnumType> WOOD_TYPE = BlockPlanks.VARIANT;

    public BlockWindowPane() {
        super(Material.WOOD, true);
        setRegistryName("window_wood_pane");
        setUnlocalizedName("create.window_wood_pane");

        setHarvestLevel("axe", -1);

        setHardness(2.0f);
        setResistance(5.0f);

        setSoundType(SoundType.WOOD);
        setCreativeTab(CreateLegacy.TAB_DECORATIONS);
    }

    public static void createItemModels() {
        CreateLegacy.setItemModel(ModBlocks.WINDOW_WOOD_PANE, 0, "window_pane/oak");
        CreateLegacy.setItemModel(ModBlocks.WINDOW_WOOD_PANE, 1, "window_pane/spruce");
        CreateLegacy.setItemModel(ModBlocks.WINDOW_WOOD_PANE, 2, "window_pane/birch");
        CreateLegacy.setItemModel(ModBlocks.WINDOW_WOOD_PANE, 3, "window_pane/jungle");
        CreateLegacy.setItemModel(ModBlocks.WINDOW_WOOD_PANE, 4, "window_pane/acacia");
        CreateLegacy.setItemModel(ModBlocks.WINDOW_WOOD_PANE, 5, "window_pane/dark_oak");
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, WOOD_TYPE, NORTH, EAST, SOUTH, WEST);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(WOOD_TYPE).getMetadata();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(WOOD_TYPE, BlockPlanks.EnumType.byMetadata(meta));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(WOOD_TYPE, BlockPlanks.EnumType.byMetadata(placer.getHeldItem(hand).getMetadata()));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(WOOD_TYPE).getMetadata();
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, state.getValue(WOOD_TYPE).getMetadata());
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1));
        items.add(new ItemStack(this, 1, 2));
        items.add(new ItemStack(this, 1, 3));
        items.add(new ItemStack(this, 1, 4));
        items.add(new ItemStack(this, 1, 5));
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        if (side == EnumFacing.UP) return shouldRenderUp(blockState, blockAccess, pos);
        if (side == EnumFacing.DOWN) return shouldRenderDown(blockState, blockAccess, pos);

        return blockAccess.getBlockState(pos.offset(side)).isSideSolid(blockAccess, pos.offset(side), side.getOpposite());
    }
    private boolean shouldRenderUp(IBlockState state, IBlockAccess blockAccess, BlockPos pos) {
        IBlockState upState = blockAccess.getBlockState(pos.up());

        if (upState.getBlock() instanceof BlockWindowPane) {
            boolean flag1 = upState.getValue(NORTH) != state.getValue(NORTH);
            boolean flag2 = upState.getValue(EAST) != state.getValue(EAST);
            boolean flag3 = upState.getValue(SOUTH) != state.getValue(SOUTH);
            boolean flag4 = upState.getValue(WEST) != state.getValue(WEST);
            return (flag1 || flag2 || flag3 || flag4);
        }

        return !upState.isSideSolid(blockAccess, pos.up(), EnumFacing.DOWN);
    }
    private boolean shouldRenderDown(IBlockState state, IBlockAccess blockAccess, BlockPos pos) {
        IBlockState downState = blockAccess.getBlockState(pos.down());

        if (downState.getBlock() instanceof BlockWindowPane) {
            boolean flag1 = downState.getValue(NORTH) != state.getValue(NORTH);
            boolean flag2 = downState.getValue(EAST) != state.getValue(EAST);
            boolean flag3 = downState.getValue(SOUTH) != state.getValue(SOUTH);
            boolean flag4 = downState.getValue(WEST) != state.getValue(WEST);
            return (flag1 || flag2 || flag3 || flag4);
        }

        return !downState.isSideSolid(blockAccess, pos.down(), EnumFacing.UP);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        final String base = "tile.create.window_";

        return base + BlockPlanks.EnumType.byMetadata(stack.getMetadata()).getUnlocalizedName() + "_pane";
    }
}
