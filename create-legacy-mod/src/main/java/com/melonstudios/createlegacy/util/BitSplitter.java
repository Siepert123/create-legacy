package com.melonstudios.createlegacy.util;

import java.util.Arrays;

/**
 * Splits the bits of a number into other numbers or boolean arrays.
 * Works by finding each bit individually.
 * please never touch this code ever
 * @author siepert123
 * @since 0.1.0
 */
public final class BitSplitter {
    public static void main(String[] args) {
        System.out.println("Test START");

        byte test1 = -37;

        System.out.println("test 1 - origin: " + test1);
        System.out.println("test 1 - boolean array: " + Arrays.toString(byteToBooleans(test1)));
        System.out.println("test 1 - reconversion: " + booleansToByte(byteToBooleans(test1)));

        int test2 = 12589923;

        System.out.println("test 2 - origin: "+ test2);
        System.out.println("test 2 - boolean array: " + Arrays.toString(intToBooleans(test2)));
        System.out.println("test 2 - reconversion: " + booleansToInt(intToBooleans(test2)));

        int test3 = -27895252;

        System.out.println("test 3 - origin: "+ test3);
        System.out.println("test 3 - byte array: " + Arrays.toString(intToBytes(test3)));
        System.out.println("test 3 - reconversion: " + bytesToInt(intToBytes(test3)));

        System.out.println("Test END");
    }

    private static boolean[] byteSect(boolean[] booleans, final int place) {
        boolean[] newBooleans = new boolean[8];

        System.arraycopy(booleans, place, newBooleans, 0, 8);

        return newBooleans;
    }

    public static boolean[] byteToBooleans(byte b) {
        boolean[] booleans = new boolean[8];

        booleans[0] = (b & 0b00000001) == 0b00000001;
        booleans[1] = (b & 0b00000010) == 0b00000010;
        booleans[2] = (b & 0b00000100) == 0b00000100;
        booleans[3] = (b & 0b00001000) == 0b00001000;
        booleans[4] = (b & 0b00010000) == 0b00010000;
        booleans[5] = (b & 0b00100000) == 0b00100000;
        booleans[6] = (b & 0b01000000) == 0b01000000;
        booleans[7] = (b & 0b10000000) == 0b10000000;

        return booleans;
    }

    public static byte booleansToByte(boolean[] booleans) {
        if (booleans.length != 8) throw new IllegalArgumentException(String.format("A byte needs 8 booleans, not %s!", booleans.length));

        int i = (booleans[0] ? 0b00000001 : 0b00000000) |
                        (booleans[1] ? 0b00000010 : 0b00000000) |
                        (booleans[2] ? 0b00000100 : 0b00000000) |
                        (booleans[3] ? 0b00001000 : 0b00000000) |
                        (booleans[4] ? 0b00010000 : 0b00000000) |
                        (booleans[5] ? 0b00100000 : 0b00000000) |
                        (booleans[6] ? 0b01000000 : 0b00000000) |
                        (booleans[7] ? 0b10000000 : 0b00000000);

        return (byte) i;
    }

    public static boolean[] intToBooleans(int i) {
        boolean[] booleans = new boolean[32];

        booleans[0] = (i & 0b00000000000000000000000000000001) == 0b00000000000000000000000000000001;
        booleans[1] = (i & 0b00000000000000000000000000000010) == 0b00000000000000000000000000000010;
        booleans[2] = (i & 0b00000000000000000000000000000100) == 0b00000000000000000000000000000100;
        booleans[3] = (i & 0b00000000000000000000000000001000) == 0b00000000000000000000000000001000;
        booleans[4] = (i & 0b00000000000000000000000000010000) == 0b00000000000000000000000000010000;
        booleans[5] = (i & 0b00000000000000000000000000100000) == 0b00000000000000000000000000100000;
        booleans[6] = (i & 0b00000000000000000000000001000000) == 0b00000000000000000000000001000000;
        booleans[7] = (i & 0b00000000000000000000000010000000) == 0b00000000000000000000000010000000;
        booleans[8] = (i & 0b00000000000000000000000100000000) == 0b00000000000000000000000100000000;
        booleans[9] = (i & 0b00000000000000000000001000000000) == 0b00000000000000000000001000000000;
        booleans[10] = (i & 0b00000000000000000000010000000000) == 0b00000000000000000000010000000000;
        booleans[11] = (i & 0b00000000000000000000100000000000) == 0b00000000000000000000100000000000;
        booleans[12] = (i & 0b00000000000000000001000000000000) == 0b00000000000000000001000000000000;
        booleans[13] = (i & 0b00000000000000000010000000000000) == 0b00000000000000000010000000000000;
        booleans[14] = (i & 0b00000000000000000100000000000000) == 0b00000000000000000100000000000000;
        booleans[15] = (i & 0b00000000000000001000000000000000) == 0b00000000000000001000000000000000;
        booleans[16] = (i & 0b00000000000000010000000000000000) == 0b00000000000000010000000000000000;
        booleans[17] = (i & 0b00000000000000100000000000000000) == 0b00000000000000100000000000000000;
        booleans[18] = (i & 0b00000000000001000000000000000000) == 0b00000000000001000000000000000000;
        booleans[19] = (i & 0b00000000000010000000000000000000) == 0b00000000000010000000000000000000;
        booleans[20] = (i & 0b00000000000100000000000000000000) == 0b00000000000100000000000000000000;
        booleans[21] = (i & 0b00000000001000000000000000000000) == 0b00000000001000000000000000000000;
        booleans[22] = (i & 0b00000000010000000000000000000000) == 0b00000000010000000000000000000000;
        booleans[23] = (i & 0b00000000100000000000000000000000) == 0b00000000100000000000000000000000;
        booleans[24] = (i & 0b00000001000000000000000000000000) == 0b00000001000000000000000000000000;
        booleans[25] = (i & 0b00000010000000000000000000000000) == 0b00000010000000000000000000000000;
        booleans[26] = (i & 0b00000100000000000000000000000000) == 0b00000100000000000000000000000000;
        booleans[27] = (i & 0b00001000000000000000000000000000) == 0b00001000000000000000000000000000;
        booleans[28] = (i & 0b00010000000000000000000000000000) == 0b00010000000000000000000000000000;
        booleans[29] = (i & 0b00100000000000000000000000000000) == 0b00100000000000000000000000000000;
        booleans[30] = (i & 0b01000000000000000000000000000000) == 0b01000000000000000000000000000000;
        booleans[31] = (i & 0b10000000000000000000000000000000) == 0b10000000000000000000000000000000;

        return booleans;
    }

