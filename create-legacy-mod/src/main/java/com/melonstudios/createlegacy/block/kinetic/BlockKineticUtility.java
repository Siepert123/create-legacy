package com.melonstudios.createlegacy.block.kinetic;

import com.melonstudios.createlegacy.block.IWrenchable;
import com.melonstudios.createlegacy.tileentity.TileEntityClutch;
import com.melonstudios.createlegacy.tileentity.TileEntityGearshift;
import com.melonstudios.createlegacy.util.IMetaName;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockKineticUtility extends AbstractBlockKinetic implements IMetaName, IWrenchable {
    public BlockKineticUtility() {
        super("kinetic_utility");

        setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.Y)
                .withProperty(SHIFT, false).withProperty(ACTIVE, false));
    }

    public static final PropertyEnum<EnumFacing.Axis> AXIS = BlockRotator.AXIS;
    public static final PropertyBool SHIFT = PropertyBool.create("shift");
    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS, SHIFT, ACTIVE);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        boolean shift = placer.getHeldItem(hand).getMetadata() != 0;
        if (placer.isSneaking()) {
            return getDefaultState().withProperty(AXIS, facing.getAxis()).withProperty(SHIFT, shift);
        }
        return getDefaultState().withProperty(AXIS, EnumFacing.getDirectionFromEntityLiving(pos, placer).getAxis()).withProperty(SHIFT, shift);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(AXIS).ordinal() + (state.getValue(SHIFT) ? 3 : 0);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(AXIS, EnumFacing.Axis.values()[meta % 3]).withProperty(SHIFT, meta / 3 != 0);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(SHIFT) ? 1: 0;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return state.getValue(SHIFT) ? new ItemStack(this, 1, 1) : new ItemStack(this, 1, 0);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1));
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
        boolean powered = world.isBlockPowered(pos) || world.isBlockIndirectlyGettingPowered(pos) > 0;
        world.setBlockState(pos, state.withProperty(ACTIVE, powered));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return getStateFromMeta(meta).getValue(SHIFT) ? new TileEntityGearshift() : new TileEntityClutch();
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "tile.create." + (stack.getMetadata() == 0 ? "clutch" : "gearshift");
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
        return true;
    }

    @Override
    public boolean shouldCheckWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean onWrenched(World world, BlockPos pos, IBlockState state, EnumFacing side, EntityPlayer wrenchHolder) {
        EnumFacing.Axis axis = state.getValue(AXIS);
        if (axis.apply(side)) return false;
        EnumFacing.Axis axisNew = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, axis).rotateAround(side.getAxis()).getAxis();
        world.setBlockState(pos, state.withProperty(AXIS, axisNew));
        return true;
    }
}
