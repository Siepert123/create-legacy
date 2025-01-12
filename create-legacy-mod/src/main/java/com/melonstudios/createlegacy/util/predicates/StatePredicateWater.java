package com.melonstudios.createlegacy.util.predicates;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class StatePredicateWater extends StatePredicate {
    public static final StatePredicateWater instance = new StatePredicateWater();
    private StatePredicateWater() {}
    @Override
    public boolean matches(IBlockState state) {
        return state.getBlock() == Blocks.WATER || state.getBlock() == Blocks.FLOWING_WATER;
    }
}
