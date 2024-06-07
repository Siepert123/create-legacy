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
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class BlockKineticUtility extends Block implements IHasModel, IMetaName, IKineticActor {
    public static final PropertyEnum<EnumHandler.KineticUtilityEnumType> VARIANT
            = PropertyEnum.<EnumHandler.KineticUtilityEnumType>create("variant", EnumHandler.KineticUtilityEnumType.class);
    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);

    public BlockKineticUtility() {
        super(Material.IRON);
        setUnlocalizedName("");
        setRegistryName("kinetic_utility");
        setCreativeTab(CreateLegacy.TAB_CREATE);
        setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.KineticUtilityEnumType.GEARBOX)
                .withProperty(AXIS, EnumFacing.Axis.Y));
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
        IBlockState j = this.getDefaultState().withProperty(VARIANT, EnumHandler.KineticUtilityEnumType.byMetaData(meta / 4));
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
        return new ItemStack(Item.getItemFromBlock(this), 1, getMetaFromState(world.getBlockState(pos)));
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (EnumHandler.KineticUtilityEnumType variant : EnumHandler.KineticUtilityEnumType.values()) {
            items.add(new ItemStack(this, 1, variant.getMeta()));
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {VARIANT, AXIS});
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
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(VARIANT, EnumHandler.KineticUtilityEnumType.values()[placer.getHeldItem(hand).getItemDamage()]);
    }

    @Override
    public void act(World worldIn, BlockPos pos, EnumFacing source) {

    }
}
