package com.melonstudios.createapi;

import com.melonstudios.createapi.addons.CreateAddon;
import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.util.DisplayLink;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Set;

@SuppressWarnings("deprecation, unchecked")
public final class CreateAPI {
    private static final List<Class<? extends CreateAddon>> ADDONS = new ArrayList<>(); //I see no problem

    public static void discoverAddons(FMLPreInitializationEvent event) {
        Set<ASMDataTable.ASMData> classes = event.getAsmData().getAll("com.melonstudios.createapi.addons.CreateAddon");
        for (ASMDataTable.ASMData data : classes) {
            try {
                Class<? extends CreateAddon> clazz = (Class<? extends CreateAddon>) Class.forName(data.getClassName());
                ADDONS.add(clazz);
                DisplayLink.debug("Found create addon: " + clazz.getCanonicalName());

                queryAddonData(clazz);
            } catch (Exception e) {
                DisplayLink.error("Something went wrong during addon loading!");
                e.printStackTrace();
            }
        }
    }
    private static void queryAddonData(Class<?> clazz) {
        DisplayLink.debug("Querying data from class %s", clazz.getCanonicalName());
        if (clazz.isAnnotationPresent(CreateAddon.class)) {
            CreateAddon annotation = clazz.getAnnotation(CreateAddon.class);

            DisplayLink.debug("Mod ID: %s", annotation.modid());
            DisplayLink.debug("Create version: %s", annotation.createVersion());
            DisplayLink.debug("Kinetic version: %s", annotation.kineticVersion());
            DisplayLink.debug("Load priority: %s", annotation.loadPriority());
        }
    }

    private static final List<Class<? extends CreateAddon>> ADDONS_BY_PRIORITY = new ArrayList<>();
    public static List<Class<? extends CreateAddon>> getAddons() {
        return ADDONS_BY_PRIORITY;
    }

    public static void sortAddons() {
        int lowestInt = 0;
        int highestInt = 0;
        for (Class<? extends CreateAddon> clazz : ADDONS) {
            CreateAddon annotation = clazz.getAnnotation(CreateAddon.class);

            if (annotation.loadPriority() < lowestInt) lowestInt = annotation.loadPriority();
            if (annotation.loadPriority() > highestInt) highestInt = annotation.loadPriority();

        }

        for (int i = lowestInt; i <= highestInt; i++) {
            for (Class<? extends CreateAddon> clazz : ADDONS) {
                CreateAddon annotation = clazz.getAnnotation(CreateAddon.class);

                if (annotation.loadPriority() == i) ADDONS_BY_PRIORITY.add(clazz);
            }
        }
    }

    public static int getVersion() {
        return CreateLegacy.VERSION_NUM;
    }
    public static int getKineticVersion() {
        return CreateLegacy.KINETIC_VERSION_NUM;
    }

    public static String translateToLocal(String key) {
        if (I18n.canTranslate(key)) return I18n.translateToLocal(key);
        else return I18n.translateToFallback(key);
    }

    public static String translateToLocalFormatted(String key, Object... format) {
        String s = translateToLocal(key);
        try {
            return String.format(s, format);
        } catch (IllegalFormatException e) {
            return "Format Error: " + s;
        }
    }

    public static int longToIntSafe(long j) {
        if (j > Integer.MAX_VALUE) return Integer.MAX_VALUE;
        return (int) j;
    }


    public static String stressImpactTooltip(double impact) {
        return CreateAPI.translateToLocalFormatted("tooltip.stressImpact", impact);
    }

    public static String stressCapacityTooltip(double capacity) {
        return CreateAPI.translateToLocalFormatted("tooltip.stressCapacity", capacity);
    }

    public static boolean checkThreads(Thread... threads) { //You can never thread too much!
        for (Thread thread : threads) {
            if (thread.isAlive()) return false;
        }
        return true;
    }
}
