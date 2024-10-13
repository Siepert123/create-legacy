package com.melonstudios.createlegacy.block;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.tileentity.TileEntityChute;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockChute extends Block implements ITileEntityProvider {
    public BlockChute() {
        super(Material.IRON);

        setRegistryName("chute");
        setUnlocalizedName("create.chute");

        setHarvestLevel("pickaxe", 1);

        setHardness(10.0f);
        setResistance(20.0f);

        setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, Variant.SOLID));

        setCreativeTab(CreateLegacy.TAB_KINETICS);
    }

    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", Variant.class);

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityChute();
    }

    public enum Variant implements IStringSerializable {
        SOLID("solid"),
        WINDOW("window"),
        ENCASED("encased");
        Variant(String name) {
            this.name = name;
        }

        private final String name;
        @Override
        public String getName() {
            return name;
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VARIANT, Variant.values()[meta % 3]);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntityChute chute = (TileEntityChute) worldIn.getTileEntity(pos);

        if (chute != null) {
            if (!chute.getStackInSlot(0).isEmpty()) {
                worldIn.spawnEntity(new EntityItem(worldIn,
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        chute.getStackInSlot(0)));
            }
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    protected boolean b(IBlockState state) {
        return state.getValue(VARIANT) == Variant.ENCASED;
    }
    @Override
    public boolean isFullBlock(IBlockState state) {
        return b(state);
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return b(state);
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return b(state);
    }

    @Override
    public boolean isNormalCube(IBlockState state) {
        return b(state);
    }

    @Override
    public boolean isTranslucent(IBlockState state) {
        return !b(state);
    }

    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        return b(state) ? 15 : 0;
    }

    @Override
    public int getLightOpacity(IBlockState state) {
        return b(state) ? 15 : 0;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return b(state);
    }

    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return b(state);
    }

    protected static final AxisAlignedBB CHUTE_AABB = CreateLegacy.aabb(1, 0, 1, 15, 16, 15);

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return b(state) ? FULL_BLOCK_AABB : CHUTE_AABB;
    }
}
