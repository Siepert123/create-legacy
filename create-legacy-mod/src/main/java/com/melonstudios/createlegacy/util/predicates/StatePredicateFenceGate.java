package com.melonstudios.createlegacy.util.predicates;

import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.state.IBlockState;

public class StatePredicateFenceGate extends StatePredicate {
    public static final StatePredicateFenceGate instance = new StatePredicateFenceGate();

    private StatePredicateFenceGate() {}

    @Override
    public boolean matches(IBlockState state) {
        return state.getBlock() instanceof BlockFenceGate;
    }
}
