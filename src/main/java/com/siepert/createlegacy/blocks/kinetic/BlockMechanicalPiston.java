package com.siepert.createlegacy.blocks.kinetic;

import com.siepert.createapi.Spaghetti;
import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.blocks.item.ItemBlockVariants;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createapi.IKineticActor;
import com.siepert.createlegacy.util.IMetaName;
import com.siepert.createapi.IWrenchable;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
public class BlockMechanicalPiston extends Block implements IHasModel, IKineticActor, IWrenchable, IMetaName {
    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);
    public static final PropertyBool STICKY = PropertyBool.create("sticky");
    public static final PropertyBool EXTENDED = PropertyBool.create("extended");
    public BlockMechanicalPiston(String name) {
        super(Material.ROCK);
        this.translucent = true;
        this.blockSoundType = SoundType.WOOD;
        this.fullBlock = false;
        setLightOpacity(0);

        setUnlocalizedName("create:");
        setRegistryName(name);
        setCreativeTab(CreateLegacy.TAB_CREATE);
        setHarvestLevel("axe", 0);
        setHardness(1);
        setResistance(2);

        setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP)
                .withProperty(STICKY, false).withProperty(EXTENDED, false));

        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlockVariants(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int flag = 0;
        if (state.getValue(STICKY)) flag = 6;
        return state.getValue(FACING).getIndex() + flag;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean flag = meta / 6 == 1;
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta % 6)).withProperty(STICKY, flag);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {FACING, STICKY, EXTENDED});
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1));
    }

    @Override
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing,
                                            float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        boolean isSticky = placer.getHeldItem(hand).getItemDamage() == 1;

        if (placer.isSneaking()) {
            return getDefaultState().withProperty(FACING, facing.getOpposite()).withProperty(STICKY, isSticky);
        }
        return getDefaultState().withProperty(FACING,
                EnumFacing.getFacingFromVector((float) placer.getLookVec().x, (float) placer.getLookVec().y, (float) placer.getLookVec().z)
                        .getOpposite()).withProperty(STICKY, isSticky);
    }

    @Override
    public void registerModels() {
        CreateLegacy.proxy.registerVariantRenderer(Item.getItemFromBlock(this), 0, "piston/unsticky", "inventory");
        CreateLegacy.proxy.registerVariantRenderer(Item.getItemFromBlock(this), 1, "piston/sticky", "inventory");
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        IBlockState waveFrontState = worldIn.getBlockState(pos.offset(state.getValue(FACING)));
        if (waveFrontState.getBlock() == ModBlocks.PISTON_ERECTOR) {
            if (waveFrontState.getValue(BlockPistonErector.AXIS) == state.getValue(FACING).getAxis()) {
                return state.withProperty(EXTENDED, true);
            }
        }
        return state;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return !state.getValue(EXTENDED);
    }
    @Override
    public boolean isFullCube(IBlockState state) {
        return !state.getValue(EXTENDED);
    }
    @Override
    public boolean isTranslucent(IBlockState state) {
        return state.getValue(EXTENDED);
    }
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return state.getValue(EXTENDED);
    }

    @Override
    public void passRotation(World worldIn, BlockPos pos, EnumFacing source, List<BlockPos> iteratedBlocks,
                             boolean srcIsCog, boolean srcCogIsHorizontal, boolean inverseRotation) {

        if (srcIsCog) return; //We don't accept a cog as input, we need a shaft!

        IBlockState myState = worldIn.getBlockState(pos);

        if (source.getAxis() == myState.getValue(FACING).getAxis()) return;

        iteratedBlocks.add(pos);

        if (worldIn.getBlockState(pos.offset(source.getOpposite())).getBlock() instanceof IKineticActor) {
            ((IKineticActor) worldIn.getBlockState(pos.offset(source.getOpposite())).getBlock())
                    .passRotation(worldIn, pos.offset(source.getOpposite()), source, iteratedBlocks,
                            false, false, inverseRotation);
        }

        if (move(worldIn, pos, myState.getValue(FACING))) {
            worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                    SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS,
                    1.0f, 1.0f);
        }
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing side) {
        IBlockState state = world.getBlockState(pos);

        if (side.getAxis() == state.getValue(FACING).getAxis()) return false;

        world.setBlockState(pos, state.withProperty(FACING, state.getValue(FACING).rotateAround(side.getAxis())), 3);

        return true;
    }
    @Override
    public boolean onWrenched(World worldIn, BlockPos pos, IBlockState state, EnumFacing side, EntityPlayer playerIn) {
        return rotateBlock(worldIn, pos, side);
    }

    @Override
    public String getSpecialName(ItemStack stack) {
        if (stack.getItemDamage() == 1) return "piston_sticky";
        return "piston";
    }


    /**
     * @param world World.
     * @param pos Position of the piston.
     * @param direction Direction of the movement.
     * @return True if the piston moved; sound!
     */
    private boolean move(World world, BlockPos pos, EnumFacing direction) {
        BlockPos whereBlock;

        if (world.getBlockState(pos.offset(direction.getOpposite())).getBlock() != ModBlocks.PISTON_ERECTOR) {
            return false;
        }

        if (world.getBlockState(pos.offset(direction)).getBlock() != ModBlocks.PISTON_ERECTOR) {
            if (!(world.getBlockState(pos.offset(direction)).getBlock()
                    .getMaterial(world.getBlockState(pos.offset(direction))).blocksMovement())
                    && world.getBlockState(pos.offset(direction, 2)).getMaterial().isReplaceable()) {
                BlockPos.MutableBlockPos lastPole = (BlockPos.MutableBlockPos) pos.offset(direction.getOpposite());
                boolean flag = world.getBlockState(lastPole).getBlock() == ModBlocks.PISTON_ERECTOR;
                while (flag) {
                    if (world.getBlockState(lastPole).getValue(BlockPistonErector.AXIS) != direction.getAxis()) {
                        flag = false;
                        break;
                    }

                    lastPole.move(direction.getOpposite());

                    flag = world.getBlockState(lastPole).getBlock() == ModBlocks.PISTON_ERECTOR;
                }

                lastPole.move(direction);

                world.setBlockState(lastPole, Blocks.AIR.getDefaultState(), 3);

                IBlockState stateOfYes = world.getBlockState(pos.offset(direction));

                world.setBlockState(pos.offset(direction, 2), stateOfYes, 3);
                world.setBlockState(pos.offset(direction), ModBlocks.PISTON_ERECTOR.getDefaultState().withProperty(BlockPistonErector.AXIS, direction.getAxis()), 3);
                return true;
            }
        }
        return false;
    }
}
