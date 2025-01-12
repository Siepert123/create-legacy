package com.melonstudios.createlegacy.util.predicates;

import com.melonstudios.createlegacy.block.BlockBlazeBurner;
import com.melonstudios.createlegacy.block.BlockBlazeBurnerDeco;
import net.minecraft.block.state.IBlockState;

public class StatePredicateBlazeBurnerAny extends StatePredicate {
    public static final StatePredicateBlazeBurnerAny instance = new StatePredicateBlazeBurnerAny();

    private StatePredicateBlazeBurnerAny() {}

    @Override
    public boolean matches(IBlockState state) {
        return state.getBlock() instanceof BlockBlazeBurner || state.getBlock() instanceof BlockBlazeBurnerDeco;
    }
}
