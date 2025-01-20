package com.melonstudios.createlegacy.block;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.util.IMetaName;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class BlockMetal extends Block implements IMetaName {
    public BlockMetal() {
        super(Material.IRON);

        setSoundType(SoundType.METAL);

        setHarvestLevel("pickaxe", 0, getDefaultState().withProperty(METAL_TYPE, Variant.ANDESITE_ALLOY));
        setHarvestLevel("pickaxe", 1, getDefaultState().withProperty(METAL_TYPE, Variant.COPPER));
        setHarvestLevel("pickaxe", 2, getDefaultState().withProperty(METAL_TYPE, Variant.ZINC));
        setHarvestLevel("pickaxe", 2, getDefaultState().withProperty(METAL_TYPE, Variant.BRASS));

        setRegistryName("metal");
        setUnlocalizedName("create.metal");

        setHardness(5.0f);
        setResistance(6.0f);

        setDefaultState(this.blockState.getBaseState().withProperty(
                METAL_TYPE, Variant.COPPER
        ));

        setCreativeTab(CreateLegacy.TAB_KINETICS);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "tile.create.block_" + Variant.fromID(stack.getMetadata()).getName();
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1));
        items.add(new ItemStack(this, 1, 2));
        items.add(new ItemStack(this, 1, 3));
    }

    public enum Variant implements IStringSerializable {
        ANDESITE_ALLOY("andesite_alloy", 0, MapColor.SILVER),
        COPPER("copper", 1, MapColor.getBlockColor(EnumDyeColor.ORANGE)),
        ZINC("zinc", 2, MapColor.LIGHT_BLUE),
        BRASS("brass", 3, MapColor.GOLD),;

        Variant(String name, int ID, MapColor mapColor) {
            this.name = name;
            this.ID = ID;
            this.mapColor = mapColor;
        }

        private final String name;
        private final int ID;
        private final MapColor mapColor;

        @Override
        public @Nonnull String getName() {
            return this.name;
        }

        public int getID() {
            return this.ID;
        }
        public static Variant fromID(int ID) {
            return values()[ID];
        }

        public MapColor getMapColor() {
            return this.mapColor;
        }
    }

    public static final PropertyEnum<Variant> METAL_TYPE = PropertyEnum.create("metal", Variant.class);

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(METAL_TYPE, Variant.fromID(meta));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, METAL_TYPE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(METAL_TYPE).getID();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(METAL_TYPE, Variant.fromID(meta));
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, state.getValue(METAL_TYPE).getID());
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(METAL_TYPE).getID();
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.getValue(METAL_TYPE).getMapColor();
    }
}
