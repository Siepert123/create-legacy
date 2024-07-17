package com.siepert.createlegacy.blocks.kinetic;

import com.siepert.createapi.IWrenchable;
import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.blocks.item.ItemBlockVariants;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.tileentity.TileEntityFunnel;
import com.siepert.createlegacy.tileentity.TileEntityFunnelAdvanced;
import com.siepert.createlegacy.util.EnumHorizontalFacing;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createlegacy.util.IMetaName;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockFunnel extends Block implements IHasModel, IMetaName, ITileEntityProvider, IWrenchable {
    public static final PropertyBool ADVANCED = PropertyBool.create("advanced");
    public static final PropertyBool DISABLED = PropertyBool.create("disabled");
    public static final PropertyBool EXTRACTING = PropertyBool.create("extracting");
    public static final PropertyEnum<EnumHorizontalFacing> FACING = PropertyEnum.create("facing", EnumHorizontalFacing.class);

    public BlockFunnel() {
        super(Material.IRON);

        setRegistryName("funnel");
        setUnlocalizedName("create:funnel");

        setCreativeTab(CreateLegacy.TAB_CREATE);

        setHarvestLevel("pickaxe", 1);
        setHardness(1);
        setResistance(2);

        setDefaultState(this.blockState.getBaseState().withProperty(ADVANCED, false)
                .withProperty(EXTRACTING, true).withProperty(FACING, EnumHorizontalFacing.NORTH)
                .withProperty(DISABLED, false));

        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlockVariants(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ADVANCED, DISABLED, EXTRACTING, FACING);
    }

    @Override
    public void registerModels() {
        CreateLegacy.proxy.registerVariantRenderer(Item.getItemFromBlock(this), 0, "funnel/basic", "inventory");
        CreateLegacy.proxy.registerVariantRenderer(Item.getItemFromBlock(this), 1, "funnel/advanced", "inventory");
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1));
    }

    @Override
    public int damageDropped(IBlockState state) {
        if (state.getValue(ADVANCED)) return 1;
        return 0;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        if (state.getValue(ADVANCED)) return new ItemStack(this, 1, 1);
        return new ItemStack(this, 1, 0);
    }

    @Override
    public String getSpecialName(ItemStack stack) {
        if (stack.getMetadata() > 0) {
            return "_advanced";
        }
        return "";
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        if (meta >= 8) {
            return new TileEntityFunnelAdvanced();
        }
        return new TileEntityFunnel();
    }

    @Override
    public boolean onWrenched(World worldIn, BlockPos pos, IBlockState state, EnumFacing side, EntityPlayer playerIn) {
        setState(worldIn, pos, !state.getValue(EXTRACTING), state.getValue(DISABLED));
        return true;
    }

    public static void setState(World world, BlockPos pos, boolean extracting, boolean disabled) {
        TileEntity tileEntity = world.getTileEntity(pos);
        ItemStack filter;
        if (tileEntity != null) {
            if (tileEntity instanceof TileEntityFunnelAdvanced) {
                filter = ((TileEntityFunnelAdvanced) tileEntity).getFilter().copy();
            } else filter = ItemStack.EMPTY;
        } else filter = ItemStack.EMPTY;
        world.setBlockState(pos, world.getBlockState(pos).withProperty(EXTRACTING, extracting).withProperty(DISABLED, disabled), 3);

        if (tileEntity != null) {
            tileEntity.validate();
            if (tileEntity instanceof TileEntityFunnelAdvanced) {
                ((TileEntityFunnelAdvanced) tileEntity).setFilter(filter.copy());
            }
            world.setTileEntity(pos, tileEntity);
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (playerIn.getHeldItem(hand).getItem() != ModItems.WRENCH) {
            if (state.getValue(ADVANCED)) {
                TileEntityFunnelAdvanced tileEntityFunnelAdvanced = (TileEntityFunnelAdvanced) worldIn.getTileEntity(pos);
                if (tileEntityFunnelAdvanced != null) {
                    if (playerIn.getHeldItem(hand).isEmpty()) {
                        tileEntityFunnelAdvanced.clearFilter();
                        if (!worldIn.isRemote) {
                            playerIn.sendStatusMessage(new TextComponentString("Filter cleared"), true);
                        }
                    } else {
                        tileEntityFunnelAdvanced.setFilter(playerIn.getHeldItem(hand).copy());
                        if (!worldIn.isRemote) {
                            playerIn.sendStatusMessage(new TextComponentString("Filter set to "
                                + playerIn.getHeldItem(hand).getDisplayName()), true);
                        }
                    }
                    return true;
                }
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        EnumHorizontalFacing facing1 = EnumHorizontalFacing.fromVanillaFacing(facing);
        boolean extracting = !placer.isSneaking();
        boolean advanced = placer.getHeldItem(hand).getMetadata() > 0;

        return this.getDefaultState().withProperty(FACING, facing1).withProperty(EXTRACTING, extracting).withProperty(ADVANCED, advanced);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        switch (state.getValue(FACING)) {
            case NORTH:
                //nothing
                break;
            case EAST:
                meta++;
                break;
            case SOUTH:
                meta += 2;
                break;
            case WEST:
                meta +=3;
                break;
        }
        if (state.getValue(EXTRACTING)) meta += 4;
        if (state.getValue(ADVANCED)) meta += 8;

        return meta;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumHorizontalFacing facing = EnumHorizontalFacing.NORTH;
        boolean extracting = false;
        boolean advanced = false;

        if (meta >= 8) {
            meta -= 8;
            advanced = true;
        }
        if (meta >= 4) {
            meta -= 4;
            extracting = true;
        }
        switch (meta) {
            case 0:
                facing = EnumHorizontalFacing.NORTH;
                break;
            case 1:
                facing = EnumHorizontalFacing.EAST;
                break;
            case 2:
                facing = EnumHorizontalFacing.SOUTH;
                break;
            case 3:
                facing = EnumHorizontalFacing.WEST;
                break;
        }

        return getDefaultState().withProperty(ADVANCED, advanced).withProperty(EXTRACTING, extracting).withProperty(FACING, facing);
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

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return Block.NULL_AABB;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }
}
