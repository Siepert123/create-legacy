package com.siepert.createapi.addons;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import javax.annotation.Nullable;

/**
 * Implementing this interface or extending {@link CreateAddon } in a class makes it a valid addon class.
 * Registering the addon is done through calling <code>registerAddon(ICreateAddon addon)</code> in {@link com.siepert.createapi.CreateAPI }
 * <code>getCreateVersion()</code> and <code>getKineticVersion()</code>
 * should return the values found in the {@link com.siepert.createlegacy.CreateLegacyModData } of the create version the addon is intended for<br>
 *
 * <strong>Each implementing class should have only one instance!</strong>
 *
 * @author moddingforreal
 * @see CreateAddon
 * @see com.siepert.createlegacy.CreateLegacyModData
 * */
public interface ICreateAddon {
    /** @deprecated  Use {@link com.siepert.createapi.addons.annotation.CreateAddon @CreateAddon}! */
    @Deprecated
    int getCreateVersion(); // Refer to com.siepert.createlegacy.CreateLegacyModData for your version of Create Legacy
    /** @deprecated  Use {@link com.siepert.createapi.addons.annotation.CreateAddon @CreateAddon}! */
    @Deprecated
    int getKineticVersion(); // Refer to com.siepert.createlegacy.CreateLegacyModData for your version of the Kinetic System
    /** @deprecated  Use {@link com.siepert.createapi.addons.annotation.CreateAddon @CreateAddon}! */
    @Deprecated
    int getLoadPriority();
    /** @deprecated  Use {@link com.siepert.createapi.addons.annotation.CreateAddon @CreateAddon}! */
    @Deprecated
    String getModId();

    /**
     * Code to be performed on loading this addon
     * @param initializationEvent The FMLInitializationEvent associated with the modloading phase the addon is loaded in
     * */
    void onLoad(FMLInitializationEvent initializationEvent);
}
