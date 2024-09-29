package com.melonstudios.createlegacy.block;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.util.IMetaName;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class BlockMetal extends Block implements IMetaName {
    public BlockMetal() {
        super(Material.IRON);

        setRegistryName("metal");
        setUnlocalizedName("create.metal");

        setHardness(10.0f);
        setResistance(20.0f);

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
    }

    public enum Variant implements IStringSerializable {
        COPPER("copper", 0, MapColor.getBlockColor(EnumDyeColor.ORANGE)),
        ZINC("zinc", 1, MapColor.LIGHT_BLUE),
        BRASS("brass", 2, MapColor.GOLD);

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
        int met = placer.getHeldItem(hand).getMetadata();
        return getDefaultState().withProperty(METAL_TYPE, Variant.fromID(met));
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
    public int damageDropped(IBlockState state) {
        return state.getValue(METAL_TYPE).getID();
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.getValue(METAL_TYPE).getMapColor();
    }
}
