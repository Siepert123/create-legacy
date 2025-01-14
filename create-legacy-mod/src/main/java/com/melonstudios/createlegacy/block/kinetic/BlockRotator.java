package com.melonstudios.createlegacy.block.kinetic;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.block.IWrenchable;
import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.tileentity.TileEntityCog;
import com.melonstudios.createlegacy.tileentity.TileEntityShaft;
import com.melonstudios.createlegacy.util.IMetaName;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
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
public class BlockRotator extends AbstractBlockKinetic implements IMetaName, IWrenchable {
    public BlockRotator() {
        super("rotator");
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1));
    }

    @Override
    public boolean onWrenched(World world, BlockPos pos, IBlockState state, EnumFacing side, EntityPlayer wrenchHolder) {
        EnumFacing.Axis axis = state.getValue(AXIS);
        if (axis.apply(side)) return false;
        EnumFacing.Axis axisNew = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, axis).rotateAround(side.getAxis()).getAxis();
        world.setBlockState(pos, state.withProperty(AXIS, axisNew));
        return true;
    }

    public enum Variant implements IStringSerializable {
        SHAFT, COG;

        public int getID() {
            return ordinal();
        }
        public static Variant fromID(int id) {
            return values()[id % values().length];
        }

        @Override
        public String getName() {
            return this == SHAFT ? "shaft" : "cog";
        }
    }
    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);
    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", Variant.class);

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS, VARIANT);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        int met = placer.getHeldItem(hand).getMetadata();

        if (world.getBlockState(pos.offset(facing.getOpposite())).getBlock() == ModBlocks.ROTATOR && !placer.isSneaking()) {
            return world.getBlockState(pos.offset(facing.getOpposite())).withProperty(VARIANT, Variant.fromID(met));
        } else {
            return getDefaultState().withProperty(VARIANT, Variant.fromID(met)).withProperty(AXIS, facing.getAxis());
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).getID();
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, state.getValue(VARIANT).getID());
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).getID() * 3 + state.getValue(AXIS).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VARIANT, Variant.fromID(meta/3)).withProperty(AXIS, EnumFacing.Axis.values()[meta%3]);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nullable World worldIn, int meta) {
        return getStateFromMeta(meta).getValue(VARIANT) == Variant.SHAFT ? new TileEntityShaft() : new TileEntityCog();
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return stack.getMetadata() == 0 ? "tile.create.shaft" : "tile.create.cog";
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.getValue(VARIANT).getID() == 0 ? MapColor.STONE : MapColor.WOOD;
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        return state.getValue(VARIANT) == Variant.SHAFT ? SoundType.STONE : SoundType.WOOD;
    }

    protected static final AxisAlignedBB SHAFT_X = CreateLegacy.aabb(0, 5, 5, 16, 11, 11);
    protected static final AxisAlignedBB SHAFT_Y = CreateLegacy.aabb(5, 0, 5, 11, 16, 11);
    protected static final AxisAlignedBB SHAFT_Z = CreateLegacy.aabb(5, 5, 0, 11, 11, 16);

    protected static final AxisAlignedBB COG_X = CreateLegacy.aabb(0, 2, 2, 16, 14, 14);
    protected static final AxisAlignedBB COG_Y = CreateLegacy.aabb(2, 0, 2, 14, 16, 14);
    protected static final AxisAlignedBB COG_Z = CreateLegacy.aabb(2, 2, 0, 14, 14, 16);

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (state.getValue(VARIANT) == Variant.SHAFT) {
            switch (state.getValue(AXIS)) {
                case X: return SHAFT_X;
                case Y: return SHAFT_Y;
                case Z: return SHAFT_Z;
            }
        }
        if (state.getValue(VARIANT) == Variant.COG) {
            switch (state.getValue(AXIS)) {
                case X: return COG_X;
                case Y: return COG_Y;
                case Z: return COG_Z;
            }
        }
        return Block.FULL_BLOCK_AABB;
    }
}
