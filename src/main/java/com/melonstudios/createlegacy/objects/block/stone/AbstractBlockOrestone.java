package com.melonstudios.createlegacy.objects.block.stone;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.util.IMetaName;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

@SuppressWarnings("deprecation")
public abstract class AbstractBlockOrestone extends Block implements IMetaName {
    protected AbstractBlockOrestone(String registry) {
        super(Material.ROCK);

        setRegistryName(registry);
        setUnlocalizedName("create." + registry);

        setHardness(5.0f);
        setResistance(20.0f);

        setDefaultState(this.blockState.getBaseState().withProperty(
                STONE_TYPE, StoneType.ASURINE
        ));

        setCreativeTab(CreateLegacy.TAB_DECORATIONS);
    }

    public enum StoneType implements IStringSerializable {
        ASURINE("asurine", 0, MapColor.BLUE),
        CRIMSITE("crimsite",1, MapColor.NETHERRACK),
        LIMESTONE("limestone", 2, MapColor.WHITE_STAINED_HARDENED_CLAY),
        OCHRUM("ochrum", 3, MapColor.GOLD),
        SCORCHIA("scorchia", 4, MapColor.BLACK),
        SCORIA("scoria", 5, MapColor.BROWN),
        VERIDIUM("veridium", 6, MapColor.GREEN);

        StoneType(String name, int ID, MapColor color) {
            this.name = name;
            this.ID = ID;
            this.mapColor = color;
        }

        public int ID() {
            return this.ID;
        }
        public static int getID(StoneType type) {
            return type.ID();
        }
        @Nonnull
        public static StoneType fromID(int ID) {
            if (ID >= values().length || ID < 0) return StoneType.ASURINE;
            return values()[ID];
        }
        @Nonnull
        public MapColor color() {
            return this.mapColor;
        }

        private final String name;
        private final int ID;
        private final MapColor mapColor;

        @Override
        @Nonnull
        public String getName() {
            return name;
        }
    }

    public static final PropertyEnum<StoneType> STONE_TYPE = PropertyEnum.create("type", StoneType.class);

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(STONE_TYPE, StoneType.fromID(meta));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, STONE_TYPE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(STONE_TYPE).ID();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(STONE_TYPE, StoneType.fromID(meta));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(STONE_TYPE).ID();
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.getValue(STONE_TYPE).color();
    }
}
