package com.melonstudios.createlegacy.copycat;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.block.ModBlocks;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

/**
 * Parent class of all copycat blocks.
 * Addon creators can extend this; all that needs to be implemented is the bounding box and meta save.
 * @see BlockCopycatPanel
 * @see BlockCopycatStep
 * @since 0.1.2
 * @author Siepert
 */
@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class BlockCopycat extends Block implements ITileEntityProvider {
    public static final PropertyBool COPYCATTING = PropertyBool.create("copycatting");

    public BlockCopycat(String registry) {
        super(Material.IRON, MapColor.CYAN);
        setRegistryName(registry);
        setUnlocalizedName("create." + registry);

        setHardness(5.0f);
        setResistance(6.0f);

        setCreativeTab(CreateLegacy.TAB_DECORATIONS);

        setHarvestLevel("pickaxe", 2);

        setDefaultState(this.blockState.getBaseState().withProperty(COPYCATTING, false));
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return state.getValue(COPYCATTING) ? EnumBlockRenderType.ENTITYBLOCK_ANIMATED : EnumBlockRenderType.MODEL;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public static void setItemModels() {
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.COPYCAT_PANEL));
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.COPYCAT_STEP));
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityCopycat) {
            IBlockState copyState = ((TileEntityCopycat)te).copyState;
            if (copyState != null) {
                return copyState.getBlock().getSoundType();
            }
        }
        return getSoundType();
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityCopycat) {
            IBlockState copyState = ((TileEntityCopycat)te).copyState;
            if (copyState != null) {
                return copyState.getBlock().getLightValue(copyState);
            }
        }
        return 0;
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
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityCopycat) {
            TileEntityCopycat copycat = (TileEntityCopycat) te;
            if (!player.isSneaking() && copycat.copyState != null) {
                return copycat.copyState.getBlock().getPickBlock(copycat.copyState, target, world, pos, player);
            }
        }
        return getItem(world, pos, state);
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
                    IBlockState copyState = block.getStateForPlacement(worldIn, pos, facing,
                            hitX, hitY, hitZ, handItem.getMetadata(), playerIn, EnumHand.MAIN_HAND);
                    copycat.copyState = copyState;
                    worldIn.setBlockState(pos, originState.withProperty(COPYCATTING, true), 3);
                    copycat.validate();
                    worldIn.setTileEntity(pos, copycat);
                    copycat.validate();
                    copycat.copyState = copyState;
                    copycat.updateClients();
                    if (!worldIn.isRemote) {
                        worldIn.playSound(null, pos, SoundEvents.ENTITY_ITEMFRAME_ADD_ITEM, SoundCategory.BLOCKS, 1, 1);
                    }
                    return true;
                }
            }
            if (handItem.isEmpty() && te instanceof TileEntityCopycat) {
                TileEntityCopycat copycat = (TileEntityCopycat) te;
                worldIn.setBlockState(pos, originState.withProperty(COPYCATTING, false), 3);
                copycat.validate();
                worldIn.setTileEntity(pos, copycat);
                copycat.validate();
                boolean filled = copycat.copyState != null;
                copycat.copyState = null;
                copycat.updateClients();
                if (!worldIn.isRemote && filled) {
                    worldIn.playSound(null, pos, SoundEvents.ENTITY_ITEMFRAME_REMOVE_ITEM, SoundCategory.BLOCKS, 1, 1);
                }
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
