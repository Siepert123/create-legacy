package com.siepert.createlegacy.util;

import java.util.ArrayList;
import java.util.List;

/**
 * A small class made for having compat easier implemented.
 * @since 1.0.0_pre-8
 */
public class MetalTypes {
    public static final List<String> METAL_NAMES = new ArrayList<String>();

    static {
        METAL_NAMES.add("Aluminum");
        METAL_NAMES.add("Lead");
        METAL_NAMES.add("Nickel");
        METAL_NAMES.add("Osmium");
        METAL_NAMES.add("Platinum");
        METAL_NAMES.add("Quicksilver");
        METAL_NAMES.add("Tin");
        METAL_NAMES.add("Uranium");
        METAL_NAMES.add("Steel");

        METAL_NAMES.add("Iron");
        METAL_NAMES.add("Gold");
        METAL_NAMES.add("Copper");
        METAL_NAMES.add("Zinc");
    }

    public static final String INGOT = "ingot";
    public static final String BLOCK = "block";
    public static final String NUGGET = "nugget";
    public static final String PLATE = "plate";
    public static final String ROD = "rod";
}
