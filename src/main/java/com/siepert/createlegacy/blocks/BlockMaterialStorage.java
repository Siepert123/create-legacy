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
public class BlockMaterialStorage extends Block implements IHasModel, IMetaName {
    public static final PropertyEnum<EnumHandler.MaterialStorageEnumType> VARIANT = PropertyEnum.<EnumHandler.MaterialStorageEnumType>create("variant", EnumHandler.MaterialStorageEnumType.class);

    private static final String name = "storage_block";

    public BlockMaterialStorage() {
        super(Material.IRON);
        setUnlocalizedName("create:" + name);
        setRegistryName(name);
        setCreativeTab(CreateLegacy.TAB_CREATE);
        setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.MaterialStorageEnumType.COPPER));
        setHarvestLevel("pickaxe", 1,
                this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.MaterialStorageEnumType.COPPER));
        setHarvestLevel("pickaxe", 2,
                this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.MaterialStorageEnumType.ZINC));
        setHarvestLevel("pickaxe", 2,
                this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.MaterialStorageEnumType.BRASS));
        setHarvestLevel("pickaxe", 1,
                this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.MaterialStorageEnumType.ANDESITE_ALLOY));

        setHardness(2.5f);
        setResistance(6);

        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlockVariants(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return ((EnumHandler.MaterialStorageEnumType) state.getValue(VARIANT)).getMeta();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumHandler.MaterialStorageEnumType) state.getValue(VARIANT)).getMeta();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, EnumHandler.MaterialStorageEnumType.byMetaData(meta));
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Item.getItemFromBlock(this), 1, getMetaFromState(world.getBlockState(pos)));
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (EnumHandler.MaterialStorageEnumType variant : EnumHandler.MaterialStorageEnumType.values()) {
            items.add(new ItemStack(this, 1, variant.getMeta()));
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {VARIANT});
    }

    @Override
    public String getSpecialName(ItemStack stack) {
        return "_" + EnumHandler.MaterialStorageEnumType.values()[stack.getItemDamage()].getName();
    }

    @Override
    public void registerModels() {
        for (int i = 0; i < EnumHandler.MaterialStorageEnumType.values().length; i++) {
            CreateLegacy.proxy.registerVariantRenderer(Item.getItemFromBlock(this),
                    i, "block_" + EnumHandler.MaterialStorageEnumType.values()[i].getName(), "inventory");
        }
    }
}
