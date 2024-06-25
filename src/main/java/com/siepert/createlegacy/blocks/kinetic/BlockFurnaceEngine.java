package com.siepert.createlegacy.blocks.kinetic;

import com.siepert.createapi.IWrenchable;
import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.blocks.item.ItemBlockVariants;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.tileentity.TileEntityFurnaceFlywheel;
import com.siepert.createlegacy.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockFurnaceEngine extends Block implements IHasModel, IHasRotation, ITileEntityProvider, IMetaName, IWrenchable {
    @Override
    public String getSpecialName(ItemStack stack) {
        return Variant.fromMeta(stack.getItemDamage()).getName();
    }

    /**
     * Do something when the block is right-clicked with a Wrench.
     *
     * @param worldIn  The world.
     * @param pos      The position.
     * @param state    Your state.
     * @param side     The side that this block is clicked on.
     * @param playerIn The player who clicked.
     * @return True if the wrench actually did something (will trigger hand movement).
     */
    @Override
    public boolean onWrenched(World worldIn, BlockPos pos, IBlockState state, EnumFacing side, EntityPlayer playerIn) {
        if (state.getValue(VARIANT) == Variant.ENGINE) return false;

        rotateBlock(worldIn, pos, side);

        return true;
    }


    public enum Variant implements IStringSerializable {
        ENGINE(0, "engine"), FLYWHEEL(1, "flywheel");
        public final int meta;
        public final String name;
        Variant(int meta, String name) {
            this.meta = meta;
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        public static Variant fromMeta(int meta) {
            for (Variant v : Variant.values()) {
                if (v.meta == meta) {
                    return v;
                }
            }
            return Variant.ENGINE;
        }
    }
    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", Variant.class);

    public BlockFurnaceEngine() {
        super(Material.IRON);
        this.translucent = true;
        this.blockSoundType = SoundType.METAL;
        this.fullBlock = false;
        setLightOpacity(0);

        setUnlocalizedName("create:" + "furnace_");
        setRegistryName("furnace_engine");
        setCreativeTab(CreateLegacy.TAB_CREATE);
        setHarvestLevel("pickaxe", 0);
        setDefaultState(this.blockState.getBaseState()
                .withProperty(VARIANT, Variant.ENGINE)
                .withProperty(HORIZONTAL_FACING, EnumHorizontalFacing.NORTH)
                .withProperty(HAS_SHAFT, false));
        setHardness(2);
        setResistance(3);
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlockVariants(this).setRegistryName(this.getRegistryName()));
    }
    public static final PropertyEnum<EnumHorizontalFacing> HORIZONTAL_FACING = PropertyEnum.create("facing", EnumHorizontalFacing.class);
    public static final PropertyBool HAS_SHAFT = PropertyBool.create("has_shaft");
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).meta * 4 + state.getValue(HORIZONTAL_FACING).index();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, Variant.fromMeta((meta - meta % 4) / 4))
                .withProperty(HORIZONTAL_FACING, EnumHorizontalFacing.fromIndex(meta % 4));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {VARIANT, HORIZONTAL_FACING, HAS_SHAFT});
    }

    @Override
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(VARIANT, Variant.fromMeta(placer.getHeldItem(hand).getItemDamage()))
                .withProperty(HORIZONTAL_FACING, EnumHorizontalFacing.fromVanillaFacing(placer.getHorizontalFacing().getOpposite()));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).meta;
    }

    @Override
    public void registerModels() {
        CreateLegacy.proxy.registerVariantRenderer(Item.getItemFromBlock(this), 0, "furnace_engine", "inventory");
        CreateLegacy.proxy.registerVariantRenderer(Item.getItemFromBlock(this), 1, "furnace_flywheel", "inventory");
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(Item.getItemFromBlock(this), 1, 0));
        items.add(new ItemStack(Item.getItemFromBlock(this), 1, 1));
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        if (state.getValue(VARIANT) == Variant.ENGINE) return new ItemStack(this, 1, 0);
        return new ItemStack(this, 1, 1);
    }


    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        if (getStateFromMeta(meta).getValue(VARIANT) == Variant.ENGINE) return null;
        return new TileEntityFurnaceFlywheel();
    }

    public static void setState(boolean withShaft, World worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos);
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (withShaft) {
            worldIn.setBlockState(pos, state.withProperty(HAS_SHAFT, true), 3);
        } else {
            worldIn.setBlockState(pos, state.withProperty(HAS_SHAFT, false), 3);
        }

        if (tileEntity != null) {
            tileEntity.validate();
            worldIn.setTileEntity(pos, tileEntity);
        }
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        IBlockState state = world.getBlockState(pos);
        TileEntity tileEntity = world.getTileEntity(pos);

        EnumHorizontalFacing newFacing = state.getValue(HORIZONTAL_FACING).cycle();

        world.setBlockState(pos, state.withProperty(HORIZONTAL_FACING, newFacing), 3);

        if (tileEntity != null) {
            tileEntity.validate();
            world.setTileEntity(pos, tileEntity);
        }

        return true;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isTranslucent(IBlockState state) {
        return true;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
}
