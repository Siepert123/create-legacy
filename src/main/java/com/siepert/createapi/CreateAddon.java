package com.siepert.createapi;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public abstract class CreateAddon {
    private final int loadPriority;
    private final String modId;
    private final int madeForCreateVersion;
    private final int madeForKineticVersion;

    public int getCreateVersion() {
        return madeForCreateVersion;
    }
    public int getKineticVersion() {
        return madeForKineticVersion;
    }

    public CreateAddon(String modId, int c, int k) {
        this.modId = modId;
        this.loadPriority = 0;
        madeForCreateVersion = c;
        madeForKineticVersion = k;
    }
    public CreateAddon(String modId, int c, int k, int loadPriority) {
        this.modId = modId;
        this.loadPriority = loadPriority;
        madeForCreateVersion = c;
        madeForKineticVersion = k;
    }

    public int getLoadPriority() {
        return loadPriority;
    }
    public String getModId() {
        return modId;
    }

    public abstract void onLoad(FMLInitializationEvent initializationEvent);
}
