package com.siepert.createlegacy.blocks.redstone;

import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.IHasModel;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockRedstoneToggleLatch extends BlockRedstoneDiode implements IHasModel {
    public static final PropertyBool TOGGLED = PropertyBool.create("toggled");

    public BlockRedstoneToggleLatch(boolean powered) {
        super(powered);

        if (powered)
            setRegistryName("toggle_latch_powered");
        else
            setRegistryName("toggle_latch");

        setDefaultState(this.blockState.getBaseState().withProperty(TOGGLED, false).withProperty(FACING, EnumFacing.NORTH));

        ModBlocks.BLOCKS.add(this);
        if (!powered) ModItems.ITEMS.add(new ItemBlock(this).setRegistryName("toggle_latch"));
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
        if (side != null) {
            return side.getAxis() == state.getValue(FACING).getAxis();
        }
        return false;
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return state.getValue(TOGGLED);
    }

    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    protected int getDelay(IBlockState state) {
        return 0;
    }

    @Override
    protected IBlockState getPoweredState(IBlockState unpoweredState) {
        return ModBlocks.TOGGLE_LATCH_POWERED.getDefaultState().withProperty(TOGGLED, unpoweredState.getValue(TOGGLED))
                .withProperty(FACING, unpoweredState.getValue(FACING));
    }

    @Override
    protected IBlockState getUnpoweredState(IBlockState poweredState) {
        return ModBlocks.TOGGLE_LATCH.getDefaultState().withProperty(TOGGLED, poweredState.getValue(TOGGLED))
                .withProperty(FACING, poweredState.getValue(FACING));
    }

    @Override
    public void registerModels() {
        CreateLegacy.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        if (this == ModBlocks.TOGGLE_LATCH) {
            return Item.getItemFromBlock(this);
        } else {
            return ModBlocks.TOGGLE_LATCH.getItemDropped(state, rand, fortune);
        }
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(this);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.HORIZONTALS[meta / 2]).withProperty(TOGGLED, meta % 2 == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = state.getValue(TOGGLED) ? 1 : 0;
        i += state.getValue(FACING).getHorizontalIndex() * 2;
        return i;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TOGGLED, FACING);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        this.notifyNeighbors(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!playerIn.capabilities.allowEdit) {
            return false;
        }
        worldIn.setBlockState(pos, state.cycleProperty(TOGGLED), 3);
        worldIn.playSound(null,
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 1.0f ,1.0f);
        return true;
    }
}
