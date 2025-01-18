package com.melonstudios.createlegacy.block.kinetic;

import com.melonstudios.createlegacy.block.IWrenchable;
import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.item.ModItems;
import com.melonstudios.createlegacy.tileentity.TileEntityBeltLegacy;
import com.melonstudios.melonlib.misc.AABB;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
public class BlockBeltLegacy extends AbstractBlockKinetic {
    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create(
            "axis", EnumFacing.Axis.class, EnumFacing.Axis.X, EnumFacing.Axis.Z
    );
    public static final PropertyBool HAS_SHAFT = PropertyBool.create("has_shaft");

    public BlockBeltLegacy() {
        super("belt_legacy");

        setDefaultState(this.blockState.getBaseState()
                .withProperty(AXIS, EnumFacing.Axis.X)
                .withProperty(HAS_SHAFT, false));

        setUnlocalizedName("create.belt");
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS, HAS_SHAFT);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBeltLegacy();
    }

    private static final AxisAlignedBB aabb = AABB.create(0, 0, 0, 16, 14, 16);
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return aabb;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing,
                                            float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(AXIS, placer.getHorizontalFacing().getAxis());
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!state.getValue(HAS_SHAFT)) {
            ItemStack stack = playerIn.getHeldItem(hand);

            if (stack.getItem() == Item.getItemFromBlock(ModBlocks.ROTATOR) && stack.getMetadata() == 0) {
                TileEntity te = worldIn.getTileEntity(pos);
                worldIn.setBlockState(pos, state.withProperty(HAS_SHAFT, true));
                if (te != null) {
                    te.validate();
                    worldIn.setTileEntity(pos, te);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (state.getValue(HAS_SHAFT)) {
            return state.getValue(AXIS) == EnumFacing.Axis.X ? 2 : 3;
        } else return state.getValue(AXIS) == EnumFacing.Axis.X ? 0 : 1;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState()
                .withProperty(AXIS, meta % 2 == 0 ? EnumFacing.Axis.X : EnumFacing.Axis.Z)
                .withProperty(HAS_SHAFT, meta / 2 > 0);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean onWrenched(World world, BlockPos pos, IBlockState state, EnumFacing side, EntityPlayer wrenchHolder) {
        TileEntity te = world.getTileEntity(pos);
        if (state.getValue(HAS_SHAFT)) {
            world.setBlockState(pos, state.withProperty(HAS_SHAFT, false));
        } else {
            world.setBlockState(pos, state.withProperty(AXIS, state.getValue(AXIS) == EnumFacing.Axis.X ?
                    EnumFacing.Axis.Z : EnumFacing.Axis.X));
        }
        if (te != null) {
            te.validate();
            world.setTileEntity(pos, te);
        }
        return true;
    }
}
