package com.melonstudios.createlegacy.block.redstone;

import com.melonstudios.createlegacy.block.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockRedstonePulseExtender extends AbstractBlockRedstoneCircuit {
    public BlockRedstonePulseExtender(boolean powered) {
        super(powered, "pulse_extender");
    }

    @Override
    protected int getDelay(IBlockState state) {
        return 1;
    }

    @Override
    protected IBlockState getPoweredState(IBlockState unpoweredState) {
        return ModBlocks.PULSE_EXTENDER_POWERED.getDefaultState().withProperty(FACING, unpoweredState.getValue(FACING));
    }

    @Override
    protected IBlockState getUnpoweredState(IBlockState poweredState) {
        return ModBlocks.PULSE_EXTENDER.getDefaultState().withProperty(FACING, poweredState.getValue(FACING));
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(ModBlocks.PULSE_EXTENDER);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(ModBlocks.PULSE_EXTENDER);
    }
}
