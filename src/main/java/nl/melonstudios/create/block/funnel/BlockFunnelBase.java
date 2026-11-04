package nl.melonstudios.create.block.funnel;

import com.melonstudios.melonlib.misc.BlockStateProperties;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nl.melonstudios.create.block.BlockKineticBase;
import nl.melonstudios.create.init.ItemInit;
import nl.melonstudios.create.tileentity.funnel.TileEntityFunnelBase;
import nl.melonstudios.create.util.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
public abstract class BlockFunnelBase extends Block implements ITileEntityProvider {
    public static final PropertyBool POWERED = BlockStateProperties.POWERED;

    public final boolean isAdvanced;
    public final String set;
    public BlockFunnelBase(String set, boolean advanced) {
        super(Material.IRON, MapColor.IRON);
        this.blockSoundType = SoundType.METAL;
        this.set = set;
        this.isAdvanced = advanced;
        this.blockHardness = BlockProperties.IRON_HARDNESS;
        this.blockResistance = BlockProperties.IRON_RESISTANCE;
        this.setHarvestLevel("pickaxe", 0);
        this.setCreativeTab(ItemInit.TAB_CREATE);
    }

    //region this is not a full block
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

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    //endregion

    @Override
    protected final BlockStateContainer createBlockState() {
        List<IProperty<?>> properties = new ArrayList<>();
        this.addStateProperties(properties);
        return new BlockStateContainer(this, properties.toArray(new IProperty[0]));
    }
    protected void addStateProperties(List<IProperty<?>> properties) {
        properties.add(POWERED);
    }

    @Nullable
    @Override
    public abstract TileEntityFunnelBase createNewTileEntity(World worldIn, int meta);

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return this.isAdvanced ? MapColor.GOLD : MapColor.STONE;
    }

    @Override
    public abstract IBlockState getStateFromMeta(int meta);
    @Override
    public abstract int getMetaFromState(IBlockState state);

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (hand != EnumHand.MAIN_HAND || !this.isAdvanced) return false;
        return SubInteractionBox.handleInteraction(worldIn, pos, playerIn, false,
                playerIn.isSneaking(), playerIn.getHeldItemMainhand(), hitX, hitY, hitZ);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        boolean poweredBefore = state.getValue(POWERED);
        boolean poweredNow = BlockKineticBase.isPosPowered(worldIn, pos);
        if (poweredBefore != poweredNow) {
            Utils.setBlockTESafe(worldIn, pos, state.withProperty(POWERED, poweredNow), 3);
        }
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(FunnelSets.get(this.set).getItem());
    }
    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(FunnelSets.get(this.set).getItem());
    }
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return FunnelSets.get(this.set).getItem();
    }

    @Override
    public boolean isAssociatedBlock(Block other) {
        return other instanceof BlockFunnelBase && ((BlockFunnelBase)other).set.equals(this.set);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
}
