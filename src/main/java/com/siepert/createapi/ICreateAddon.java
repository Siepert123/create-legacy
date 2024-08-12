package com.siepert.createapi;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public interface ICreateAddon {
    int getCreateVersion(); // Refer to com.siepert.createlegacy.CreateLegacyModData for your version of Create Legacy
    int getKineticVersion(); // Refer to com.siepert.createlegacy.CreateLegacyModData for your version of the Kinetic System
    public int getLoadPriority();
    String getModId();
    void onLoad(FMLInitializationEvent initializationEvent);
}
