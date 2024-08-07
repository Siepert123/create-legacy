package com.siepert.createapi;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * Implementing this interface or extending {@link com.siepert.createapi.CreateAddon } in a class makes it a valid addon class.
 * Registering the addon is done through calling <code>registerAddon(ICreateAddon addon)</code> in {@link com.siepert.createapi.CreateAPI }
 * <code>getCreateVersion()</code> and <code>getKineticVersion()</code>
 * should return the values found in the {@link com.siepert.createlegacy.CreateLegacyModData } of the create version the addon is intended for
 *
 * @author moddingforreal
 * @see com.siepert.createapi.CreateAddon
 * @see com.siepert.createlegacy.CreateLegacyModData
 * */
public interface ICreateAddon {
    int getCreateVersion(); // Refer to com.siepert.createlegacy.CreateLegacyModData for your version of Create Legacy
    int getKineticVersion(); // Refer to com.siepert.createlegacy.CreateLegacyModData for your version of the Kinetic System
    public int getLoadPriority();
    String getModId();
    void onLoad(FMLInitializationEvent initializationEvent);
}
