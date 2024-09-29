package com.melonstudios.createlegacy.schematic;

/**
 * Thrown when a Schematic size exceeds 255 in any direction.
 * @author Siepert123
 */
public class InvalidSchematicSizeException extends Exception {
    public InvalidSchematicSizeException() {
        super("Schematic dimensions exceed the max of 255 blocks!");
    }
    public InvalidSchematicSizeException(String message) {
        super(message);
    }
}
