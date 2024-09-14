package com.melonstudios.createlegacy.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DisplayLink {
    private static Logger logger;

    private static void initialize() {
        logger = LogManager.getLogger("Create Legacy");
    }

    public static void log(Level level, String msg, Object... format) {
        if (logger == null) {
            initialize();
        }

        try {
            msg = String.format(msg, format);
        } catch (Exception ignored) {}

        logger.log(level, msg);
    }

    public static void debug(String msg, Object... format) {
        log(Level.DEBUG, msg, format);
    }
    public static void info(String msg, Object... format) {
        log(Level.INFO, msg, format);
    }
    public static void warn(String msg, Object... format) {
        log(Level.WARN, msg, format);
    }
    public static void error(String msg, Object... format) {
        log(Level.ERROR, msg, format);
    }
    public static void fatal(String msg, Object... format) {
        log(Level.FATAL, msg, format);
    }
}
