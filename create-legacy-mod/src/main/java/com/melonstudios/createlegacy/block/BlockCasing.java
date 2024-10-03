package com.melonstudios.createlegacy.block;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.util.IMetaName;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCasing extends Block implements IMetaName {
    public BlockCasing() {
        super(Material.ROCK);
        setSoundType(SoundType.WOOD);

        setRegistryName("casing");
        setUnlocalizedName("create.casing");

        setCreativeTab(CreateLegacy.TAB_KINETICS);

        setHardness(2.5f);
        setResistance(5.0f);
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
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VARIANT, Variant.fromID(meta));
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
}
