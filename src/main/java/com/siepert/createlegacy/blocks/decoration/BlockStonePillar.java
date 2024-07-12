package com.siepert.createlegacy.blocks.decoration;

import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.blocks.item.ItemBlockVariants;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.IHasModel;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class BlockStonePillar extends Block implements IHasModel, IMetaName {
    public static final PropertyEnum<EnumHandler.DecoStoneEnumType> VARIANT = PropertyEnum.<EnumHandler.DecoStoneEnumType>create("variant", EnumHandler.DecoStoneEnumType.class);
    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.<EnumFacing.Axis>create("axis", EnumFacing.Axis.class);

    public static final PropertyBool CONNECTED_TOP = PropertyBool.create("connected_top");
    public static final PropertyBool CONNECTED_BOTTOM = PropertyBool.create("connected_bottom");

    private static final String name = "stone_pillar";

    public BlockStonePillar() {
        super(Material.ROCK);
        setUnlocalizedName("create:" + name);
        setRegistryName(name);
        setCreativeTab(CreateLegacy.TAB_CREATE_DECORATIONS);
        setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.DecoStoneEnumType.ASURINE)
                .withProperty(CONNECTED_TOP, false).withProperty(CONNECTED_BOTTOM, false)
                .withProperty(AXIS, EnumFacing.Axis.Y));
        setHarvestLevel("pickaxe", 1);
        setHarvestLevel("pickaxe", 0, this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.DecoStoneEnumType.CALCITE));
        setHarvestLevel("pickaxe", 0, this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.DecoStoneEnumType.TUFF));
        setHardness(5f);
        setResistance(20f);

        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlockVariants(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public int damageDropped(IBlockState state) {
        int meta = ((EnumHandler.DecoStoneEnumType) state.getValue(VARIANT)).getMeta();
        return (meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumHandler.DecoStoneEnumType) state.getValue(VARIANT)).getMeta();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        boolean topCon, belowCon;
        if (state.getValue(AXIS) == EnumFacing.Axis.Y) {
            topCon = worldIn.getBlockState(pos.up()).getBlock() == ModBlocks.STONE_PILLAR;
            belowCon = worldIn.getBlockState(pos.down()).getBlock() == ModBlocks.STONE_PILLAR;
        } else if (state.getValue(AXIS) == EnumFacing.Axis.X) {
            topCon = worldIn.getBlockState(pos.east()).getBlock() == ModBlocks.STONE_PILLAR;
            belowCon = worldIn.getBlockState(pos.west()).getBlock() == ModBlocks.STONE_PILLAR;
        } else if (state.getValue(AXIS) == EnumFacing.Axis.Z) {
            topCon = worldIn.getBlockState(pos.north()).getBlock() == ModBlocks.STONE_PILLAR;
            belowCon = worldIn.getBlockState(pos.south()).getBlock() == ModBlocks.STONE_PILLAR;
        } else {
            topCon = false;
            belowCon = false;
        }
        return state.withProperty(CONNECTED_TOP, topCon).withProperty(CONNECTED_BOTTOM, belowCon);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, EnumHandler.DecoStoneEnumType.byMetaData(meta));
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        int baseMeta = getMetaFromState(world.getBlockState(pos));
        return new ItemStack(Item.getItemFromBlock(this), 1, baseMeta);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (EnumHandler.DecoStoneEnumType variant : EnumHandler.DecoStoneEnumType.values()) {
            items.add(new ItemStack(this, 1, variant.getMeta()));
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {VARIANT, AXIS, CONNECTED_TOP, CONNECTED_BOTTOM});
    }

    @Override
    public String getSpecialName(ItemStack stack) {
        return "_" + EnumHandler.DecoStoneEnumType.values()[stack.getItemDamage()].getName();
    }

    @Override
    public void registerModels() {
        for (int i = 0; i < EnumHandler.DecoStoneEnumType.values().length; i++) {
            String s = EnumHandler.DecoStoneEnumType.values()[i].getName();
            CreateLegacy.proxy.registerVariantRenderer(Item.getItemFromBlock(this),
                    i, "stone/stone_pillar/" + s, "inventory");

        }
    }


    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        EnumFacing.Axis axis = facing.getAxis();
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(AXIS, axis);
    }
}
