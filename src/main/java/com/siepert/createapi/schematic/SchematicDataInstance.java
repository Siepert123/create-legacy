package com.siepert.createapi.schematic;

public class SchematicDataInstance {
    private final byte size_x;
    private final byte size_y;
    private final byte size_z;

    public SchematicDataInstance(byte size_x, byte size_z, byte size_y) {
        this.size_x = size_x;
        this.size_z = size_z;
        this.size_y = size_y;
    }

    public int getSizeX() {
        return size_x;
    }

    public int getSizeY() {
        return size_y;
    }

    public int getSizeZ() {
        return size_z;
    }
}
