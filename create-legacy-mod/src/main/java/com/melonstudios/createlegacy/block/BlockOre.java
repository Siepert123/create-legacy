package com.melonstudios.createlegacy.block;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.util.IMetaName;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockOre extends Block implements IMetaName {
    public BlockOre() {
        super(Material.ROCK);

        setRegistryName("ore");
        setUnlocalizedName("create.ore");

        setHardness(4.0f);
        setResistance(8.0f);

        setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, Variant.COPPER));
        setHarvestLevel("pickaxe", 1, getDefaultState().withProperty(VARIANT, Variant.COPPER));
        setHarvestLevel("pickaxe", 2, getDefaultState().withProperty(VARIANT, Variant.ZINC));

        setCreativeTab(CreateLegacy.TAB_KINETICS);
    }

    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("type", Variant.class);

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return stack.getMetadata() == 0 ? "tile.create.ore_copper" : "tile.create.ore_zinc";
    }

    public enum Variant implements IStringSerializable {
        COPPER, ZINC;

        @Override
        public String getName() {
            return this == COPPER ? "copper" : "zinc";
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1,  0));
        items.add(new ItemStack(this, 1,  1));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).ordinal();
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        int met = placer.getHeldItem(hand).getMetadata() % 2;
        return getStateFromMeta(met);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return meta == 0 ? getDefaultState().withProperty(VARIANT, Variant.COPPER) : getDefaultState().withProperty(VARIANT, Variant.ZINC);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, state.getValue(VARIANT).ordinal());
    }
}
