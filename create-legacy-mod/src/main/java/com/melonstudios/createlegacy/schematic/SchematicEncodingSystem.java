package com.melonstudios.createlegacy.schematic;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

import static com.melonstudios.createlegacy.util.BitSplitter.intToBytes;

/**
 * Encodes and decodes Schematics; converting {@link IBlockState}s to {@code byte}s and vice versa!
 * <p>
 * I really hope this does not cause memory issues
 * @author Siepert123
 * @see SchematicSaveHelper
 * @since 0.1.0
 */
public final class SchematicEncodingSystem {
    public static byte[] encode(IBlockState[][][] structure) throws InvalidSchematicSizeException {
        final int x = structure.length;
        final int y = structure[0].length;
        final int z = structure[0][0].length;
        if (x > 255 || y > 255 || z > 255) throw new InvalidSchematicSizeException();

        final int totalSize = x*y*z*5;

        //Create the byte array
        byte[] schematic = new byte[totalSize + 3]; // + 3 for the size data

        //Add the dimensions data
        schematic[0] = intToBytes(x)[0];
        schematic[1] = intToBytes(y)[0];
        schematic[2] = intToBytes(z)[0];

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                for (int k = 0; k < z; k++) {
                    System.arraycopy(encodeBlockstate(structure[i][j][k]), 0,
                            schematic,getArrayPos(i, j, k, x, y, z) + 3, 5);
                }
            }
        }
        return schematic;
    }
    private static int getArrayPos(int x, int y, int z, int sizeX, int sizeY, int sizeZ) {
        return x * sizeX^2 + y * sizeY + z;
    }
    private static byte[] encodeBlockstate(IBlockState state) {
        byte[] part1 = intToBytes(Block.getIdFromBlock(state.getBlock()));
        byte part2 = (byte) state.getBlock().getMetaFromState(state);
        return new byte[]{
                part1[0], part1[1], part1[2], part1[3], part2
        };
    }
}
