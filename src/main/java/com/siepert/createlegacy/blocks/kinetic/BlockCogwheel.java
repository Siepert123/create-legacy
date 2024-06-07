package com.siepert.createlegacy.blocks.kinetic;

import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createlegacy.util.IHasRotation;
import com.siepert.createlegacy.util.IKineticActor;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class BlockCogwheel extends Block implements IHasModel, IHasRotation, IKineticActor {
    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);

    public BlockCogwheel(String name) {
        super(Material.WOOD);
        this.translucent = true;
        this.blockSoundType = SoundType.WOOD;
        this.fullBlock = false;
        setLightOpacity(0);

        setUnlocalizedName(name);
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
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        EnumFacing.Axis axis = facing.getAxis();
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(AXIS, axis);
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
    public void rotate(World worldIn, BlockPos pos, EnumFacing source) {
        for (EnumFacing facing : EnumFacing.values()) {
            if (facing != source) {
                Block blockNow = worldIn.getBlockState(pos.offset(facing)).getBlock();
                if (blockNow instanceof IKineticActor) {
                    if (blockNow instanceof BlockCogwheel) {
                        try {
                            if (worldIn.getBlockState(pos).getValue(AXIS).equals(worldIn.getBlockState(pos.offset(facing)).getValue(AXIS))) {
                                ((IKineticActor) blockNow).act(worldIn, pos.offset(facing), facing.getOpposite());
                            }
                        } catch (Exception e) {
                            return;
                        }
                    }
                    else {
                        ((IKineticActor) blockNow).act(worldIn, pos.offset(facing), facing.getOpposite());
                    }
                }
            }
        }
    }

    @Override
    public void act(World worldIn, BlockPos pos, EnumFacing source) {
        try {
            IBlockState state = worldIn.getBlockState(pos);
            if (state.getValue(ROTATION) < 3)
                worldIn.setBlockState(pos, state.withProperty(ROTATION, state.getValue(ROTATION) + 1), 0);
            else worldIn.setBlockState(pos, state.withProperty(ROTATION, 0), 1);
            worldIn.markBlockRangeForRenderUpdate(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1,
                    pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
            rotate(worldIn, pos, source);
        } catch (StackOverflowError overflowError) {
            return;
        }
    }

    @Override //TODO: add the code
    public void passRotation(World worldIn, BlockPos pos, EnumFacing source, List<BlockPos> iteratedBlocks, boolean srcIsCog, boolean srcCogIsHorizontal) {
        iteratedBlocks.add(pos);
        IBlockState myState = worldIn.getBlockState(pos);

        if (isCognectionValid(myState, source, srcIsCog, srcCogIsHorizontal)) {
            IBlockState myNewState;
            if (myState.getValue(ROTATION) == 3) {
                myNewState = myState.withProperty(ROTATION, 0);
            } else {
                myNewState = myState.withProperty(ROTATION, myState.getValue(ROTATION) + 1);
            }
            worldIn.markBlockRangeForRenderUpdate(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1,
                    pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
            for (EnumFacing facing : EnumFacing.values()) {
                if (facing != source && !iteratedBlocks.contains(pos.offset(facing))) {
                    Block blockNow = worldIn.getBlockState(pos.offset(facing)).getBlock();
                    boolean srcCog = facing.getAxis() != myState.getValue(AXIS);
                    boolean srcCogH = myState.getValue(AXIS) == EnumFacing.Axis.Y;
                    if (blockNow instanceof IKineticActor) {
                        ((IKineticActor) blockNow).passRotation(worldIn, pos.offset(facing), facing.getOpposite(), iteratedBlocks, srcCog, srcCogH);
                    }
                }
            }
            worldIn.setBlockState(pos, myNewState, 0);
        }
    }

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
}
