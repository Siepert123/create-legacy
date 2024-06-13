package com.siepert.createlegacy.blocks;

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
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class BlockStone extends Block implements IHasModel, IMetaName {
    public static final PropertyEnum<EnumHandler.StoneEnumType> VARIANT = PropertyEnum.<EnumHandler.StoneEnumType>create("variant", EnumHandler.StoneEnumType.class);

    private static final String name = "stone";
    private static boolean addToInv;

    public BlockStone(boolean addToInv) {
        super(Material.ROCK);
        this.addToInv = addToInv;
        setUnlocalizedName("create:" + name);
        setRegistryName(name);
        setCreativeTab(CreateLegacy.TAB_CREATE_DECORATIONS);
        setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.StoneEnumType.CALCITE));
        setHarvestLevel("pickaxe", 0, this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.StoneEnumType.CALCITE));
        setHarvestLevel("pickaxe", 0, this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.StoneEnumType.TUFF));
        setHardness(1.5f);
        setResistance(3);

        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlockVariants(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return ((EnumHandler.StoneEnumType) state.getValue(VARIANT)).getMeta();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumHandler.StoneEnumType) state.getValue(VARIANT)).getMeta();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, EnumHandler.StoneEnumType.byMetaData(meta));
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Item.getItemFromBlock(this), 1, getMetaFromState(world.getBlockState(pos)));
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        if (addToInv) {
            for (EnumHandler.StoneEnumType variant : EnumHandler.StoneEnumType.values()) {
                items.add(new ItemStack(this, 1, variant.getMeta()));
            }
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {VARIANT});
    }

    @Override
    public String getSpecialName(ItemStack stack) {
        return "_" + EnumHandler.StoneEnumType.values()[stack.getItemDamage()].getName();
    }

    @Override
    public void registerModels() {
        for (int i = 0; i < EnumHandler.StoneEnumType.values().length; i++) {
            CreateLegacy.proxy.registerVariantRenderer(Item.getItemFromBlock(this),
                    i, "stone/stone_" + EnumHandler.StoneEnumType.values()[i].getName(), "inventory");
        }
    }
}
