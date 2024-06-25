package com.siepert.createlegacy.blocks.kinetic;

import com.siepert.createapi.IKineticActor;
import com.siepert.createapi.IWrenchable;
import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.*;
import com.siepert.createlegacy.util.handlers.ModSoundHandler;
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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class BlockCogwheel extends Block implements IHasModel, IHasRotation, IKineticActor, IWrenchable {
    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);

    public BlockCogwheel(String name) {
        super(Material.ROCK);
        this.translucent = true;
        this.blockSoundType = SoundType.WOOD;
        this.fullBlock = false;
        setLightOpacity(0);

        setUnlocalizedName("create:" + name);
        setRegistryName(name);
        setCreativeTab(CreateLegacy.TAB_CREATE);
        setHarvestLevel("axe", 0);
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
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing,
                                            float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        EnumFacing.Axis axis = facing.getAxis();
        if (world.getBlockState(pos.offset(facing.getOpposite())).getBlock() instanceof BlockCogwheel && !placer.isSneaking()) {
            return this.getDefaultState().withProperty(AXIS, world.getBlockState(pos.offset(facing.getOpposite())).getValue(AXIS));
        }
        return this.getDefaultState().withProperty(AXIS, axis);
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

        if (isCognectionValid(myState, source, srcIsCog, srcCogIsHorizontal)) {
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
            if (Reference.random.nextInt(100) == 0)
                worldIn.playSound(null, pos.getX() + 0.5,
                    pos.getY() + 0.5, pos.getZ() + 0.5,
                    ModSoundHandler.BLOCK_COGWHEEL_AMBIENT_2, SoundCategory.BLOCKS,
                    0.1f, 1.0f);
            for (EnumFacing facing : EnumFacing.values()) {
                if (facing != source && !iteratedBlocks.contains(pos.offset(facing))) {
                    Block blockNow = worldIn.getBlockState(pos.offset(facing)).getBlock();
                    boolean srcCog = facing.getAxis() != myState.getValue(AXIS);
                    boolean srcCogH = myState.getValue(AXIS) == EnumFacing.Axis.Y;
                    if (blockNow instanceof IKineticActor) {
                        if (!srcCog) {
                            ((IKineticActor) blockNow).passRotation(worldIn, pos.offset(facing), facing.getOpposite(), iteratedBlocks,
                                    false, false, inverseRotation);
                        } else {
                            ((IKineticActor) blockNow).passRotation(worldIn, pos.offset(facing), facing.getOpposite(), iteratedBlocks,
                                    true, srcCogH, !inverseRotation);
                        }
                    }
                }
            }
            worldIn.setBlockState(pos, myNewState, 0);
        }
    }

    /**Checks whether the connection to this cogwheel is valid.
     * <p>
     * Get it? <tt>cognection</tt>? Hehe
     * @param myState The blockstate of the cogwheel.
     * @param source The direction of the signal.
     * @param srcIsCog Whether the source is a cogwheel.
     * @param srcCogHorizontal Whether the source cogwheel is horizontal.
     * @return True if the connection is  validated.*/
    private boolean isCognectionValid(IBlockState myState, EnumFacing source, boolean srcIsCog, boolean srcCogHorizontal) {
        boolean amIHorizontal = myState.getValue(AXIS) == EnumFacing.Axis.Y;
        EnumFacing.Axis whatsMyAxis = myState.getValue(AXIS);
        List<EnumFacing> thingies = new ArrayList<EnumFacing>();
        thingies.add(EnumFacing.NORTH);
        thingies.add(EnumFacing.EAST);
        thingies.add(EnumFacing.SOUTH);
        thingies.add(EnumFacing.WEST);

        if (source.getAxis() == whatsMyAxis && !srcIsCog) {
            if (EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, whatsMyAxis) == source
                || EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.NEGATIVE, whatsMyAxis) == source) {
                return true;
            }
        }
        if (amIHorizontal && srcCogHorizontal && thingies.contains(source)) {
            return true;
        }
        if (!amIHorizontal && !srcCogHorizontal && srcIsCog) {
            List<EnumFacing> thingyA = new ArrayList<>();
            List<EnumFacing> thingyB = new ArrayList<>();

            thingyA.add(EnumFacing.NORTH);
            thingyA.add(EnumFacing.DOWN);
            thingyA.add(EnumFacing.UP);
            thingyA.add(EnumFacing.SOUTH);

            thingyB.add(EnumFacing.WEST);
            thingyB.add(EnumFacing.DOWN);
            thingyB.add(EnumFacing.UP);
            thingyB.add(EnumFacing.EAST);
            switch (whatsMyAxis) {
                case X:
                    if (thingyA.contains(source)) return true;
                    break;
                case Z:
                    if (thingyB.contains(source)) return true;
                    break;
                default:
                    return false;
            }
        }

        return false;
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
