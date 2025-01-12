package com.melonstudios.createlegacy.util.predicates;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class StatePredicateClass extends StatePredicate {
    private final Class<? extends Block> clazz;

    public StatePredicateClass(Class<? extends Block> clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean matches(IBlockState state) {
        return state.getBlock().getClass().isAssignableFrom(clazz);
    }
}
