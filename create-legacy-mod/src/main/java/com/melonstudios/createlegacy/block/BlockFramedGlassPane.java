package com.melonstudios.createlegacy.block;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.util.IMetaName;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockPane;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockFramedGlassPane extends BlockPane implements IMetaName {
    protected BlockFramedGlassPane() {
        super(Material.GLASS, true);
        setRegistryName("framed_glass_pane");
        setUnlocalizedName("create.framed_glass_pane");

        setHarvestLevel("pickaxe", 0);

        setHardness(0.3f);
        setResistance(0.3f);

        setSoundType(SoundType.GLASS);
        setCreativeTab(CreateLegacy.TAB_DECORATIONS);
    }

    public enum Variant implements IStringSerializable {
        NORMAL("normal"),
        HORIZONTAL("horizontal"),
        VERTICAL("vertical"),
        TILED("tiled");

        private final String name;
        Variant(String name) {
            this.name = name;
        }

        public int getID() {
            return ordinal();
        }
        public static BlockFramedGlassPane.Variant fromID(int id) {
            return values()[id % 4];
        }

        @Override
        public String getName() {
            return name;
        }
    }
    public static final PropertyEnum<BlockFramedGlass.Variant> VARIANT = PropertyEnum.create("variant", BlockFramedGlass.Variant.class);

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT, NORTH, EAST, SOUTH, WEST);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).getID();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VARIANT, BlockFramedGlass.Variant.fromID(meta));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(VARIANT, BlockFramedGlass.Variant.fromID(placer.getHeldItem(hand).getMetadata()));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).getID();
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, state.getValue(VARIANT).getID());
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1));
        items.add(new ItemStack(this, 1, 2));
        items.add(new ItemStack(this, 1, 3));
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        if (side == EnumFacing.UP) return shouldRenderUp(blockState, blockAccess, pos);
        if (side == EnumFacing.DOWN) return shouldRenderDown(blockState, blockAccess, pos);

        return blockAccess.getBlockState(pos.offset(side)).isSideSolid(blockAccess, pos.offset(side), side.getOpposite());
    }
    private boolean shouldRenderUp(IBlockState state, IBlockAccess blockAccess, BlockPos pos) {
        IBlockState upState = blockAccess.getBlockState(pos.up());

        if (upState.getBlock() instanceof BlockFramedGlassPane) {
            boolean flag1 = upState.getValue(NORTH) != state.getValue(NORTH);
            boolean flag2 = upState.getValue(EAST) != state.getValue(EAST);
            boolean flag3 = upState.getValue(SOUTH) != state.getValue(SOUTH);
            boolean flag4 = upState.getValue(WEST) != state.getValue(WEST);
            return (flag1 || flag2 || flag3 || flag4);
        }

        return !upState.isSideSolid(blockAccess, pos.up(), EnumFacing.DOWN);
    }
    private boolean shouldRenderDown(IBlockState state, IBlockAccess blockAccess, BlockPos pos) {
        IBlockState downState = blockAccess.getBlockState(pos.down());

        if (downState.getBlock() instanceof BlockFramedGlassPane) {
            boolean flag1 = downState.getValue(NORTH) != state.getValue(NORTH);
            boolean flag2 = downState.getValue(EAST) != state.getValue(EAST);
            boolean flag3 = downState.getValue(SOUTH) != state.getValue(SOUTH);
            boolean flag4 = downState.getValue(WEST) != state.getValue(WEST);
            return (flag1 || flag2 || flag3 || flag4);
        }

        return !downState.isSideSolid(blockAccess, pos.down(), EnumFacing.UP);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        final String base = "tile.create.framed_glass_pane";
        switch (stack.getMetadata()) {
            case 0: return base;
            case 1: return base + "_horizontal";
            case 2: return base + "_vertical";
            case 3: return base + "_tiled";
        }
        return base;
    }
}
