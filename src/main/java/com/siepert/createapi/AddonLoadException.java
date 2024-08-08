package com.siepert.createapi;

/**
 * AddonLoadException is thrown whenever an addon cannot be loaded
 *
 * @author Siepert123
 * */
public class AddonLoadException extends RuntimeException {
    public AddonLoadException(String message) {
        super(message);
    }
    /**
     * @throws AddonLoadException Throws a new AddonLoadException reporting the kinetic version mismatch;
     * addon modid, addon kinetic version and create kinetic version are also reported.
     * */

    public static void kineticVersionMismatch(String modId, int wrongVersion) throws AddonLoadException {
        throw new AddonLoadException("Kinetic version mismatch: Addon "
                + modId + " is made for kinetic version "
                + wrongVersion + ", current version is "
                + CreateAPI.getKineticVersion() + "!");
    }
}
