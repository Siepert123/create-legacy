package com.melonstudios.createlegacy.util.predicates;

import com.melonstudios.createlegacy.util.MetaBlock;
import net.minecraft.block.state.IBlockState;

public class StatePredicateMetaBlock extends StatePredicate {
    private final MetaBlock filter;

    public StatePredicateMetaBlock(MetaBlock filter) {
        this.filter = filter;
    }

    @Override
    public boolean matches(IBlockState state) {
        return new MetaBlock(state).equals(filter);
    }
}
