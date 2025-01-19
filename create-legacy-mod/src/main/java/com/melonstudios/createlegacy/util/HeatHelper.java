package com.melonstudios.createlegacy.util;

import com.melonstudios.createlegacy.block.IHeatProvider;
import com.melonstudios.createlegacy.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class HeatHelper {
    private static final Map<Block, Integer> customHeatings = new HashMap<>();
    static {
        customHeatings.put(Blocks.FIRE, 0);
        customHeatings.put(Blocks.MAGMA, 0);
        customHeatings.put(ModBlocks.BLAZE_BURNER_LIT, 0);
    }
    public static boolean canBlockProvideHeatAt(World world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof IHeatProvider) {
            return true;
        }
        return customHeatings.containsKey(block);
    }
    public static int getHeatLevelAt(World world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof IHeatProvider) {
            ((IHeatProvider)block).getHeatLevel(world, pos);
        }
        if (customHeatings.containsKey(block)) {
            return customHeatings.get(block);
        }
        return -1;
    }
    public static boolean doesHeatingFulfillNeedsAt(World world, BlockPos pos, int requiredHeating) {
        return getHeatLevelAt(world, pos) >= requiredHeating;
    }

    public static boolean isBlockPassivelyHeated(World world, BlockPos pos) {
        return doesHeatingFulfillNeedsAt(world, pos, 0);
    }
    public static boolean isBlockHeated(World world, BlockPos pos) {
        return doesHeatingFulfillNeedsAt(world, pos, 1);
    }
    public static boolean isBlockSuperheated(World world, BlockPos pos) {
        return doesHeatingFulfillNeedsAt(world, pos, 2);
    }
}
