package com.melonstudios.createlegacy.util.predicates;

import net.minecraft.init.Blocks;

public class StatePredicateFire extends StatePredicateBlock {
    public static final StatePredicateFire instance = new StatePredicateFire();

    private StatePredicateFire() {
        super(Blocks.FIRE);
    }
}
