package com.siepert.createlegacy.blocks.redstone;

import com.siepert.createapi.IWrenchable;
import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.tileentity.redstone.TileEntityRedstoneLink;
import com.siepert.createlegacy.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockRedstoneLink extends Block implements IHasModel, IWrenchable {
    public BlockRedstoneLink() {
        super(Material.ROCK);
        setSoundType(SoundType.WOOD);

        setRegistryName("redstone_link");
        setUnlocalizedName("create:redstone_link");

        setDefaultState(this.blockState.getBaseState().withProperty(RECEIVER, false).withProperty(FACING, EnumFacing.DOWN)
                .withProperty(POWERED, false));

        setCreativeTab(CreateLegacy.TAB_CREATE);

        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName("redstone_link"));
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, RECEIVER, POWERED);
    }

    public static final PropertyBool RECEIVER = PropertyBool.create("receiver");
    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    @Override
    public void registerModels() {
        CreateLegacy.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.values()[meta % 6]);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(FACING, facing.getOpposite());
    }

    @Override
    public boolean onWrenched(World worldIn, BlockPos pos, IBlockState state, EnumFacing side, EntityPlayer playerIn) {
        TileEntity e = worldIn.getTileEntity(pos);

        worldIn.setBlockState(pos, state.cycleProperty(RECEIVER), 3);

        if (e != null) {
            e.validate();
            worldIn.setTileEntity(pos, e);
        }

        return true;
    }

    @Override
    public boolean onBlockActivated(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state,
                                    @Nullable EntityPlayer playerIn, @Nonnull EnumHand hand, @Nonnull EnumFacing facing,
                                    float hitX, float hitY, float hitZ) {
        if (playerIn != null) {
            ItemStack stack = playerIn.getHeldItem(hand);

            if (stack.isEmpty() || stack.getItem() == Items.DYE) {
                TileEntityRedstoneLink link = (TileEntityRedstoneLink) worldIn.getTileEntity(pos);

                if (link != null) {
                    if (stack.isEmpty()) {
                        link.setFilter(null);
                    } else {
                        link.setFilter(EnumDyeColor.byDyeDamage(stack.getItemDamage()));
                    }
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntityRedstoneLink link = (TileEntityRedstoneLink) worldIn.getTileEntity(pos);

        if (link != null) {
            link.onBreak();
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
        return true;
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return blockState.getValue(POWERED) ? 15 : 0;
    }
}
