package com.siepert.createapi;

public class AddonLoadException extends RuntimeException {
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
