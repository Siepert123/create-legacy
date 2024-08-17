package com.siepert.createlegacy.blocks.kinetic;

import com.siepert.createapi.IWrenchable;
import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.CreateLegacyConfigHolder;
import com.siepert.createlegacy.blocks.item.ItemBlockVariants;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.tileentity.TileEntitySpeedometer;
import com.siepert.createlegacy.tileentity.TileEntityStressometer;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createlegacy.util.IMetaName;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
public class BlockNetworkMeter extends Block implements IHasModel, IWrenchable, ITileEntityProvider, IMetaName {

    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);
    public static final PropertyBool ALT  = PropertyBool.create("alt");

    public BlockNetworkMeter(String name) {
        super(Material.ROCK);
        this.translucent = true;
        this.blockSoundType = SoundType.WOOD;
        this.fullBlock = false;
        setLightOpacity(0);

        setUnlocalizedName("create:" + name);
        setRegistryName(name);
        setCreativeTab(CreateLegacy.TAB_CREATE);
        setHarvestLevel("axe", 0);

        setDefaultState(this.blockState.getBaseState()
                .withProperty(AXIS, EnumFacing.Axis.X)
                .withProperty(ALT, false));

        setHardness(1);
        setResistance(2);
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlockVariants(this).setRegistryName(this.getRegistryName()));


    }

    @Override
    public int getMetaFromState(IBlockState state) {
        switch (state.getValue(AXIS)) {
            case X:
                if (state.getValue(ALT)) return 1;
                return 0;
            case Z:
                if (state.getValue(ALT)) return 3;
                return 2;
        }
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        switch (meta) {
            case 0:
                return this.getDefaultState().withProperty(AXIS, EnumFacing.Axis.X).withProperty(ALT, false);
            case 1:
                return this.getDefaultState().withProperty(AXIS, EnumFacing.Axis.X).withProperty(ALT, true);
            case 2:
                return this.getDefaultState().withProperty(AXIS, EnumFacing.Axis.Z).withProperty(ALT, false);
            case 3:
                return this.getDefaultState().withProperty(AXIS, EnumFacing.Axis.Z).withProperty(ALT, true);
            default:
                return this.getDefaultState();
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS, ALT);
    }

    @Override
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        boolean j = meta == 1;
        if (placer != null) {
            return this.getDefaultState().withProperty(AXIS, placer.getHorizontalFacing()
                    .rotateAround(EnumFacing.Axis.Y).getAxis()).withProperty(ALT, j);
        }
        return this.getDefaultState().withProperty(AXIS, facing.rotateY().getAxis()).withProperty(ALT, j);
    }

    @Override
    public void registerModels() {
        if (CreateLegacyConfigHolder.otherConfig.sillyStuff) {
            CreateLegacy.proxy.registerVariantRenderer(Item.getItemFromBlock(this), 0, "speedometer", "inventory");
            CreateLegacy.proxy.registerVariantRenderer(Item.getItemFromBlock(this), 1, "stressometer", "inventory");
        } else {
            CreateLegacy.proxy.registerVariantRenderer(Item.getItemFromBlock(this), 0, "speedometer_hd", "inventory");
            CreateLegacy.proxy.registerVariantRenderer(Item.getItemFromBlock(this), 1, "stressometer_hd", "inventory");
        }
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1));
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

    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true; // Needed for computercraft
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        IBlockState state = world.getBlockState(pos);
        TileEntity tileEntity = world.getTileEntity(pos);

        if (state.getValue(AXIS) == EnumFacing.Axis.X) {
            world.setBlockState(pos, state.withProperty(AXIS, EnumFacing.Axis.Z), 3);
        } else {
            world.setBlockState(pos, state.withProperty(AXIS, EnumFacing.Axis.X), 3);
        }

        if (tileEntity != null) {
            tileEntity.validate();
            world.setTileEntity(pos, tileEntity);
        }

        return true;
    }
    @Override
    public boolean onWrenched(World worldIn, BlockPos pos, IBlockState state, EnumFacing side, EntityPlayer playerIn) {
        rotateBlock(worldIn, pos, side);

        return true;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
                                    EntityPlayer playerIn, EnumHand hand, EnumFacing facing,
                                    float hitX, float hitY, float hitZ) {
        TileEntity entity = worldIn.getTileEntity(pos);

        if (entity != null && !worldIn.isRemote) {
            if (state.getValue(ALT)) {
                playerIn.sendStatusMessage(new TextComponentString(((TileEntityStressometer) entity).getMessage()), true);
            } else {
                playerIn.sendStatusMessage(new TextComponentString(((TileEntitySpeedometer) entity).getMessage()), true);
            }
        }
        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        if (meta == 0 || meta == 2) return new TileEntitySpeedometer();
        return new TileEntityStressometer();
    }

    @Override
    public String getSpecialName(ItemStack stack) {
        if (stack.getMetadata() == 0) return "_speed";
        return "_stress";
    }

    @Override
    public int damageDropped(IBlockState state) {
        if (state.getValue(ALT)) return 1;
        return 0;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        if (state.getValue(ALT)) return new ItemStack(this, 1, 1);
        return new ItemStack(this, 1, 0);
    }
}
