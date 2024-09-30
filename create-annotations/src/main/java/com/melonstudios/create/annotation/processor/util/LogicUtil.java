package com.melonstudios.create.annotation.processor.util;

import java.util.List;

public class LogicUtil {
    public static boolean and(Boolean... booleans) {
        if (booleans.length < 1) {
            return false;
        }
        if (booleans.length == 1) {
            return booleans[0];
        }
        boolean ret = true; // will be set to false upon encountering first false
        for (boolean boo:
             booleans) {
            ret = ret && boo;
        }
        return ret;
    }
    public static boolean and(List<Boolean> list) {
        Boolean[] booleans = new Boolean[0];
        return and(list.toArray(booleans));
    }
}
