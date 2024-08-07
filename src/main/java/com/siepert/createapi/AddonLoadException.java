package com.siepert.createapi;

/**
 * AddonLoadException is thrown whenever an addon cannot be loaded
 *
 * @author Siepert123
 * */
public class AddonLoadException extends RuntimeException {
    /**
     * @param message The detail message
     * */
    public AddonLoadException(String message) {
        super(message);
    }

    public static void kineticVersionMismatch(String modId, int wrongVersion) throws AddonLoadException {
        throw new AddonLoadException("Kinetic version mismatch: Addon "
                + modId + " is made for kinetic version "
                + wrongVersion + ", current version is "
                + CreateAPI.getKineticVersion() + "!");
    }
}
