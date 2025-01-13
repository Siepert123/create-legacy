package com.melonstudios.createlegacy.block;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.network.PacketUpdateFunnelAdvanced;
import com.melonstudios.createlegacy.tileentity.TileEntityFunnel;
import com.melonstudios.createlegacy.tileentity.TileEntityFunnelAdvanced;
import com.melonstudios.createlegacy.util.IMetaName;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockFunnel extends Block implements ITileEntityProvider, IMetaName {
    public BlockFunnel() {
        super(Material.IRON);
        setRegistryName("funnel");
        setUnlocalizedName("create.funnel");

        this.setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.NORTH)
                .withProperty(BRASS, false).withProperty(DISABLED, false));

        setCreativeTab(CreateLegacy.TAB_KINETICS);
        setHarvestLevel("pickaxe", 0);

        setHardness(2.0f);
        setResistance(3.0f);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return getStateFromMeta(meta).getValue(BRASS) ? new TileEntityFunnelAdvanced() : new TileEntityFunnel();
    }

    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class,
            EnumFacing.HORIZONTALS);
    public static final PropertyBool BRASS = PropertyBool.create("brass");
    public static final PropertyBool DISABLED = PropertyBool.create("disabled");

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, BRASS, DISABLED);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (facing.getAxis().isVertical()) return getStateForPlacement(world, pos, placer.getHorizontalFacing().getOpposite(), hitX, hitY, hitZ, meta, placer, hand);
        return getDefaultState().withProperty(FACING, facing).withProperty(BRASS, placer.getHeldItem(hand).getMetadata() == 1)
                .withProperty(DISABLED, false);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex() + (state.getValue(BRASS) ? 4 : 0);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta % 4))
                .withProperty(BRASS, meta / 4 == 1);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(BRASS) ? 1 : 0;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, state.getValue(BRASS) ? 1 : 0);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return stack.getMetadata() == 1 ? "tile.create.funnel_brass" : "tile.create.funnel_andesite";
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (state.getValue(BRASS)) {
            TileEntityFunnelAdvanced funnelAdvanced = (TileEntityFunnelAdvanced) worldIn.getTileEntity(pos);

            if (funnelAdvanced != null) {
                if (playerIn.isSneaking()) return false;
                else {
                    funnelAdvanced.setFilter(playerIn.getHeldItem(hand));
                    if (funnelAdvanced.getFilter().isEmpty()) playerIn.sendStatusMessage(
                            new TextComponentString("Cleared filter"), true);
                    else playerIn.sendStatusMessage(
                            new TextComponentString(String.format("Set filter to %s",
                                    funnelAdvanced.getFilter().getDisplayName())), true);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        checkRedstone(worldIn, pos, state);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        checkRedstone(worldIn, pos, state);
    }

    private void checkRedstone(World world, BlockPos pos, IBlockState state) {
        boolean powered = world.isBlockPowered(pos)  || world.isBlockIndirectlyGettingPowered(pos) > 0;
        System.out.println(pos + " has " + powered);
        TileEntity te = world.getTileEntity(pos);
        world.setBlockState(pos, state.withProperty(DISABLED, powered));
        if (te != null) {
            te.validate();
            world.setTileEntity(pos, te);
            if (te instanceof TileEntityFunnelAdvanced && !world.isRemote) {
                PacketUpdateFunnelAdvanced.sendToPlayersNearby((TileEntityFunnelAdvanced) te, 32);
            }
        }
    }

    protected static final AxisAlignedBB AABB_SOUTH = CreateLegacy.aabb(0, 0, 0, 16, 16, 8);
    protected static final AxisAlignedBB AABB_NORTH = CreateLegacy.aabb(0, 0, 8, 16, 16, 16);
    protected static final AxisAlignedBB AABB_WEST = CreateLegacy.aabb(8, 0, 0, 16, 16, 16);
    protected static final AxisAlignedBB AABB_EAST = CreateLegacy.aabb(0, 0, 0, 8, 16, 16);

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(FACING)) {
            case NORTH: return AABB_NORTH;
            case EAST: return AABB_EAST;
            case SOUTH: return AABB_SOUTH;
            case WEST: return AABB_WEST;
        }
        return FULL_BLOCK_AABB;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
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
}
