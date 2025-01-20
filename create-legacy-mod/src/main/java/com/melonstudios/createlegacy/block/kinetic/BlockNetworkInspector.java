package com.melonstudios.createlegacy.block.kinetic;

import com.melonstudios.createlegacy.block.IGoggleInfo;
import com.melonstudios.createlegacy.tileentity.TileEntitySpeedometer;
import com.melonstudios.createlegacy.tileentity.TileEntityStressometer;
import com.melonstudios.createlegacy.util.IMetaName;
import com.melonstudios.melonlib.misc.AABB;
import mcp.MethodsReturnNonnullByDefault;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
public class BlockNetworkInspector extends AbstractBlockKinetic implements IMetaName, IGoggleInfo {
    public BlockNetworkInspector() {
        super("network_inspector");
    }

    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class,
            EnumFacing.Axis.X, EnumFacing.Axis.Z);
    public static final PropertyBool ALT = PropertyBool.create("alt");

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return meta / 2 == 0 ? new TileEntitySpeedometer() : new TileEntityStressometer();
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(AXIS) == EnumFacing.Axis.X ? 0 : 1) + (state.getValue(ALT) ? 2: 0);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ALT, AXIS);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(AXIS, meta % 2 == 0 ? EnumFacing.Axis.X : EnumFacing.Axis.Z)
                .withProperty(ALT, meta / 2 != 0);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return stack.getMetadata() == 0 ? "tile.create.speedometer" : "tile.create.stressometer";
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(AXIS, placer.getHorizontalFacing().rotateY().getAxis()).withProperty(ALT, meta != 0);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(ALT) ? 1 : 0;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, state.getValue(ALT) ? 1 : 0);
    }

    private static final AxisAlignedBB aabb = AABB.create(1, 0, 1, 15, 13, 15);
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return aabb;
    }

    @Override
    public NonNullList<String> getGoggleInformation(World world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntitySpeedometer) {
            return NonNullList.from("", ((TileEntitySpeedometer)te).queryData());
        }
        if (te instanceof TileEntityStressometer) {
            return NonNullList.from("", ((TileEntityStressometer)te).queryData());
        }
        return IGoggleInfo.EMPTY;
    }
}
