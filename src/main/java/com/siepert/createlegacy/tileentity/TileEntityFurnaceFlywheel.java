package com.siepert.createlegacy.tileentity;

import com.siepert.createlegacy.blocks.kinetic.BlockFurnaceEngine;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.util.IKineticActor;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

import static com.siepert.createlegacy.blocks.kinetic.BlockFurnaceEngine.*;

public class TileEntityFurnaceFlywheel extends TileEntity implements ITickable {
    @Override
    public void update() {
        IBlockState myState = world.getBlockState(pos);

        if (shouldPower(myState)) {
            if (world.getTotalWorldTime() % 10 == 0) {
                Block block = world.getBlockState(pos.offset(myState.getValue(HORIZONTAL_FACING).toVanillaFacing().getOpposite())).getBlock();
                if (block instanceof IKineticActor) {
                    List<BlockPos> iteratedBlocks = new ArrayList<>(); //Generate the iteratedBlocks list for using
                    ((IKineticActor) block).passRotation(world, pos.offset(myState.getValue(HORIZONTAL_FACING).toVanillaFacing().getOpposite()),
                            myState.getValue(HORIZONTAL_FACING).getOpposite().toVanillaFacing(),
                            iteratedBlocks, false, false);
                    world.markBlockRangeForRenderUpdate(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1,
                            pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
                }
            }
            if (world.getTotalWorldTime() % 100 == 0) {
                world.playSound(null,
                        pos.getX() + 0.5,
                        pos.getY() + 0.5,
                        pos.getZ() + 0.5,
                        SoundEvents.BLOCK_FIRE_EXTINGUISH,
                        SoundCategory.BLOCKS,
                        0.5f, 0.8f);
            }
        }

        if (shouldIUpdate(hasShaft(myState), myState)) setState(hasShaft(myState), world, pos);
    }

    private boolean shouldIUpdate(boolean shaft, IBlockState myState) {
        if (myState.getValue(HAS_SHAFT) == shaft) return false;
        return true;
    }

    private boolean hasShaft(IBlockState myState) {
        EnumFacing vanillaFacing = myState.getValue(HORIZONTAL_FACING).toVanillaFacing();

        boolean isRightSided = false;
        boolean hasValidEngine = false;
        boolean hasFurnacePowered = false;

        EnumFacing facingCW =       vanillaFacing.rotateY();
        EnumFacing facingCCW =      vanillaFacing.rotateYCCW();
        IBlockState blockStateCW =  world.getBlockState(pos.offset(facingCW, 2));
        IBlockState blockStateCCW = world.getBlockState(pos.offset(facingCCW, 2));
        if (blockStateCW.getBlock() == ModBlocks.FURNACE_ENGINE) {
            if (blockStateCW.getValue(VARIANT) == BlockFurnaceEngine.Variant.ENGINE
                    && blockStateCW.getValue(HORIZONTAL_FACING).toVanillaFacing() == facingCW.getOpposite()) {
                hasValidEngine = true;
            }
        } else if (blockStateCCW.getBlock() == ModBlocks.FURNACE_ENGINE) {
            if (blockStateCCW.getValue(VARIANT) == BlockFurnaceEngine.Variant.ENGINE
                    && blockStateCCW.getValue(HORIZONTAL_FACING).toVanillaFacing() == facingCCW.getOpposite()) {
                hasValidEngine = true;
                isRightSided = true;
            }
        }

        return hasValidEngine;
    }

    private boolean shouldPower(IBlockState myState) {
        Block furnaceLit = Blocks.LIT_FURNACE;

        EnumFacing vanillaFacing = myState.getValue(HORIZONTAL_FACING).toVanillaFacing();

        boolean isRightSided = false;
        boolean hasValidEngine = false;
        boolean hasFurnacePowered = false;

        EnumFacing facingCW =       vanillaFacing.rotateY();
        EnumFacing facingCCW =      vanillaFacing.rotateYCCW();
        IBlockState blockStateCW =  world.getBlockState(pos.offset(facingCW, 2));
        IBlockState blockStateCCW = world.getBlockState(pos.offset(facingCCW, 2));
        if (blockStateCW.getBlock() == ModBlocks.FURNACE_ENGINE) {
            if (blockStateCW.getValue(VARIANT) == BlockFurnaceEngine.Variant.ENGINE
                    && blockStateCW.getValue(HORIZONTAL_FACING).toVanillaFacing() == facingCW.getOpposite()) {
                hasValidEngine = true;
            }
        } else if (blockStateCCW.getBlock() == ModBlocks.FURNACE_ENGINE) {
            if (blockStateCCW.getValue(VARIANT) == BlockFurnaceEngine.Variant.ENGINE
                    && blockStateCCW.getValue(HORIZONTAL_FACING).toVanillaFacing() == facingCCW.getOpposite()) {
                hasValidEngine = true;
                isRightSided = true;
            }
        }

        if (hasValidEngine) {
            if (isRightSided) {
                Block check = world.getBlockState(pos.offset(facingCCW, 3)).getBlock();
                if (check == furnaceLit) hasFurnacePowered = true;
            }
            if (!isRightSided) {
                Block check = world.getBlockState(pos.offset(facingCW, 3)).getBlock();
                if (check == furnaceLit) hasFurnacePowered = true;
            }
        } else return false;

        return hasFurnacePowered && !isRightSided;
    }
}
