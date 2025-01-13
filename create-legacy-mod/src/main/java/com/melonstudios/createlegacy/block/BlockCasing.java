package com.melonstudios.createlegacy.block;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.util.IMetaName;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nullable;

public class BlockCasing extends Block implements IMetaName {
    public BlockCasing() {
        super(Material.ROCK);
        setSoundType(SoundType.WOOD);

        setHarvestLevel("pickaxe", -1);

        setRegistryName("casing");
        setUnlocalizedName("create.casing");

        setCreativeTab(CreateLegacy.TAB_KINETICS);

        setHardness(2.5f);
        setResistance(5.5f);
    }

    public enum Variant implements IStringSerializable {
        ANDESITE,
        COPPER,
        BRASS,
        TRAIN;
        public int getID() {
            return ordinal();
        }
        public static Variant fromID(int id) {
            return values()[id % 4];
        }

        @Override
        public String getName() {
            switch (getID()) {
                case 0: return "andesite";
                case 1: return "copper";
                case 2: return "brass";
                case 3: return "train";
                default: return "andesite";
            }

        }
    }
    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", Variant.class);

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(VARIANT, Variant.fromID(placer.getHeldItem(hand).getMetadata()));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).getID();
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).getID();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VARIANT, Variant.fromID(meta));
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
    public String getUnlocalizedName(ItemStack stack) {
        switch (stack.getMetadata()) {
            case 0: return "tile.create.casing_andesite";
            case 1: return "tile.create.casing_copper";
            case 2: return "tile.create.casing_brass";
            case 3: return "tile.create.casing_train";
            default: return "null";
        }
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        return state.getValue(VARIANT) == Variant.TRAIN ? SoundType.METAL : SoundType.WOOD;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (worldIn.isRemote && !placer.getName().startsWith("Player") && !Loader.isModLoaded("ctm")) placer.sendMessage(new TextComponentString("WHERE CTM???"));
        //Inform people of the lack of CTM!
    }
}
