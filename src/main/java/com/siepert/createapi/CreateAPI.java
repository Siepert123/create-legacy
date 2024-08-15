package com.siepert.createapi;

import com.siepert.createapi.addons.ICreateAddon;
import com.siepert.createlegacy.CreateLegacyModData;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

/**
 * The Create Legacy API
 * Addons can be registered through <code>registerAddon(ICreateAddon addon)</code>.
 * oh btw there are also some random functions here that have no other place ig
 *
 * @author Siepert123
 * */
public final class CreateAPI {


    private static final List<ICreateAddon> ADDONS = new ArrayList<>();

    /**
     * Registers a Create addon.
     * @param addon Your addon.
     */
    public static void registerAddon(ICreateAddon addon) {
        if (!isModAlreadyRegistered(addon.getModId())) {
            ADDONS.add(addon);
        }
    }

    private static boolean isModAlreadyRegistered(@Nonnull String id) {
        for (ICreateAddon addon : ADDONS) {
            if (addon.getModId().equals(id)) return true;
        }
        return false;
    }

    private static final List<ICreateAddon> ADDONS_IN_PRIORITY = new ArrayList<>();
    public static List<ICreateAddon> getAddons() {
        return ADDONS_IN_PRIORITY;
    }


    public static void consumeAddons() {
        int lowestInt = 0;
        int highestInt = 0;
        for (ICreateAddon addon : ADDONS) {
            if (addon.getLoadPriority() < lowestInt) lowestInt = addon.getLoadPriority();
            if (addon.getLoadPriority() > highestInt) highestInt = addon.getLoadPriority();
        }

        for (int i = lowestInt; i <= highestInt; i++) {
            for (ICreateAddon addon : ADDONS) {
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

    /**
     * Find rotation based on time, network speed & position.
     *
     * @param world The world. Mainly used for {@link World#getTotalWorldTime()}
     * @param pos The position.
     * @param axis The axis where the rotations happen around.
     * @param speed Speed of the network.
     * @param inverted Is the rotation inverted?
     * @return Integer (0-3, sometimes 4 for some reason) which represents the rotation state.
     */
    public static int discoverRotation(World world, BlockPos pos, EnumFacing.Axis axis, int speed, boolean inverted) {
        switch (axis) {
            case X:
                if (Math.abs(pos.getY()) % 2 == Math.abs(pos.getZ()) % 2) {
                    return findRotationModifier(world.getTotalWorldTime(), speed, inverted) + 1;
                } else {
                    return findRotationModifier(world.getTotalWorldTime(), speed, inverted);
                }
            case Y:
                if (Math.abs(pos.getX()) % 2 == Math.abs(pos.getZ()) % 2) {
                    return findRotationModifier(world.getTotalWorldTime(), speed, inverted) + 1;
                } else {
                    return findRotationModifier(world.getTotalWorldTime(), speed, inverted);
                }
            case Z:
                if (Math.abs(pos.getY()) % 2 == Math.abs(pos.getX()) % 2) {
                    return findRotationModifier(world.getTotalWorldTime(), speed, inverted) + 1;
                } else {
                    return findRotationModifier(world.getTotalWorldTime(), speed, inverted);
                }
        }
        return 0;
    }

    private static final int bluh = 256;
    private static int findRotationModifier(long time, int speed, boolean inverted) {
        if (speed == 0) return 0;

        int timeBetween = Math.max(1, bluh / speed);

        long guh = time / timeBetween;

        int notInv = longToIntSafe(guh % 4);
        int inv = invertRotationInteger(notInv);

        if (inverted) return inv;
        return notInv;
    }
    private static int invertRotationInteger(int rot) {
        switch (rot) {
            case 0:
                return 0;
            case 1:
                return 3;
            case 2:
                return 2;
            case 3:
                return 1;
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
}
