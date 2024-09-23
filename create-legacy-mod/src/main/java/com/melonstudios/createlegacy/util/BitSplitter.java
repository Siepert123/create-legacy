package com.melonstudios.createlegacy.util;

import java.util.Arrays;
import java.util.Random;

/**
 * Splits the bits of a number into other numbers or boolean arrays.
 * Works by finding each bit individually.
 * please never touch this code ever
 * @author siepert123
 * @since 0.1.0
 */
public final class BitSplitter {
    private static final class InconsistentBitConversionException extends RuntimeException {
        private final int original;
        private final int result;

        public InconsistentBitConversionException(int original, int result) {
            this.original = original;
            this.result = result;
        }

        @Override
        public String getMessage() {
            return String.format("Bit conversion mismatch! Expected %s, got %s instead", original, result);
        }
    }

    public static void runTests(boolean crash) {
        final Random random = new Random();
        byte[] idk = new byte[1];
        random.nextBytes(idk);
        byte test1 = idk[0];
        int test2 = random.nextInt();
        int test3 = random.nextInt();

        boolean error = false;

        DisplayLink.info("Starting BitSplitter tests");
        if (crash) DisplayLink.warn("Warning: inconsistent tests crash the game!!");

        DisplayLink.info("Test 1: origin = " + test1);
        DisplayLink.info("Test 1: bit conversion = " + Arrays.toString(byteToBooleans(test1)));
        DisplayLink.info("Test 1: result = " + booleansToByte(byteToBooleans(test1)));
        if (booleansToByte(byteToBooleans(test1)) != test1) {
            error = true;
            if (crash)
                throw new InconsistentBitConversionException(test1, booleansToByte(byteToBooleans(test1)));
        }

        DisplayLink.info("Test 2: origin = " + test2);
        DisplayLink.info("Test 2: byte conversion = " + Arrays.toString(intToBytes(test2)));
        DisplayLink.info("Test 2: result = " + bytesToInt(intToBytes(test2)));
        if (bytesToInt(intToBytes(test2)) != test2) {
            error = true;
            if (crash)
                throw new InconsistentBitConversionException(test2, bytesToInt(intToBytes(test2)));
        }

        DisplayLink.info("Test 3: origin = " + test3);
        DisplayLink.info("Test 3: byte conversion = " + Arrays.toString(intToBytes(test3)));
        DisplayLink.info("Test 3: result = " + bytesToInt(intToBytes(test3)));
        if (bytesToInt(intToBytes(test3)) != test3) {
            error = true;
            if (crash)
                throw new InconsistentBitConversionException(test3, bytesToInt(intToBytes(test3)));
        }

        if (error) {
            DisplayLink.fatal("Bit conversions are inconsistent, proceed with extreme caution!");
        } else {
            DisplayLink.info("BitSplitter tests completed without error!");
        }
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