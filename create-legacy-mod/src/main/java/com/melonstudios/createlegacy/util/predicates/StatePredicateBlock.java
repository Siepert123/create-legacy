package com.melonstudios.createlegacy.util.predicates;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class StatePredicateBlock extends StatePredicate {
    private final Block filter;

    public StatePredicateBlock(Block filter) {
        this.filter = filter;
    }

    @Override
    public boolean matches(IBlockState state) {
        return state.getBlock() == filter;
    }
}
