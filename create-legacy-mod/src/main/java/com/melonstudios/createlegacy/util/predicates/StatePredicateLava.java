package com.melonstudios.createlegacy.util.predicates;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class StatePredicateLava extends StatePredicate {
    public static final StatePredicateLava instance = new StatePredicateLava();
    private StatePredicateLava() {}
    @Override
    public boolean matches(IBlockState state) {
        return state.getBlock() == Blocks.LAVA || state.getBlock() == Blocks.FLOWING_LAVA;
    }
}
