package com.siepert.createapi;

import javafx.fxml.LoadException;

public class AddonLoadException extends LoadException {
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
