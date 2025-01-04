package com.melonstudios.createlegacy.copycat;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.block.ModBlocks;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class BlockCopycat extends Block implements ITileEntityProvider {
    public static final PropertyBool COPYCATTING = PropertyBool.create("copycatting");

    public BlockCopycat(String registry) {
        super(Material.IRON, MapColor.CYAN);
        setRegistryName(registry);
        setUnlocalizedName("create." + registry);

        setHardness(2.0f);
        setResistance(4.0f);

        setDefaultState(this.blockState.getBaseState().withProperty(COPYCATTING, false));
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return state.getValue(COPYCATTING) ? EnumBlockRenderType.INVISIBLE : EnumBlockRenderType.MODEL;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public static void setItemModels() {
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.COPYCAT_PANEL));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(COPYCATTING) ? 1 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return meta == 1 ? getDefaultState().withProperty(COPYCATTING, true) : getDefaultState().withProperty(COPYCATTING, false);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityCopycat) {
            return ((TileEntityCopycat) te).copyState != null ? state.withProperty(COPYCATTING, true) : state.withProperty(COPYCATTING, false);
        }
        return state;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (hand == EnumHand.OFF_HAND) return false;
        ItemStack handItem = playerIn.getHeldItem(EnumHand.MAIN_HAND);
        TileEntity te = worldIn.getTileEntity(pos);
        IBlockState originState = worldIn.getBlockState(pos);
        if (originState.getBlock() instanceof BlockCopycat) {
            if (handItem.getItem() instanceof ItemBlock && te instanceof TileEntityCopycat) {
                ItemBlock item = (ItemBlock) handItem.getItem();
                TileEntityCopycat copycat = (TileEntityCopycat) te;
                if (copycat.copyState == null) {
                    Block block = item.getBlock();
                    if (block instanceof BlockCopycat) return false;
                    copycat.copyState = block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, handItem.getMetadata(), playerIn, EnumHand.MAIN_HAND);
                    worldIn.setBlockState(pos, originState.withProperty(COPYCATTING, true), 3);
                    copycat.validate();
                    worldIn.setTileEntity(pos, copycat);
                    copycat.validate();
                    return true;
                }
            }
            if (handItem.isEmpty() && te instanceof TileEntityCopycat) {
                TileEntityCopycat copycat = (TileEntityCopycat) te;
                copycat.copyState = null;
                worldIn.setBlockState(pos, originState.withProperty(COPYCATTING, false), 3);
                copycat.validate();
                worldIn.setTileEntity(pos, copycat);
                copycat.validate();
                return true;
            }
            if (te instanceof TileEntityCopycat) {
                ((TileEntityCopycat)te).updateClients();
            }
        }
        return false;
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
        ((TileEntityCopycat)worldIn.getTileEntity(pos)).updateClients();
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityCopycat();
    }

    public abstract void render(IBlockState state, TileEntityCopycat te, double x, double y, double z);

    //region stuff
    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 0;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isTranslucent(IBlockState state) {
        return true;
    }

    @Override
    public boolean isNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    //endregion
}
