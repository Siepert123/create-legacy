package com.siepert.createlegacy.blocks.kinetic;

import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.blocks.item.ItemBlockVariants;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createlegacy.util.IKineticActor;
import com.siepert.createlegacy.util.IMetaName;
import com.siepert.createlegacy.util.handlers.EnumHandler;
import net.minecraft.block.Block;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("deprecation")
public class BlockKineticUtility extends Block implements IHasModel, IMetaName, IKineticActor {
    public static final PropertyEnum<EnumHandler.KineticUtilityEnumType> VARIANT
            = PropertyEnum.<EnumHandler.KineticUtilityEnumType>create("variant", EnumHandler.KineticUtilityEnumType.class);
    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);
    public static final PropertyBool _BOOLEAN0 = PropertyBool.create("_boolean0");

    public BlockKineticUtility() {
        super(Material.WOOD);
        setUnlocalizedName("");
        setRegistryName("kinetic_utility");
        setCreativeTab(CreateLegacy.TAB_CREATE);
        setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.KineticUtilityEnumType.GEARBOX)
                .withProperty(AXIS, EnumFacing.Axis.Y).withProperty(_BOOLEAN0, false));
        setHarvestLevel("pickaxe", 1,
                this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.KineticUtilityEnumType.GEARBOX));
        setHarvestLevel("pickaxe", 1,
                this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.KineticUtilityEnumType.CLUTCH));

        setHardness(2);
        setResistance(5);

        ModBlocks.BLOCKS.add(this);
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
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        switch (state.getValue(VARIANT)) {
            case GEARBOX:
                return new ItemStack(Item.getItemFromBlock(this), 1, 0);
            case CLUTCH:
                return new ItemStack(Item.getItemFromBlock(this), 1, 1);
            default:
                return new ItemStack(Items.AIR);
        }
    }

    @Override
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
                3, "kinetic_utility/shaft_encased_andesite", "inventory");
        CreateLegacy.proxy.registerVariantRenderer(Item.getItemFromBlock(this),
                4, "kinetic_utility/shaft_encased_brass", "inventory");
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(VARIANT, EnumHandler.KineticUtilityEnumType.values()[placer.getHeldItem(hand).getItemDamage()]);
    }

    @Override
    public void act(World worldIn, BlockPos pos, EnumFacing source) {
        EnumHandler.KineticUtilityEnumType blockLogicController = worldIn.getBlockState(pos).getValue(VARIANT);
        switch (blockLogicController) {
            case GEARBOX:
                actGearbox(worldIn, pos, source);
                break;
            case CLUTCH:
                actClutch(worldIn, pos, source);
                break;
            default:
                actEncasedShaft(worldIn, pos, source);
                break;
        }
    }

    private void actEncasedShaft(World worldIn, BlockPos pos, EnumFacing source) {
        if (worldIn.getBlockState(pos).getValue(AXIS) == EnumFacing.Axis.Y) {
            if (source.getAxis() == EnumFacing.Axis.Y) {
                switch (source) {
                    case UP:
                        if (worldIn.getBlockState(pos.down()).getBlock() instanceof IKineticActor) {
                            ((IKineticActor) worldIn.getBlockState(pos.down()).getBlock()).act(worldIn, pos.down(), EnumFacing.UP);
                        }
                        break;
                    case DOWN:
                        if (worldIn.getBlockState(pos.down()).getBlock() instanceof IKineticActor) {
                            ((IKineticActor) worldIn.getBlockState(pos.down()).getBlock()).act(worldIn, pos.up(), EnumFacing.DOWN);
                        }
                        break;
                }
            }
            return;
        }

        if (worldIn.getBlockState(pos).getValue(AXIS) == EnumFacing.Axis.X) {
            if (source.getAxis() == EnumFacing.Axis.X) {
                switch (source) {
                    case WEST:
                        if (worldIn.getBlockState(pos.down()).getBlock() instanceof IKineticActor) {
                            ((IKineticActor) worldIn.getBlockState(pos.down()).getBlock()).act(worldIn, pos.down(), EnumFacing.WEST);
                        }
                        break;
                    case EAST:
                        if (worldIn.getBlockState(pos.down()).getBlock() instanceof IKineticActor) {
                            ((IKineticActor) worldIn.getBlockState(pos.down()).getBlock()).act(worldIn, pos.up(), EnumFacing.EAST);
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
                        if (worldIn.getBlockState(pos.down()).getBlock() instanceof IKineticActor) {
                            ((IKineticActor) worldIn.getBlockState(pos.down()).getBlock()).act(worldIn, pos.down(), EnumFacing.NORTH);
                        }
                        break;
                    case SOUTH:
                        if (worldIn.getBlockState(pos.down()).getBlock() instanceof IKineticActor) {
                            ((IKineticActor) worldIn.getBlockState(pos.down()).getBlock()).act(worldIn, pos.up(), EnumFacing.SOUTH);
                        }
                        break;
                }
            }
        }
    }

    private void actGearbox(World worldIn, BlockPos pos, EnumFacing source) {
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


                for (EnumFacing output : AXLES_TO_ACT) {
                    IBlockState theBlock = worldIn.getBlockState(pos.offset(output));
                    if (theBlock.getBlock() instanceof IKineticActor) {
                        if (theBlock.getBlock() instanceof BlockCogwheel) {
                            if (theBlock.getValue(BlockCogwheel.AXIS) == output.getAxis()) {
                                ((IKineticActor) theBlock.getBlock()).act(worldIn, pos.offset(output), output.getOpposite());
                            }
                        } else {
                            ((IKineticActor) theBlock.getBlock()).act(worldIn, pos.offset(output), output.getOpposite());
                        }
                    }
                }
            }
        }
    }
    private void actClutch(World worldIn, BlockPos pos, EnumFacing source) {
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
                            ((IKineticActor) worldIn.getBlockState(pos.down()).getBlock()).act(worldIn, pos.down(), EnumFacing.UP);
                        }
                        break;
                    case DOWN:
                        if (worldIn.getBlockState(pos.down()).getBlock() instanceof IKineticActor) {
                            ((IKineticActor) worldIn.getBlockState(pos.down()).getBlock()).act(worldIn, pos.up(), EnumFacing.DOWN);
                        }
                        break;
                }
            }
        }
    }
}
