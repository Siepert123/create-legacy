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

import javax.annotation.Nonnull;

@SuppressWarnings("deprecation")
public class BlockCasing extends Block implements IHasModel, IMetaName {
    public static final PropertyEnum<EnumHandler.CasingMaterialEnumType> VARIANT = PropertyEnum.create("variant", EnumHandler.CasingMaterialEnumType.class);

    private static final String name = "casing";

    public BlockCasing() {
        super(Material.IRON);
        setUnlocalizedName("create:" + name);
        setRegistryName(name);
        setCreativeTab(CreateLegacy.TAB_CREATE);
        setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.CasingMaterialEnumType.ANDESITE));
        setHarvestLevel("pickaxe", 1,
                this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.CasingMaterialEnumType.ANDESITE));
        setHarvestLevel("pickaxe", 1,
                this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.CasingMaterialEnumType.COPPER));
        setHarvestLevel("pickaxe", 2,
                this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.CasingMaterialEnumType.BRASS));
        setHarvestLevel("pickaxe", 2,
                this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.CasingMaterialEnumType.TRAIN));

        setHardness(2);
        setResistance(5);

        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlockVariants(this).setRegistryName("casing"));
    }

    @Override
    public int damageDropped(@Nonnull IBlockState state) {
        return state.getValue(VARIANT).getMeta();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).getMeta();
    }

    @Override
    public @Nonnull IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, EnumHandler.CasingMaterialEnumType.byMetaData(meta));
    }

    @Override
    public @Nonnull ItemStack getPickBlock(@Nonnull IBlockState state, @Nonnull RayTraceResult target,
                                           World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player) {
        return new ItemStack(Item.getItemFromBlock(this), 1, getMetaFromState(world.getBlockState(pos)));
    }

    @Override
    public void getSubBlocks(@Nonnull CreativeTabs itemIn, @Nonnull NonNullList<ItemStack> items) {
        for (EnumHandler.CasingMaterialEnumType variant : EnumHandler.CasingMaterialEnumType.values()) {
            items.add(new ItemStack(this, 1, variant.getMeta()));
        }
    }

    @Override
    protected @Nonnull BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    public String getSpecialName(ItemStack stack) {
        return "_" + EnumHandler.CasingMaterialEnumType.values()[stack.getItemDamage()].getName();
    }

    @Override
    public void registerModels() {
        for (int i = 0; i < EnumHandler.CasingMaterialEnumType.values().length; i++) {
            CreateLegacy.proxy.registerVariantRenderer(Item.getItemFromBlock(this),
                    i, "casing_" + EnumHandler.CasingMaterialEnumType.values()[i].getName(), "inventory");
        }
    }
}
