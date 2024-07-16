package com.siepert.createapi;

import com.siepert.createlegacy.ModData;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CreateAPI {
    public static boolean compareCreateVersions(int versionOther) {
        return versionOther == getVersion();
    }

    public static int getVersion() {
        return ModData.VERSION_NUMBER;
    }

    public static int getKineticVersion() {
        return ModData.KINETIC_VERSION;
    }

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
}
