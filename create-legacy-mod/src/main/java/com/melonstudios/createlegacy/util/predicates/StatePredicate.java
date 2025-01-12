package com.melonstudios.createlegacy.util.predicates;

import net.minecraft.block.state.IBlockState;

public abstract class StatePredicate {
    public abstract boolean matches(IBlockState state);

    public static boolean matchInList(Iterable<StatePredicate> predicates, IBlockState state) {
        for (StatePredicate predicate : predicates) {
            if (predicate.matches(state)) return true;
        }
        return false;
    }
}
