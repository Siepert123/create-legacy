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
public class BlockOre extends Block implements IHasModel, IMetaName {
    public static final PropertyEnum<EnumHandler.OreEnumType> VARIANT = PropertyEnum.<EnumHandler.OreEnumType>create("variant", EnumHandler.OreEnumType.class);

    private static final String name = "ore";

    public BlockOre() {
        super(Material.ROCK);
        setUnlocalizedName("create:" + name);
        setRegistryName(name);
        setCreativeTab(CreateLegacy.TAB_CREATE);
        setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.OreEnumType.COPPER));
        setHarvestLevel("pickaxe", 1, this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.OreEnumType.COPPER));
        setHarvestLevel("pickaxe", 2, this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.OreEnumType.ZINC));
        setHardness(3);
        setResistance(6);

        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlockVariants(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return ((EnumHandler.OreEnumType) state.getValue(VARIANT)).getMeta();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumHandler.OreEnumType) state.getValue(VARIANT)).getMeta();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, EnumHandler.OreEnumType.byMetaData(meta));
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Item.getItemFromBlock(this), 1, getMetaFromState(world.getBlockState(pos)));
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (EnumHandler.OreEnumType variant : EnumHandler.OreEnumType.values()) {
            items.add(new ItemStack(this, 1, variant.getMeta()));
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {VARIANT});
    }

    @Override
    public String getSpecialName(ItemStack stack) {
        return "_" + EnumHandler.OreEnumType.values()[stack.getItemDamage()].getName();
    }

    @Override
    public void registerModels() {
        for (int i = 0; i < EnumHandler.OreEnumType.values().length; i++) {
            CreateLegacy.proxy.registerVariantRenderer(Item.getItemFromBlock(this),
                    i, "ore/ore_" + EnumHandler.OreEnumType.values()[i].getName(), "inventory");
        }
    }
}
