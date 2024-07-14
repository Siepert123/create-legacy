package com.siepert.createapi;

import com.siepert.createlegacy.util.Reference;

public class CreateAPI {
    public static boolean compareCreateVersions(int versionOther) {
        return versionOther == getVersion();
    }

    public static int getVersion() {
        return Reference.VERSION_NUMBER;
    }

    public static int getKineticVersion() {
        return Reference.KINETIC_VERSION;
    }
}
