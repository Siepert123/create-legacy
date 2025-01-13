package com.melonstudios.createlegacy.block;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.util.IMetaName;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.SoundType;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockFramedGlass extends BlockGlass implements IMetaName {

    public BlockFramedGlass() {
        super(Material.GLASS, false);
        setRegistryName("framed_glass");
        setUnlocalizedName("create.framed_glass");

        setHarvestLevel("pickaxe", 0);

        setHardness(0.3f);
        setResistance(0.3f);

        setSoundType(SoundType.GLASS);
        setCreativeTab(CreateLegacy.TAB_DECORATIONS);
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }

    @Override
    public int quantityDroppedWithBonus(int fortune, Random random) {
        return 1;
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        return 1;
    }

    public enum Variant implements IStringSerializable {
        NORMAL("normal"),
        HORIZONTAL("horizontal"),
        VERTICAL("vertical"),
        TILED("tiled");

        private final String name;
        Variant(String name) {
            this.name = name;
        }

        public int getID() {
            return ordinal();
        }
        public static Variant fromID(int id) {
            return values()[id % 4];
        }

        @Override
        public String getName() {
            return name;
        }
    }
    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", Variant.class);

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).getID();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VARIANT, Variant.fromID(meta));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(VARIANT, Variant.fromID(placer.getHeldItem(hand).getMetadata()));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).getID();
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, state.getValue(VARIANT).getID());
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1));
        items.add(new ItemStack(this, 1, 2));
        items.add(new ItemStack(this, 1, 3));
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return !(blockAccess.getBlockState(pos.offset(side)).getBlock() instanceof BlockFramedGlass);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        final String base = "tile.create.framed_glass";
        switch (stack.getMetadata()) {
            case 0: return base;
            case 1: return base + "_horizontal";
            case 2: return base + "_vertical";
            case 3: return base + "_tiled";
        }
        return base;
    }
}