    public static int booleansToInt(boolean[] booleans) {
        if (booleans.length != 32) throw new IllegalArgumentException(String.format("An int needs 32 booleans, not %s!", booleans.length));

        int i = 0;

        i |= booleans[0] ? 0b00000000000000000000000000000001 : 0;
        i |= booleans[1] ? 0b00000000000000000000000000000010 : 0;
        i |= booleans[2] ? 0b00000000000000000000000000000100 : 0;
        i |= booleans[3] ? 0b00000000000000000000000000001000 : 0;
        i |= booleans[4] ? 0b00000000000000000000000000010000 : 0;
        i |= booleans[5] ? 0b00000000000000000000000000100000 : 0;
        i |= booleans[6] ? 0b00000000000000000000000001000000 : 0;
        i |= booleans[7] ? 0b00000000000000000000000010000000 : 0;
        i |= booleans[8] ? 0b00000000000000000000000100000000 : 0;
        i |= booleans[9] ? 0b00000000000000000000001000000000 : 0;
        i |= booleans[10] ? 0b00000000000000000000010000000000 : 0;
        i |= booleans[11] ? 0b00000000000000000000100000000000 : 0;
        i |= booleans[12] ? 0b00000000000000000001000000000000 : 0;
        i |= booleans[13] ? 0b00000000000000000010000000000000 : 0;
        i |= booleans[14] ? 0b00000000000000000100000000000000 : 0;
        i |= booleans[15] ? 0b00000000000000001000000000000000 : 0;
        i |= booleans[16] ? 0b00000000000000010000000000000000 : 0;
        i |= booleans[17] ? 0b00000000000000100000000000000000 : 0;
        i |= booleans[18] ? 0b00000000000001000000000000000000 : 0;
        i |= booleans[19] ? 0b00000000000010000000000000000000 : 0;
        i |= booleans[20] ? 0b00000000000100000000000000000000 : 0;
        i |= booleans[21] ? 0b00000000001000000000000000000000 : 0;
        i |= booleans[22] ? 0b00000000010000000000000000000000 : 0;
        i |= booleans[23] ? 0b00000000100000000000000000000000 : 0;
        i |= booleans[24] ? 0b00000001000000000000000000000000 : 0;
        i |= booleans[25] ? 0b00000010000000000000000000000000 : 0;
        i |= booleans[26] ? 0b00000100000000000000000000000000 : 0;
        i |= booleans[27] ? 0b00001000000000000000000000000000 : 0;
        i |= booleans[28] ? 0b00010000000000000000000000000000 : 0;
        i |= booleans[29] ? 0b00100000000000000000000000000000 : 0;
        i |= booleans[30] ? 0b01000000000000000000000000000000 : 0;
        i |= booleans[31] ? 0b10000000000000000000000000000000 : 0;

        return i;
    }

    public static byte[] intToBytes(int i) {
        boolean[] booleans = intToBooleans(i);

        return new byte[]{
                booleansToByte(byteSect(booleans, 0)),
                booleansToByte(byteSect(booleans, 8)),
                booleansToByte(byteSect(booleans, 16)),
                booleansToByte(byteSect(booleans, 24))
        };
    }

    public static int bytesToInt(byte[] bytes) {
        if (bytes.length != 4) throw new IllegalArgumentException(String.format("An int needs 4 bytes, not %s!", bytes.length));
        boolean[] booleans = new boolean[32];

        System.arraycopy(byteToBooleans(bytes[0]), 0, booleans, 0, 8);
        System.arraycopy(byteToBooleans(bytes[1]), 0, booleans, 8, 8);
        System.arraycopy(byteToBooleans(bytes[2]), 0, booleans, 16, 8);
        System.arraycopy(byteToBooleans(bytes[3]), 0, booleans, 24, 8);

        return booleansToInt(booleans);
    }
}