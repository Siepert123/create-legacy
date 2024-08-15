package com.siepert.createlegacy.blocks.kinetic;

import com.siepert.createapi.CreateAPI;
import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.CreateLegacyConfigHolder;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.tileentity.TileEntityPress;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createapi.IKineticActor;
import com.siepert.createapi.IWrenchable;
import com.siepert.createlegacy.CreateLegacyModData;
import com.siepert.createlegacy.util.handlers.ModSoundHandler;
import com.siepert.createlegacy.util.handlers.recipes.CompactingRecipes;
import com.siepert.createlegacy.util.handlers.recipes.PressingRecipes;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
public class BlockMechanicalPress extends Block implements IHasModel, IWrenchable, ITileEntityProvider {
    private static final AxisAlignedBB BB = new AxisAlignedBB(0.0, 2.0 / 16.0, 0.0, 1.0, 1.0, 1.0);
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BB;
    }

    public static final PropertyBool EXTENDED = PropertyBool.create("extended");
    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);
    public BlockMechanicalPress(String name) {
        super(Material.ROCK);
        this.translucent = true;
        this.blockSoundType = SoundType.WOOD;
        this.fullBlock = false;
        setLightOpacity(0);

        setUnlocalizedName("create:" + name);
        setRegistryName(name);
        setCreativeTab(CreateLegacy.TAB_CREATE);
        setHarvestLevel("axe", 0);

        setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.X)
                .withProperty(EXTENDED, false));

        setHardness(1);
        setResistance(2);
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(CreateAPI.stressImpactTooltip(CreateLegacyConfigHolder.kineticConfig.mechanicalPressStressImpact));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        switch (state.getValue(AXIS)) {
            case X:
                return 0;
            case Z:
                return 1;
        }
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        switch (meta) {
            case 0:
                return this.getDefaultState().withProperty(AXIS, EnumFacing.Axis.X);
            case 1:
                return this.getDefaultState().withProperty(AXIS, EnumFacing.Axis.Z);
            default:
                return this.getDefaultState();
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {AXIS, EXTENDED });
    }

    @Override
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (placer != null) {
            return this.getDefaultState().withProperty(AXIS, placer.getHorizontalFacing().getAxis());
        }
        return this.getDefaultState().withProperty(AXIS, facing.getAxis());
    }

    @Override
    public void registerModels() {
        CreateLegacy.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
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
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityPress();
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
}
