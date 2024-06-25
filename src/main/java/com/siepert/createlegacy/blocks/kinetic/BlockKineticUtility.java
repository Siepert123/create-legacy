package com.siepert.createlegacy.blocks.kinetic;

import com.siepert.createapi.IKineticActor;
import com.siepert.createapi.IWrenchable;
import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.blocks.item.ItemBlockVariants;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.*;
import com.siepert.createlegacy.util.handlers.EnumHandler;
import com.siepert.createlegacy.util.handlers.ModSoundHandler;
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
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
public class BlockKineticUtility extends Block implements IHasModel, IMetaName, IKineticActor, IWrenchable {
    public static final PropertyEnum<EnumHandler.KineticUtilityEnumType> VARIANT
            = PropertyEnum.<EnumHandler.KineticUtilityEnumType>create("variant", EnumHandler.KineticUtilityEnumType.class);
    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);
    public static final PropertyBool _BOOLEAN0 = PropertyBool.create("_boolean0");

    public BlockKineticUtility() {
        super(Material.ROCK);
        setUnlocalizedName("create:");
        setRegistryName("kinetic_utility");
        setCreativeTab(CreateLegacy.TAB_CREATE);
        setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.KineticUtilityEnumType.GEARBOX)
                .withProperty(AXIS, EnumFacing.Axis.Y).withProperty(_BOOLEAN0, false));
        setHarvestLevel("axe", 0,
                this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.KineticUtilityEnumType.GEARBOX));
        setHarvestLevel("axe", 0,
                this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.KineticUtilityEnumType.CLUTCH));
        setHarvestLevel("axe", 0,
                this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.KineticUtilityEnumType.GEARSHIFT));
        setHarvestLevel("axe", 0,
                this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.KineticUtilityEnumType.AXLE_ENCASED_ANDESITE));
        setHarvestLevel("axe", 0,
                this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.KineticUtilityEnumType.AXLE_ENCASED_BRASS));
        setHardness(2);
        setResistance(5);

        setSoundType(SoundType.WOOD);

        ModBlocks.BLOCKS.add(this);
        assert this.getRegistryName() != null;
        ModItems.ITEMS.add(new ItemBlockVariants(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return ((EnumHandler.KineticUtilityEnumType) state.getValue(VARIANT)).getMeta();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int appendix;
        switch (state.getValue(AXIS)) {
            case X:
                appendix = 1;
                break;
            case Z:
                appendix = 2;
                break;
            default:
                appendix = 0;
                break;
        }
        return ((EnumHandler.KineticUtilityEnumType) state.getValue(VARIANT)).getMeta() * 3 + appendix;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState j = this.getDefaultState().withProperty(VARIANT, EnumHandler.KineticUtilityEnumType.byMetaData(meta / 3));
        switch (meta % 3) {
            case 0:
                return j.withProperty(AXIS, EnumFacing.Axis.Y);
            case 1:
                return j.withProperty(AXIS, EnumFacing.Axis.X);
            case 2:
                return j.withProperty(AXIS, EnumFacing.Axis.Z);
        }
        return j;
    }

    @Override
    @ParametersAreNonnullByDefault
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        switch (state.getValue(VARIANT)) {
            case GEARBOX:
                return new ItemStack(Item.getItemFromBlock(this), 1, 0);
            case CLUTCH:
                return new ItemStack(Item.getItemFromBlock(this), 1, 1);
            case GEARSHIFT:
                return new ItemStack(Item.getItemFromBlock(this), 1, 2);
            case AXLE_ENCASED_ANDESITE:
                return new ItemStack(Item.getItemFromBlock(this), 1, 3);
            case AXLE_ENCASED_BRASS:
                return new ItemStack(Item.getItemFromBlock(this), 1, 4);
            default:
                return new ItemStack(Items.AIR);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (EnumHandler.KineticUtilityEnumType variant : EnumHandler.KineticUtilityEnumType.values()) {
            items.add(new ItemStack(this, 1, variant.getMeta()));
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {VARIANT, AXIS, _BOOLEAN0});
    }

    @Override
    public String getSpecialName(ItemStack stack) {
        return EnumHandler.KineticUtilityEnumType.values()[stack.getItemDamage()].getName();
    }

    @Override
    public void registerModels() {
        CreateLegacy.proxy.registerVariantRenderer(Item.getItemFromBlock(this),
                0, "kinetic_utility/gearbox", "inventory");
        CreateLegacy.proxy.registerVariantRenderer(Item.getItemFromBlock(this),
                1, "kinetic_utility/clutch", "inventory");
        CreateLegacy.proxy.registerVariantRenderer(Item.getItemFromBlock(this),
                2, "kinetic_utility/gearshift", "inventory");
        CreateLegacy.proxy.registerVariantRenderer(Item.getItemFromBlock(this),
                3, "kinetic_utility/axle_encased_andesite", "inventory");
        CreateLegacy.proxy.registerVariantRenderer(Item.getItemFromBlock(this),
                4, "kinetic_utility/axle_encased_brass", "inventory");
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        IBlockState variantState = this.getDefaultState()
                .withProperty(VARIANT, EnumHandler.KineticUtilityEnumType.values()[placer.getHeldItem(hand).getItemDamage()]);
        if (variantState.getValue(VARIANT) == EnumHandler.KineticUtilityEnumType.GEARBOX) {
            if (placer.isSneaking()) {
                return variantState.withProperty(AXIS, facing.getAxis());
            }
            return variantState;
        }
        if (placer.isSneaking()) {
            return variantState.withProperty(AXIS, facing.getAxis());
        }
        return variantState.withProperty(AXIS, EnumFacing.getFacingFromVector(
                (float) placer.getLookVec().x, (float) placer.getLookVec().y, (float) placer.getLookVec().z)
                .getAxis());
    }


    @Override
    public void passRotation(World worldIn, BlockPos pos, EnumFacing source, List<BlockPos> iteratedBlocks,
                             boolean srcIsCog, boolean srcCogIsHorizontal, boolean inverseRotation) {

        if (srcIsCog) return;

        EnumHandler.KineticUtilityEnumType blockLogicController = worldIn.getBlockState(pos).getValue(VARIANT);

        switch (blockLogicController) {
            case GEARBOX:
                actGearbox(worldIn, pos, source, iteratedBlocks, inverseRotation);
                break;
            case CLUTCH:
                actClutch(worldIn, pos, source, iteratedBlocks, inverseRotation);
                break;
            case GEARSHIFT:
                actGearshift(worldIn, pos, source, iteratedBlocks, inverseRotation);
                break;
            default:
                actEncasedShaft(worldIn, pos, source, iteratedBlocks, inverseRotation);
                break;
        }
    }

    private void actEncasedShaft(World worldIn, BlockPos pos, EnumFacing source, List<BlockPos> iteratedBlocks, boolean inverseRotation) {
        if (worldIn.getBlockState(pos).getValue(AXIS) == EnumFacing.Axis.Y) {
            if (source.getAxis() == EnumFacing.Axis.Y) {
                switch (source) {
                    case UP:
                        if (worldIn.getBlockState(pos.down()).getBlock() instanceof IKineticActor) {
                            ((IKineticActor) worldIn.getBlockState(pos.down()).getBlock())
                                    .passRotation(worldIn, pos.down(), EnumFacing.UP, iteratedBlocks,
                                            false, false, inverseRotation);
                        }
                        break;
                    case DOWN:
                        if (worldIn.getBlockState(pos.up()).getBlock() instanceof IKineticActor) {
                            ((IKineticActor) worldIn.getBlockState(pos.up()).getBlock())
                                    .passRotation(worldIn, pos.up(), EnumFacing.DOWN, iteratedBlocks,
                                            false, false, inverseRotation);
                        }
                        break;
                }
                iteratedBlocks.add(pos);
            }
            return;
        }

        if (worldIn.getBlockState(pos).getValue(AXIS) == EnumFacing.Axis.X) {
            if (source.getAxis() == EnumFacing.Axis.X) {
                switch (source) {
                    case WEST:
                        if (worldIn.getBlockState(pos.east()).getBlock() instanceof IKineticActor) {
                            ((IKineticActor) worldIn.getBlockState(pos.east()).getBlock())
                                    .passRotation(worldIn, pos.east(), EnumFacing.WEST, iteratedBlocks,
                                            false, false, inverseRotation);
                        }
                        break;
                    case EAST:
                        if (worldIn.getBlockState(pos.west()).getBlock() instanceof IKineticActor) {
                            ((IKineticActor) worldIn.getBlockState(pos.west()).getBlock())
                                    .passRotation(worldIn, pos.west(), EnumFacing.EAST, iteratedBlocks,
                                            false, false, inverseRotation);
                        }
                        break;
                }
            }
            return;
        }
        if (worldIn.getBlockState(pos).getValue(AXIS) == EnumFacing.Axis.Z) {
            if (source.getAxis() == EnumFacing.Axis.Z) {
                switch (source) {
                    case NORTH:
                        if (worldIn.getBlockState(pos.south()).getBlock() instanceof IKineticActor) {
                            ((IKineticActor) worldIn.getBlockState(pos.south()).getBlock())
                                    .passRotation(worldIn, pos.south(), EnumFacing.NORTH, iteratedBlocks,
                                            false, false, inverseRotation);
                        }
                        break;
                    case SOUTH:
                        if (worldIn.getBlockState(pos.north()).getBlock() instanceof IKineticActor) {
                            ((IKineticActor) worldIn.getBlockState(pos.north()).getBlock())
                                    .passRotation(worldIn, pos.north(), EnumFacing.SOUTH, iteratedBlocks,
                                            false, false, inverseRotation);
                        }
                        break;
                }
            }
        }
    }

    private void actGearbox(World worldIn, BlockPos pos, EnumFacing source, List<BlockPos> iteratedBlocks, boolean inverseRotation) {
        if (worldIn.getBlockState(pos).getValue(AXIS) == EnumFacing.Axis.Y) {
            if (source.getAxis() != EnumFacing.Axis.Y) {
                ArrayList<EnumFacing> AXLES_TO_ACT = new ArrayList<>();
                AXLES_TO_ACT.add(EnumFacing.NORTH);
                AXLES_TO_ACT.add(EnumFacing.EAST);
                AXLES_TO_ACT.add(EnumFacing.SOUTH);
                AXLES_TO_ACT.add(EnumFacing.WEST);
                AXLES_TO_ACT.remove(source);

                IBlockState sourceBlock = worldIn.getBlockState(pos.offset(source));
                if (sourceBlock.getBlock() instanceof BlockCogwheel) {
                    if (sourceBlock.getValue(BlockCogwheel.AXIS) != source.getAxis()) return;
                }

                if (Reference.random.nextInt(25) == 0)
                    worldIn.playSound(null, pos.getX() + 0.5,
                            pos.getY() + 0.5, pos.getZ() + 0.5,
                            ModSoundHandler.BLOCK_COGWHEEL_AMBIENT_2, SoundCategory.BLOCKS,
                            0.1f, 1.0f);

                for (EnumFacing output : AXLES_TO_ACT) {
                    IBlockState theBlock = worldIn.getBlockState(pos.offset(output));
                    if (theBlock.getBlock() instanceof IKineticActor) {
                        boolean shouldInv = output.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE;
                        if (shouldInv) {
                            ((IKineticActor) theBlock.getBlock()).passRotation(worldIn, pos.offset(output), output.getOpposite(), iteratedBlocks,
                                    false, false, inverseRotation);
                        } else {
                            ((IKineticActor) theBlock.getBlock()).passRotation(worldIn, pos.offset(output), output.getOpposite(), iteratedBlocks,
                                    false, false, !inverseRotation);
                        }
                    }
                }
                iteratedBlocks.add(pos);
            }
            return;
        }

        if (worldIn.getBlockState(pos).getValue(AXIS) == EnumFacing.Axis.X) {
            if (source.getAxis() != EnumFacing.Axis.X) {
                ArrayList<EnumFacing> AXLES_TO_ACT = new ArrayList<>();
                AXLES_TO_ACT.add(EnumFacing.NORTH);
                AXLES_TO_ACT.add(EnumFacing.UP);
                AXLES_TO_ACT.add(EnumFacing.SOUTH);
                AXLES_TO_ACT.add(EnumFacing.DOWN);
                AXLES_TO_ACT.remove(source);

                IBlockState sourceBlock = worldIn.getBlockState(pos.offset(source));
                if (sourceBlock.getBlock() instanceof BlockCogwheel) {
                    if (sourceBlock.getValue(BlockCogwheel.AXIS) != source.getAxis()) return;
                }

                if (Reference.random.nextInt(25) == 0)
                    worldIn.playSound(null, pos.getX() + 0.5,
                            pos.getY() + 0.5, pos.getZ() + 0.5,
                            ModSoundHandler.BLOCK_COGWHEEL_AMBIENT_2, SoundCategory.BLOCKS,
                            0.1f, 1.0f);


                for (EnumFacing output : AXLES_TO_ACT) {
                    IBlockState theBlock = worldIn.getBlockState(pos.offset(output));
                    if (theBlock.getBlock() instanceof IKineticActor) {
                        ((IKineticActor) theBlock.getBlock()).passRotation(worldIn, pos.offset(output), output.getOpposite(), iteratedBlocks,
                                false, false, inverseRotation);
                    }
                }
                iteratedBlocks.add(pos);
            }
            return;
        }

        if (worldIn.getBlockState(pos).getValue(AXIS) == EnumFacing.Axis.Z) {
            if (source.getAxis() != EnumFacing.Axis.Z) {
                ArrayList<EnumFacing> AXLES_TO_ACT = new ArrayList<>();
                AXLES_TO_ACT.add(EnumFacing.UP);
                AXLES_TO_ACT.add(EnumFacing.EAST);
                AXLES_TO_ACT.add(EnumFacing.DOWN);
                AXLES_TO_ACT.add(EnumFacing.WEST);
                AXLES_TO_ACT.remove(source);

                IBlockState sourceBlock = worldIn.getBlockState(pos.offset(source));
                if (sourceBlock.getBlock() instanceof BlockCogwheel) {
                    if (sourceBlock.getValue(BlockCogwheel.AXIS) != source.getAxis()) return;
                }

                if (Reference.random.nextInt(25) == 0)
                    worldIn.playSound(null, pos.getX() + 0.5,
                            pos.getY() + 0.5, pos.getZ() + 0.5,
                            ModSoundHandler.BLOCK_COGWHEEL_AMBIENT_2, SoundCategory.BLOCKS,
                            0.1f, 1.0f);


                for (EnumFacing output : AXLES_TO_ACT) {
                    IBlockState theBlock = worldIn.getBlockState(pos.offset(output));
                    if (theBlock.getBlock() instanceof IKineticActor) {
                        ((IKineticActor) theBlock.getBlock()).passRotation(worldIn, pos.offset(output), output.getOpposite(), iteratedBlocks,
                                false, false, inverseRotation);
                    }
                }
                iteratedBlocks.add(pos);
            }
            return;
        }

    }

    private void actClutch(World worldIn, BlockPos pos, EnumFacing source, List<BlockPos> iteratedBlocks, boolean inverseRotation) {
        if (worldIn.getBlockState(pos).getValue(AXIS) == EnumFacing.Axis.Y) {
            if (source.getAxis() == EnumFacing.Axis.Y) {
                for (EnumFacing enumFacing : Arrays.asList(EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST)) {
                    if (worldIn.getRedstonePower(pos, enumFacing) > 0) {
                        worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(_BOOLEAN0, true), 0);
                        worldIn.markBlockRangeForRenderUpdate(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1,
                                pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
                        return;
                    } else {
                        worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(_BOOLEAN0, false), 0);
                        worldIn.markBlockRangeForRenderUpdate(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1,
                                pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
                    }
                }

                switch (source) {
                    case UP:
                        if (worldIn.getBlockState(pos.down()).getBlock() instanceof IKineticActor) {
                            ((IKineticActor) worldIn.getBlockState(pos.down()).getBlock())
                                    .passRotation(worldIn, pos.down(), EnumFacing.UP, iteratedBlocks,
                                            false, false, inverseRotation);
                        }
                        break;
                    case DOWN:
                        if (worldIn.getBlockState(pos.up()).getBlock() instanceof IKineticActor) {
                            ((IKineticActor) worldIn.getBlockState(pos.up()).getBlock())
                                    .passRotation(worldIn, pos.up(), EnumFacing.DOWN, iteratedBlocks,
                                            false, false, inverseRotation);
                        }
                        break;
                }
                iteratedBlocks.add(pos);
            }
            return;
        }

        if (worldIn.getBlockState(pos).getValue(AXIS) == EnumFacing.Axis.X) {
            for (EnumFacing enumFacing : Arrays.asList(EnumFacing.NORTH, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.UP)) {
                if (worldIn.getRedstonePower(pos, enumFacing) > 0) {
                    worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(_BOOLEAN0, true), 0);
                    worldIn.markBlockRangeForRenderUpdate(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1,
                            pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
                    return;
                } else {
                    worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(_BOOLEAN0, false), 0);
                    worldIn.markBlockRangeForRenderUpdate(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1,
                            pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
                }
            }
            if (source.getAxis() == EnumFacing.Axis.X) {
                switch (source) {
                    case WEST:
                        if (worldIn.getBlockState(pos.east()).getBlock() instanceof IKineticActor) {
                            ((IKineticActor) worldIn.getBlockState(pos.east()).getBlock())
                                    .passRotation(worldIn, pos.east(), EnumFacing.WEST, iteratedBlocks,
                                            false, false, inverseRotation);
                        }
                        break;
                    case EAST:
                        if (worldIn.getBlockState(pos.west()).getBlock() instanceof IKineticActor) {
                            ((IKineticActor) worldIn.getBlockState(pos.west()).getBlock())
                                    .passRotation(worldIn, pos.west(), EnumFacing.EAST, iteratedBlocks,
                                            false, false, inverseRotation);
                        }
                        break;
                }
                iteratedBlocks.add(pos);
            }
            return;
        }
        if (worldIn.getBlockState(pos).getValue(AXIS) == EnumFacing.Axis.Z) {
            for (EnumFacing enumFacing : Arrays.asList(EnumFacing.UP, EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.WEST)) {
                if (worldIn.getRedstonePower(pos, enumFacing) > 0) {
                    worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(_BOOLEAN0, true), 0);
                    worldIn.markBlockRangeForRenderUpdate(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1,
                            pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
                    return;
                } else {
                    worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(_BOOLEAN0, false), 0);
                    worldIn.markBlockRangeForRenderUpdate(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1,
                            pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
                }
            }
            if (source.getAxis() == EnumFacing.Axis.Z) {
                switch (source) {
                    case NORTH:
                        if (worldIn.getBlockState(pos.south()).getBlock() instanceof IKineticActor) {
                            ((IKineticActor) worldIn.getBlockState(pos.south()).getBlock())
                                    .passRotation(worldIn, pos.south(), EnumFacing.NORTH, iteratedBlocks,
                                            false, false, inverseRotation);
                        }
                        break;
                    case SOUTH:
                        if (worldIn.getBlockState(pos.north()).getBlock() instanceof IKineticActor) {
                            ((IKineticActor) worldIn.getBlockState(pos.north()).getBlock())
                                    .passRotation(worldIn, pos.north(), EnumFacing.SOUTH, iteratedBlocks,
                                            false, false, inverseRotation);
                        }
                        break;
                }
                iteratedBlocks.add(pos);
            }
        }
    }

    private void actGearshift(World worldIn, BlockPos pos, EnumFacing source, List<BlockPos> iteratedBlocks, boolean inverseRotation) {
        if (worldIn.getBlockState(pos).getValue(AXIS) == EnumFacing.Axis.Y) {
            if (source.getAxis() == EnumFacing.Axis.Y) {
                boolean outputIsInverted;
                boolean foundTheStuff = false;
                for (EnumFacing enumFacing : Arrays.asList(EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST)) {
                    if (worldIn.getRedstonePower(pos, enumFacing) > 0) {
                        worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(_BOOLEAN0, true), 0);
                        worldIn.markBlockRangeForRenderUpdate(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1,
                                pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
                        foundTheStuff = true;
                    } else {
                        worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(_BOOLEAN0, foundTheStuff), 0);
                        worldIn.markBlockRangeForRenderUpdate(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1,
                                pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
                    }
                }

                if (foundTheStuff) {
                    outputIsInverted = !inverseRotation;
                } else {
                    outputIsInverted = inverseRotation;
                }

                switch (source) {
                    case UP:
                        if (worldIn.getBlockState(pos.down()).getBlock() instanceof IKineticActor) {
                            ((IKineticActor) worldIn.getBlockState(pos.down()).getBlock())
                                    .passRotation(worldIn, pos.down(), EnumFacing.UP, iteratedBlocks,
                                            false, false, outputIsInverted);
                        }
                        break;
                    case DOWN:
                        if (worldIn.getBlockState(pos.up()).getBlock() instanceof IKineticActor) {
                            ((IKineticActor) worldIn.getBlockState(pos.up()).getBlock())
                                    .passRotation(worldIn, pos.up(), EnumFacing.DOWN, iteratedBlocks,
                                            false, false, outputIsInverted);
                        }
                        break;
                }
                iteratedBlocks.add(pos);
            }
            return;
        }

        if (worldIn.getBlockState(pos).getValue(AXIS) == EnumFacing.Axis.X) {
            boolean outputIsInverted;
            boolean foundTheStuff = false;
            for (EnumFacing enumFacing : Arrays.asList(EnumFacing.NORTH, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.UP)) {
                if (worldIn.getRedstonePower(pos, enumFacing) > 0) {
                    worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(_BOOLEAN0, true), 0);
                    worldIn.markBlockRangeForRenderUpdate(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1,
                            pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
                    foundTheStuff = true;
                } else {
                    worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(_BOOLEAN0, foundTheStuff), 0);
                    worldIn.markBlockRangeForRenderUpdate(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1,
                            pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
                }
            }
            if (foundTheStuff) {
                outputIsInverted = !inverseRotation;
            } else {
                outputIsInverted = inverseRotation;
            }
            if (source.getAxis() == EnumFacing.Axis.X) {
                switch (source) {
                    case WEST:
                        if (worldIn.getBlockState(pos.east()).getBlock() instanceof IKineticActor) {
                            ((IKineticActor) worldIn.getBlockState(pos.east()).getBlock())
                                    .passRotation(worldIn, pos.east(), EnumFacing.WEST, iteratedBlocks,
                                            false, false, outputIsInverted);
                        }
                        break;
                    case EAST:
                        if (worldIn.getBlockState(pos.west()).getBlock() instanceof IKineticActor) {
                            ((IKineticActor) worldIn.getBlockState(pos.west()).getBlock())
                                    .passRotation(worldIn, pos.west(), EnumFacing.EAST, iteratedBlocks,
                                            false, false, outputIsInverted);
                        }
                        break;
                }
                iteratedBlocks.add(pos);
            }
            return;
        }
        if (worldIn.getBlockState(pos).getValue(AXIS) == EnumFacing.Axis.Z) {
            boolean outputIsInverted;
            boolean foundTheStuff = false;
            for (EnumFacing enumFacing : Arrays.asList(EnumFacing.UP, EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.WEST)) {
                if (worldIn.getRedstonePower(pos, enumFacing) > 0) {
                    worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(_BOOLEAN0, true), 0);
                    worldIn.markBlockRangeForRenderUpdate(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1,
                            pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
                    foundTheStuff = true;
                } else {
                    worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(_BOOLEAN0, foundTheStuff), 0);
                    worldIn.markBlockRangeForRenderUpdate(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1,
                            pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
                }
            }
            if (foundTheStuff) {
                outputIsInverted = !inverseRotation;
            } else {
                outputIsInverted = inverseRotation;
            }
            if (source.getAxis() == EnumFacing.Axis.Z) {
                switch (source) {
                    case NORTH:
                        if (worldIn.getBlockState(pos.south()).getBlock() instanceof IKineticActor) {
                            ((IKineticActor) worldIn.getBlockState(pos.south()).getBlock())
                                    .passRotation(worldIn, pos.south(), EnumFacing.NORTH, iteratedBlocks,
                                            false, false, outputIsInverted);
                        }
                        break;
                    case SOUTH:
                        if (worldIn.getBlockState(pos.north()).getBlock() instanceof IKineticActor) {
                            ((IKineticActor) worldIn.getBlockState(pos.north()).getBlock())
                                    .passRotation(worldIn, pos.north(), EnumFacing.SOUTH, iteratedBlocks,
                                            false, false, outputIsInverted);
                        }
                        break;
                }
                iteratedBlocks.add(pos);
            }
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
