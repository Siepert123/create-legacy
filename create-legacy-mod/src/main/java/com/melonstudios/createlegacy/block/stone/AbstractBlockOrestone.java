package com.melonstudios.createlegacy.block.stone;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.block.ModBlocks;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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

/**
 * Mother of all Orestones.
 * Really just an organization thing, I guess.
 * @since 0.1.0
 * @author Siepert123
 * @see BlockOrestone
 * @see BlockOrestoneBricks
 */
@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class AbstractBlockOrestone extends Block implements IMetaName {
    protected AbstractBlockOrestone(String registry) {
        super(Material.ROCK);

        setRegistryName(registry);
        setUnlocalizedName("create." + registry);

        setHardness(5.0f);
        setResistance(10.0f);

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

        public int getID() {
            return this.ID;
        }
        @Nonnull
        public static StoneType fromID(int ID) {
            return values()[ID];
        }
        @Nonnull
        public MapColor getMapColor() {
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
        int met = placer.getHeldItem(hand).getMetadata();
        return getDefaultState().withProperty(STONE_TYPE, StoneType.fromID(met));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, STONE_TYPE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(STONE_TYPE).getID();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(STONE_TYPE, StoneType.fromID(meta));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(STONE_TYPE).getID();
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.getValue(STONE_TYPE).getMapColor();
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, state.getValue(STONE_TYPE).getID());
    }

    /**
     * @return The orestone prefix (eg. {@code stone})
     */
    protected abstract String getOrestonePrefix();

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "tile.create." + getOrestonePrefix() + "_" + StoneType.fromID(stack.getMetadata()).getName();
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1));
        items.add(new ItemStack(this, 1, 2));
        items.add(new ItemStack(this, 1, 3));
        items.add(new ItemStack(this, 1, 4));
        items.add(new ItemStack(this, 1, 5));
        items.add(new ItemStack(this, 1, 6));
    }

    /**
     * Sets item models for orestones.
     * These functions are placed here for organization.
     */
    public static void setItemModels() {
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE),
                0, "orestone/stone_asurine");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE),
                1, "orestone/stone_crimsite");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE),
                2, "orestone/stone_limestone");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE),
                3, "orestone/stone_ochrum");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE),
                4, "orestone/stone_scorchia");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE),
                5, "orestone/stone_scoria");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE),
                6, "orestone/stone_veridium");

        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_POLISHED),
                0, "orestone/stone_polished_asurine");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_POLISHED),
                1, "orestone/stone_polished_crimsite");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_POLISHED),
                2, "orestone/stone_polished_limestone");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_POLISHED),
                3, "orestone/stone_polished_ochrum");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_POLISHED),
                4, "orestone/stone_polished_scorchia");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_POLISHED),
                5, "orestone/stone_polished_scoria");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_POLISHED),
                6, "orestone/stone_polished_veridium");

        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_BRICKS),
                0, "orestone/stone_bricks_asurine");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_BRICKS),
                1, "orestone/stone_bricks_crimsite");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_BRICKS),
                2, "orestone/stone_bricks_limestone");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_BRICKS),
                3, "orestone/stone_bricks_ochrum");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_BRICKS),
                4, "orestone/stone_bricks_scorchia");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_BRICKS),
                5, "orestone/stone_bricks_scoria");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_BRICKS),
                6, "orestone/stone_bricks_veridium");

        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_BRICKS_FANCY),
                0, "orestone/stone_bricks_fancy_asurine");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_BRICKS_FANCY),
                1, "orestone/stone_bricks_fancy_crimsite");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_BRICKS_FANCY),
                2, "orestone/stone_bricks_fancy_limestone");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_BRICKS_FANCY),
                3, "orestone/stone_bricks_fancy_ochrum");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_BRICKS_FANCY),
                4, "orestone/stone_bricks_fancy_scorchia");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_BRICKS_FANCY),
                5, "orestone/stone_bricks_fancy_scoria");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_BRICKS_FANCY),
                6, "orestone/stone_bricks_fancy_veridium");

        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_PILLAR_Y),
                0, "orestone/stone_pillar_y_asurine");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_PILLAR_Y),
                1, "orestone/stone_pillar_y_crimsite");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_PILLAR_Y),
                2, "orestone/stone_pillar_y_limestone");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_PILLAR_Y),
                3, "orestone/stone_pillar_y_ochrum");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_PILLAR_Y),
                4, "orestone/stone_pillar_y_scorchia");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_PILLAR_Y),
                5, "orestone/stone_pillar_y_scoria");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_PILLAR_Y),
                6, "orestone/stone_pillar_y_veridium");

        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_LAYERED),
                0, "orestone/stone_layered_asurine");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_LAYERED),
                1, "orestone/stone_layered_crimsite");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_LAYERED),
                2, "orestone/stone_layered_limestone");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_LAYERED),
                3, "orestone/stone_layered_ochrum");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_LAYERED),
                4, "orestone/stone_layered_scorchia");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_LAYERED),
                5, "orestone/stone_layered_scoria");
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.ORESTONE_LAYERED),
                6, "orestone/stone_layered_veridium");
    }
}
