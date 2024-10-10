package com.melonstudios.createlegacy.block.fluid;

import com.melonstudios.createlegacy.fluid.ModFluids;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockFluidChocolate extends BlockFluidBase {
    public BlockFluidChocolate() {
        super(ModFluids.chocolate(), ModFluids.CHOCOLATE);
    }

    @Override
    public int getQuantaValue(IBlockAccess world, BlockPos pos) {
        return 0;
    }

    @Override
    public boolean canCollideCheck(@Nonnull IBlockState state, boolean fullHit) {
        return false;
    }

    @Override
    public int getMaxRenderHeightMeta() {
        return 0;
    }

    @Override
    public int place(World world, BlockPos pos, @Nonnull FluidStack fluidStack, boolean doPlace) {
        return 0;
    }

    @Nullable
    @Override
    public FluidStack drain(World world, BlockPos pos, boolean doDrain) {
        if (doDrain) world.setBlockToAir(pos);
        return new FluidStack(ModFluids.chocolate(), 1000);
    }

    @Override
    public boolean canDrain(World world, BlockPos pos) {
        return world.getBlockState(pos).getValue(BlockFluidBase.LEVEL) == 7;
    }
}
