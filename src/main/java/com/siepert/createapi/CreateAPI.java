package com.siepert.createapi;

import com.siepert.createlegacy.CreateLegacyModData;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.IllegalFormatException;
import java.util.List;

public final class CreateAPI {


    private static final List<CreateAddon> ADDONS = new ArrayList<>();

    /**
     * Registers a Create addon.
     * @param addon Your addon.
     */
    public static void registerAddon(CreateAddon addon) {
        if (!isModAlreadyRegistered(addon.getModId())) {
            ADDONS.add(addon);
        }
    }

    private static boolean isModAlreadyRegistered(@Nonnull String id) {
        for (CreateAddon addon : ADDONS) {
            if (addon.getModId().equals(id)) return true;
        }
        return false;
    }

    private static final List<CreateAddon> ADDONS_IN_PRIORITY = new ArrayList<>();
    public static List<CreateAddon> getAddons() {
        return ADDONS_IN_PRIORITY;
    }


    public static void consumeAddons() {
        int lowestInt = 0;
        int highestInt = 0;
        for (CreateAddon addon : ADDONS) {
            if (addon.getLoadPriority() < lowestInt) lowestInt = addon.getLoadPriority();
            if (addon.getLoadPriority() > highestInt) highestInt = addon.getLoadPriority();
        }

        for (int i = lowestInt; i <= highestInt; i++) {
            for (CreateAddon addon : ADDONS) {
                if (addon.getLoadPriority() == i) ADDONS_IN_PRIORITY.add(addon);
            }
        }
    }


    public static boolean compareCreateVersions(int versionOther) {
        return versionOther == getVersion();
    }

    public static int getVersion() {
        return CreateLegacyModData.VERSION_NUMBER;
    }

    public static boolean compareKineticVersions(int versionOther) {
        return versionOther == getKineticVersion();
    }

    public static int getKineticVersion() {
        return CreateLegacyModData.KINETIC_VERSION;
    }

    @TestCode(explanation = "Small test for later, when the cool kinetic stuff is here")
    public static int discoverRotation(World world, BlockPos pos, EnumFacing.Axis axis) {
        switch (axis) {
            case X:
                if (pos.getY() % 2 == pos.getZ() % 2) {
                    return (int) world.getTotalWorldTime() % 4 + 1;
                } else {
                    return (int) world.getTotalWorldTime() % 4;
                }
            case Y:
                if (pos.getX() % 2 == pos.getZ() % 2) {
                    return (int) world.getTotalWorldTime() % 4 + 1;
                } else {
                    return (int) world.getTotalWorldTime() % 4;
                }
            case Z:
                if (pos.getY() % 2 == pos.getX() % 2) {
                    return (int) world.getTotalWorldTime() % 4 + 1;
                } else {
                    return (int) world.getTotalWorldTime() % 4;
                }
        }
        return 0;
    }

    public static int discoverRotationForPlacement(World world, BlockPos pos, EnumFacing.Axis axis) {
        switch (axis) {
            case X:
                if (Math.abs(pos.getY()) % 2 == Math.abs(pos.getZ()) % 2) {
                    return 1;
                } else {
                    return 0;
                }
            case Y:
                if (Math.abs(pos.getX()) % 2 == Math.abs(pos.getZ()) % 2) {
                    return 1;
                } else {
                    return 0;
                }
            case Z:
                if (Math.abs(pos.getY()) % 2 == Math.abs(pos.getX()) % 2) {
                    return 1;
                } else {
                    return 0;
                }
        }
        return 0;
    }

    public static void addBlockToWrenchables(Block block) {
        if (CreateLegacyModData.WRENCHABLES.contains(block)) {
            return;
        }
        CreateLegacyModData.WRENCHABLES.add(block);
    }

    public static void addCreditsToStringList(List<String> stringList) {
        stringList.add("Create Legacy credits");
        stringList.add("Simibubi: for making the original Create mod");
        stringList.add("Okamiz: for pixelating the Create logo");
        stringList.add("Basil: for the Czech translation");
        stringList.add("Magistr Djo: for the Russian translation");
        stringList.add("And all the members of the Discord server, for supporting me");
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
}
