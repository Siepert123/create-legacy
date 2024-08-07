package com.siepert.createapi;

/* Example implementation of ICreateAddon */
public abstract class CreateAddon implements ICreateAddon {
    private final int loadPriority;
    private final String modId;
    private final int madeForCreateVersion;
    private final int madeForKineticVersion;

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

    @Override
    public int getCreateVersion() {
        return madeForCreateVersion;
    }
    @Override
    public int getKineticVersion() {
        return madeForKineticVersion;
    }
    @Override
    public int getLoadPriority() {
        return loadPriority;
    }
    @Override
    public String getModId() {
        return modId;
    }
}
