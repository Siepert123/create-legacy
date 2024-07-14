package com.siepert.createapi;

import com.siepert.createlegacy.util.Reference;

public class CreateAPI {
    public static boolean compareCreateVersions(String versionIn) {
        String[] tokens = versionIn.split("w");
        String year = tokens[0];
        String week = tokens[1].substring(0, 2);
        String[] tokens1 = Reference.VERSION.split("w");
        String year1 = tokens1[0];
        String week1 = tokens1[1].substring(0, 2);

        if (Integer.parseInt(year1) < Integer.parseInt(year)) return false;
        return Integer.parseInt(week1) >= Integer.parseInt(week);
    }

    public static String getVersion() {
        return Reference.VERSION;
    }

    public static int getKineticVersion() {
        return 2;
    }
}
