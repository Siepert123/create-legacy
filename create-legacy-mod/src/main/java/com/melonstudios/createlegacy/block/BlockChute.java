package com.melonstudios.createlegacy.block;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.tileentity.TileEntityChute;
import com.melonstudios.melonlib.misc.AABB;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
public class BlockChute extends Block implements ITileEntityProvider, IWrenchable {
    public BlockChute() {
        super(Material.IRON);

        setRegistryName("chute");
        setUnlocalizedName("create.chute");

        setHarvestLevel("pickaxe", 1);

        setHardness(5.0f);
        setResistance(6.0f);

        setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, Variant.SOLID));

        setCreativeTab(CreateLegacy.TAB_KINETICS);
    }

    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", Variant.class);

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityChute();
    }

    protected IBlockState solid() {
        return getDefaultState().withProperty(VARIANT, Variant.SOLID);
    }
    protected IBlockState window() {
        return getDefaultState().withProperty(VARIANT, Variant.WINDOW);
    }

    @Override
    public boolean onWrenched(World world, BlockPos pos, IBlockState state, EnumFacing side, EntityPlayer wrenchHolder) {
        TileEntityChute chute = (TileEntityChute) world.getTileEntity(pos);

        if (state.getValue(VARIANT) == Variant.ENCASED) {
            world.setBlockState(pos, solid(), 3);
        } else {
            if (state.getValue(VARIANT) == Variant.SOLID) world.setBlockState(pos, window(), 3);
            else world.setBlockState(pos, solid());
        }

        if (chute != null) {
            chute.validate();
            world.setTileEntity(pos, chute);
        }

        return true;
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        return isEncased(blockState) ? 15 : this.blockHardness;
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        return isEncased(world.getBlockState(pos)) ? 50 : this.blockResistance;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (playerIn.getHeldItem(hand).getItem() == Item.getItemFromBlock(ModBlocks.INDUSTRIAL_IRON) && !isEncased(state) && !playerIn.isSneaking()) {
            TileEntityChute chute = (TileEntityChute) worldIn.getTileEntity(pos);
            ItemStack stack;
            if (chute != null) {
                stack = chute.getStackInSlot(0);
            } else stack = ItemStack.EMPTY;

            worldIn.setBlockState(pos, state.withProperty(VARIANT, Variant.ENCASED), 3);

            if (chute != null) {
                chute.validate();
                worldIn.setTileEntity(pos, chute);
                chute.setInventorySlotContents(0, stack);
            }

            return true;
        } else if (playerIn.getHeldItem(hand).isEmpty()) {
            TileEntityChute chute = (TileEntityChute) worldIn.getTileEntity(pos);
            if (chute != null && !chute.getStack().isEmpty() && !playerIn.isSneaking()) {
                if (!worldIn.isRemote) {
                    EntityItem item = new EntityItem(worldIn,
                            playerIn.posX, playerIn.posY, playerIn.posZ,
                            chute.removeStackFromSlot(0));
                    item.motionX = item.motionY = item.motionZ = 0;
                    worldIn.spawnEntity(item);
                }
                return true;
            }
        }
        return false;
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
    protected boolean isEncased(IBlockState state) {
        return state.getValue(VARIANT) == Variant.ENCASED;
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
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isTranslucent(IBlockState state) {
        return true;
    }

    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 0;
    }

    @Override
    public int getLightOpacity(IBlockState state) {
        return 0;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }

    public static final AxisAlignedBB CHUTE_AABB = AABB.create(1, 0, 1, 15, 16, 15);

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return isEncased(state) ? FULL_BLOCK_AABB : CHUTE_AABB;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        if (entityIn instanceof EntityItem) {
            AxisAlignedBB aabb = Block.FULL_BLOCK_AABB.offset(pos);
            if (entityBox.intersects(aabb)) collidingBoxes.add(aabb);
        } else {
            for (AxisAlignedBB aabb : getCollisionBoxList(state)) {
                if (aabb.offset(pos).intersects(entityBox)) collidingBoxes.add(aabb.offset(pos));
            }
        }
    }

    private static final List<AxisAlignedBB> encasedAABBList = new ArrayList<>();
    private static final List<AxisAlignedBB> AABBList = new ArrayList<>();

    static {
        encasedAABBList.add(AABB.create(0, 0, 0, 2, 16, 16));
        encasedAABBList.add(AABB.create(14, 0, 0, 16, 16, 16));
        encasedAABBList.add(AABB.create(0, 0, 0, 16, 16, 2));
        encasedAABBList.add(AABB.create(0, 0, 14, 16, 16, 16));

        AABBList.add(AABB.create(1, 0, 1, 2, 16, 15));
        AABBList.add(AABB.create(14, 0, 1, 15, 16, 15));
        AABBList.add(AABB.create(1, 0, 1, 15, 16, 2));
        AABBList.add(AABB.create(1, 0, 14, 15, 16, 15));
    }

    protected static List<AxisAlignedBB> getCollisionBoxList(IBlockState state) {
        if (((BlockChute)state.getBlock()).isEncased(state)) {
            return encasedAABBList;
        } else {
            return AABBList;
        }
    }


}
