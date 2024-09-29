package com.melonstudios.createlegacy.schematic;

public class InvalidSchematicSizeException extends Exception {
    public InvalidSchematicSizeException() {
        super("Schematic dimensions exceed the max of 255 blocks!");
    }
    public InvalidSchematicSizeException(String message) {
        super(message);
    }
}
