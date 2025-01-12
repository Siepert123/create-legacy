package com.melonstudios.createlegacy.util.predicates;

import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;

public class StatePredicateFence extends StatePredicate {
    public static final StatePredicateFence instance = new StatePredicateFence();

    private StatePredicateFence() {}

    @Override
    public boolean matches(IBlockState state) {
        return state.getBlock() instanceof BlockFence;
    }
}
