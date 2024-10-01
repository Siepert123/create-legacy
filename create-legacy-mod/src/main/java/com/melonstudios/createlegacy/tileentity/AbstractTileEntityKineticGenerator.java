package com.melonstudios.createlegacy.tileentity;

import net.minecraft.util.ITickable;

public abstract class AbstractTileEntityKineticGenerator extends AbstractTileEntityKinetic implements ITickable {
    protected abstract void start();

    @Override
    public void update() {
        start();
    }
}
