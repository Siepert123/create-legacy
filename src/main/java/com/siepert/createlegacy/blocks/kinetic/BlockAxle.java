package com.siepert.createlegacy.blocks.kinetic;

import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createlegacy.util.IHasRotation;
import com.siepert.createapi.IKineticActor;
import com.siepert.createapi.IWrenchable;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

@SuppressWarnings("deprecation")
public class BlockAxle extends Block implements IHasModel, IHasRotation, IKineticActor, IWrenchable {
    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);

    private static final AxisAlignedBB HITBOX_X = new AxisAlignedBB(0.0, 6.0 / 16.0, 6.0 / 16.0, 1.0, 10.0 / 16.0, 10.0 / 16.0);
    private static final AxisAlignedBB HITBOX_Y = new AxisAlignedBB(6.0 / 16.0, 0.0, 6.0 / 16.0, 10.0 / 16.0, 1.0, 10.0 / 16.0);
    private static final AxisAlignedBB HITBOX_Z = new AxisAlignedBB(6.0 / 16.0, 6.0 / 16.0, 0.0, 10.0 / 16.0, 10.0 / 16.0, 1.0);

    public BlockAxle(String name) {
        super(Material.ROCK);
        this.translucent = true;
        this.blockSoundType = SoundType.STONE;
        this.fullBlock = false;
        setLightOpacity(0);

        setUnlocalizedName("create:" + name);
        setRegistryName(name);
        setCreativeTab(CreateLegacy.TAB_CREATE);
        setHarvestLevel("pickaxe", 0);
        setHardness(1);
        setResistance(2);
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));


    }

    @Override
    public void registerModels() {
        CreateLegacy.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        switch (state.getValue(AXIS)) {
            case X:
                meta += 3;
                break;
            case Z:
                meta += 6;
                break;
            case Y:
                break;
        }
        meta += state.getValue(ROTATION);
        return meta;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState toReturn = this.getDefaultState().withProperty(ROTATION, meta % 4);
        switch ((meta - meta % 4) / 4) {
            case 0:
                return toReturn.withProperty(AXIS, EnumFacing.Axis.Y);
            case 1:
                return toReturn.withProperty(AXIS, EnumFacing.Axis.X);
            case 2:
                return toReturn.withProperty(AXIS, EnumFacing.Axis.Z);
            default:
                return toReturn.withProperty(AXIS, EnumFacing.Axis.Y);
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {AXIS, ROTATION});
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        EnumFacing.Axis axis = facing.getAxis();
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(AXIS, axis);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(AXIS)) {
            case X:
                return HITBOX_X;
            case Y:
                return HITBOX_Y;
            case Z:
                return HITBOX_Z;
            default:
                return super.getBoundingBox(state, source, pos);
        }
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
    public void passRotation(World worldIn, BlockPos pos, EnumFacing source, List<BlockPos> iteratedBlocks,
                             boolean srcIsCog, boolean srcCogIsHorizontal, boolean inverseRotation) {

        IBlockState myState = worldIn.getBlockState(pos);

        if (source.getAxis() == myState.getValue(AXIS) && !srcIsCog) {
            iteratedBlocks.add(pos);

            IBlockState myNewState;
            if (!inverseRotation) {
                if (myState.getValue(ROTATION) == 3) {
                    myNewState = myState.withProperty(ROTATION, 0);
                } else {
                    myNewState = myState.withProperty(ROTATION, myState.getValue(ROTATION) + 1);
                }
            } else {
                if (myState.getValue(ROTATION) == 0) {
                    myNewState = myState.withProperty(ROTATION, 3);
                } else {
                    myNewState = myState.withProperty(ROTATION, myState.getValue(ROTATION) - 1);
                }
            }
            worldIn.markBlockRangeForRenderUpdate(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1,
                    pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);

            if (worldIn.getBlockState(pos.offset(source.getOpposite())).getBlock() instanceof IKineticActor) {
                ((IKineticActor) worldIn.getBlockState(pos.offset(source.getOpposite())).getBlock()).passRotation(worldIn,
                        pos.offset(source.getOpposite()), source, iteratedBlocks, false, false, inverseRotation);
            }

            worldIn.setBlockState(pos, myNewState, 0);
        }
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing side) {
        IBlockState state = world.getBlockState(pos);

        if (side.getAxis() == state.getValue(AXIS)) return false;

        EnumFacing f = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, state.getValue(AXIS)).rotateAround(side.getAxis());

        world.setBlockState(pos, state.withProperty(AXIS, f.getAxis()), 3);

        return true;
    }
    @Override
    public boolean onWrenched(World worldIn, BlockPos pos, IBlockState state, EnumFacing side, EntityPlayer playerIn) {
        return rotateBlock(worldIn, pos, side);
    }
}
