package com.siepert.createlegacy.tileentity;

import com.siepert.createapi.network.IKineticTE;
import com.siepert.createapi.network.KineticBlockInstance;
import com.siepert.createapi.network.NetworkContext;
import com.siepert.createlegacy.CreateLegacyConfigHolder;
import com.siepert.createlegacy.blocks.kinetic.BlockFurnaceEngine;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.CreateLegacyModData;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

import static com.siepert.createlegacy.blocks.kinetic.BlockFurnaceEngine.*;

public class TileEntityFurnaceFlywheel extends TileEntity implements ITickable, IKineticTE {
    private static final int SMOKE_MIN = 5;
    private static final int SMOKE_MAX = 15;
    private static final int SMOKE_DIFF = SMOKE_MAX - SMOKE_MIN;

    boolean updated = false;

    @Override
    public void update() {
        IBlockState state = world.getBlockState(pos);

        if (shouldPower(state) && !updated) {
            NetworkContext context = new NetworkContext();

            context.addKineticBlockInstance(new KineticBlockInstance(pos, false));

            TileEntity entity = world.getTileEntity(pos.offset(state.getValue(HORIZONTAL_FACING).toVanillaFacing().getOpposite()));

            if (entity instanceof IKineticTE) {
                ((IKineticTE) entity).passNetwork(context, state.getValue(HORIZONTAL_FACING).toVanillaFacing(),
                        false, false, false);
            }

            context.runThroughPhases(world);
        }

        updated = false;

        if (shouldIUpdate(hasShaft(state), state)) setState(hasShaft(state), world, pos);
    }

    /**
     * @param shaft Whether the shaft state should be true or false.
     * @param myState The current blockstate.
     * @return True if the new blockstate differs from the current blockstate.
     */
    private boolean shouldIUpdate(boolean shaft, IBlockState myState) {
        return myState.getValue(HAS_SHAFT) != shaft;
    }

    /**
     * @param myState The current blockstate.
     * @return True if the circumstances are correct for having those shaft thingies.
     */
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

        return hasValidEngine && !isRightSided;
    }

    /**
     * @param myState The current blockstate.
     * @return True if the circumstances allow for power generation.
     */
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

    @Override
    public double getStressImpact() {
        return 0;
    }

    @Override
    public double getStressCapacity() {
        return CreateLegacyConfigHolder.kineticConfig.furnaceEngineStressCapacity;
    }

    @Override
    public int getProducedSpeed() {
        return shouldPower(world.getBlockState(pos)) ? 64 : 0;
    }

    @Override
    public boolean isGenerator() {
        return true;
    }

    @Override
    public void kineticTick(NetworkContext context) {
        IBlockState state = world.getBlockState(pos);

        if (shouldPower(state)) {
            if (world.getTotalWorldTime() % 10 == 0) {
                BlockPos enginePos = pos.offset(state.getValue(HORIZONTAL_FACING).toVanillaFacing().rotateY(), 2);

                for (int i = 0; i < SMOKE_MIN + CreateLegacyModData.timedLucky.nextInt(SMOKE_DIFF); i++) {
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                            enginePos.getX() + CreateLegacyModData.random.nextFloat(),
                            enginePos.getY() + CreateLegacyModData.random.nextFloat(),
                            enginePos.getZ() + CreateLegacyModData.random.nextFloat(),
                            0, 0, 0);
                }
            }
            if (world.getTotalWorldTime() % 70 == 0) {
                world.playSound(null,
                        pos.getX() + 0.5,
                        pos.getY() + 0.5,
                        pos.getZ() + 0.5,
                        SoundEvents.BLOCK_FIRE_EXTINGUISH,
                        SoundCategory.BLOCKS,
                        0.5f, 0.8f);
            }
        }
    }

    @Override
    public void setUpdated() {
        updated = true;
    }

    @Override
    public void passNetwork(NetworkContext context, EnumFacing source, boolean srcIsCog, boolean srcCogIsHorizontal, boolean inverted) {
        if (srcIsCog) return;

        if (world.getBlockState(pos).getValue(HORIZONTAL_FACING).toVanillaFacing().getOpposite() == source)
            context.addKineticBlockInstance(new KineticBlockInstance(pos, inverted));
    }
}
