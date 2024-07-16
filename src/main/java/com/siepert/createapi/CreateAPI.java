package com.siepert.createapi;

import com.siepert.createlegacy.CreateLegacyModData;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class CreateAPI {
    public static boolean compareCreateVersions(int versionOther) {
        return versionOther == getVersion();
    }

    public static int getVersion() {
        return CreateLegacyModData.VERSION_NUMBER;
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
}
