package com.siepert.createlegacy.blocks.kinetic;

import com.siepert.createlegacy.blocks.KineticBlock;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.tileentity.TileEntityBelt;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockBelt extends KineticBlock {
    public static final PropertyBool HAS_AXLE = PropertyBool.create("has_axle");
    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);
    public BlockBelt(String name) {
        super(name, Material.CLOTH, MapColor.BLACK);
        this.translucent = true;
        this.blockSoundType = SoundType.CLOTH;
        this.fullBlock = false;
        setLightOpacity(0);

        setDefaultState(this.blockState.getBaseState().withProperty(HAS_AXLE, false).withProperty(AXIS, EnumFacing.Axis.X));
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        if (state.getValue(HAS_AXLE)) meta += 2;
        if (state.getValue(AXIS) == EnumFacing.Axis.Z) meta++;
        return meta;
    }
    public static final AxisAlignedBB bb = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 14.0 / 16.0, 1.0);
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return bb;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        switch (meta) {
            case 0:
                return this.getDefaultState().withProperty(AXIS, EnumFacing.Axis.X).withProperty(HAS_AXLE, false);
            case 1:
                return this.getDefaultState().withProperty(AXIS, EnumFacing.Axis.Z).withProperty(HAS_AXLE, false);
            case 2:
                return this.getDefaultState().withProperty(AXIS, EnumFacing.Axis.X).withProperty(HAS_AXLE, true);
            case 3:
                return this.getDefaultState().withProperty(AXIS, EnumFacing.Axis.Z).withProperty(HAS_AXLE, true);
        }
        return this.getStateFromMeta(0);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {HAS_AXLE, AXIS});
    }

    @Override
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(AXIS, placer.getHorizontalFacing().getAxis());
    }


    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (playerIn.getHeldItem(hand).getItem() == Item.getItemFromBlock(ModBlocks.AXLE) && !state.getValue(HAS_AXLE)) {
            setState(worldIn, pos, state.withProperty(HAS_AXLE, true));
            return true;
        }
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isTranslucent(IBlockState state) {
        return true;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        IBlockState state = world.getBlockState(pos);

        if (axis.getAxis() != EnumFacing.Axis.Y) return false;

        if (state.getValue(AXIS) == EnumFacing.Axis.X) {
            setState(world, pos, state.withProperty(AXIS, EnumFacing.Axis.Z));
        } else {
            setState(world, pos, state.withProperty(AXIS, EnumFacing.Axis.X));
        }

        return true;
    }
    @Override
    public boolean onWrenched(World worldIn, BlockPos pos, IBlockState state, EnumFacing side, EntityPlayer playerIn) {
        rotateBlock(worldIn, pos, side);

        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBelt();
    }

    private static void setState(World world, BlockPos pos, IBlockState state) {
        TileEntity entity = world.getTileEntity(pos);

        world.setBlockState(pos, state, 3);

        if (entity != null) {
            entity.validate();
            world.setTileEntity(pos, entity);
        }
    }
}
