package com.siepert.createlegacy.util.compat;

import java.util.ArrayList;
import java.util.List;

/**
 * A small class made for having compat easier implemented.
 * @since 1.0.0_pre-8
 */
public class MetalTypes {
    public static final List<String> METAL_NAMES = new ArrayList<String>();

    static {
        //Common metals
        METAL_NAMES.add("Aluminum");
        METAL_NAMES.add("Lead");
        METAL_NAMES.add("Nickel");
        METAL_NAMES.add("Osmium");
        METAL_NAMES.add("Platinum");
        METAL_NAMES.add("Silver");
        METAL_NAMES.add("Quicksilver");
        METAL_NAMES.add("Tin");
        METAL_NAMES.add("Uranium");
        METAL_NAMES.add("Steel");

        //Base metals
        METAL_NAMES.add("Iron");
        METAL_NAMES.add("Gold");
        METAL_NAMES.add("Copper");
        METAL_NAMES.add("Zinc");
        METAL_NAMES.add("Brass");

        //Things for HBM specifically lmao
        METAL_NAMES.add("Titanium");
        METAL_NAMES.add("Mingrade");
        METAL_NAMES.add("AdvancedAlloy");
        METAL_NAMES.add("Tungsten");
        METAL_NAMES.add("Beryllium");
        METAL_NAMES.add("Schrabidium");
        METAL_NAMES.add("Saturnite");

        //TC metals
        METAL_NAMES.add("Cobalt");
        METAL_NAMES.add("Pigiron");
        METAL_NAMES.add("Alubrass");
        METAL_NAMES.add("Manyullyn");
        METAL_NAMES.add("Knightslime");
        METAL_NAMES.add("Ardite");

        //Odd IE metals
        METAL_NAMES.add("Constantan");
        METAL_NAMES.add("Electrum");
    }

    public static final String INGOT = "ingot";
    public static final String BLOCK = "block";
    public static final String NUGGET = "nugget";
    public static final String PLATE = "plate";
    public static final String ROD = "rod";
    public static final String ORE = "ore";
    public static final String CRUSHED = "crushed";
}
