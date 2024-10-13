package com.melonstudios.createlegacy.block;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.tileentity.TileEntityBlazeBurner;
import com.melonstudios.createlegacy.util.EnumBlazeLevel;
import com.melonstudios.createlegacy.util.IMetaName;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockBlazeBurner extends Block implements ITileEntityProvider, IMetaName {
    public BlockBlazeBurner() {
        super(Material.IRON);

        setRegistryName("blaze_burner");
        setUnlocalizedName("create.blaze_burner");

        setHarvestLevel("pickaxe", 1);

        setHardness(5.0f);
        setResistance(10.0f);

        setSoundType(SoundType.METAL);
        setCreativeTab(CreateLegacy.TAB_DECORATIONS);
    }

    public static final PropertyBool HAS_BLAZE = PropertyBool.create("has_blaze");

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HAS_BLAZE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(HAS_BLAZE) ? 1 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(HAS_BLAZE, meta == 1);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(HAS_BLAZE, placer.getHeldItem(hand).getMetadata() == 1);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, state.getValue(HAS_BLAZE) ? 1 : 0);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(HAS_BLAZE) ? 1 : 0;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return stack.getMetadata() == 0 ? "tile.create.blaze_burner_empty" : "tile.create.blaze_burner";
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return meta == 1 ? new TileEntityBlazeBurner() : null;
    }

    public EnumBlazeLevel getBlazeLevel(World world, BlockPos pos, IBlockState state) {
        if (state.getValue(HAS_BLAZE)) {
            TileEntityBlazeBurner blazeBurner = (TileEntityBlazeBurner) world.getTileEntity(pos);
            if (blazeBurner != null) {
                return blazeBurner.getBlazeLevel();
            }
        } return EnumBlazeLevel.NONE;
    }
}
