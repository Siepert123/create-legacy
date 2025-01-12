package com.melonstudios.createapi.network;

import com.melonstudios.createapi.kinetic.IKineticTileEntity;
import com.melonstudios.createlegacy.util.DisplayLink;
import com.melonstudios.createlegacy.util.INetworkLogger;
import com.melonstudios.createlegacy.util.registries.ModSoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class NetworkContext {
    private final World world;
    public NetworkContext(World world) {
        this.world = world;
    }
    public World getWorld() {
        return world;
    }

    private final Map<IKineticTileEntity, Boolean> map = new HashMap<>();

    public boolean checked (IKineticTileEntity te) {
        return map.containsKey(te);
    }
    public boolean checked(BlockPos pos) {
        for (Map.Entry<IKineticTileEntity, Boolean> entry : map.entrySet()) {
            if (entry.getKey().getPos() == pos) return true;
        }
        return false;
    }
    public boolean isInverted(IKineticTileEntity te) {
        return map.get(te);
    }
    public boolean isInverted(BlockPos pos) {
        for (Map.Entry<IKineticTileEntity, Boolean> entry : map.entrySet()) {
            if (entry.getKey().getPos() == pos) return isInverted(entry.getKey());
        }
        return false;
    }

    private boolean infiniteSU = false;
    public boolean hasInfiniteSU() {
        return infiniteSU;
    }
    public void enableInfiniteSU() {
        infiniteSU = true;
    }
    private float networkSpeed = 0;
    public float speed() {
        return networkSpeed;
    }
    public void addSpeed(float speed) {
        networkSpeed = Math.max(networkSpeed, speed);
    }
    private float totalSU = 0;
    public float totalSU() {
        return totalSU;
    }
    public void addSU(float su) {
        totalSU += su;
    }
    private float consumedSU = 0;
    public float consumedSU() {
        return consumedSU;
    }
    public void consumeSU(float su) {
        consumedSU += su;
    }

    public void add(IKineticTileEntity te, boolean inverted) {
        map.put(te, inverted);
    }

    public boolean overstressed() {
        if (hasInfiniteSU()) return false;
        return consumedSU() > totalSU();
    }

    private void phase1() {
        for (Map.Entry<IKineticTileEntity, Boolean> entry : map.entrySet()) {
            if (entry.getKey().isGenerator()) {
                addSpeed(Math.abs(entry.getKey().generatedRPM()));
                addSU(entry.getKey().generatedSUMarkiplier() * Math.abs(entry.getKey().generatedRPM()));

                entry.getKey().setUpdated();
            }
        }
    }
    private void phase2() {
        for (Map.Entry<IKineticTileEntity, Boolean> entry : map.entrySet()) {
            if (entry.getKey().isConsumer()) {
                consumeSU(entry.getKey().consumedStressMarkiplier() * speed());

                entry.getKey().setUpdated();
            }
        }
    }
    private void phase3() {
        int soundCD = 0;
        for (Map.Entry<IKineticTileEntity, Boolean> entry : map.entrySet()) {
            if (!overstressed()) {
                entry.getKey().updateSpeed(isInverted(entry.getKey()) ? -speed() : speed());

                if (entry.getKey() instanceof INetworkLogger) {
                    ((INetworkLogger) entry.getKey()).setSU(consumedSU());
                    ((INetworkLogger) entry.getKey()).setMaxSU(totalSU());
                }

                if (soundCD == 0) {
                    try {
                        if (networkSpeed != 0 && !world.isRemote) {
                            if (world.getTotalWorldTime() % (20 * 6) == 0) {
                                if (entry.getKey() instanceof TileEntity) {
                                    world.playSound(null, ((TileEntity) entry.getKey()).getPos(),
                                            ModSoundEvents.BLOCK_COG_AMBIENT, SoundCategory.BLOCKS,
                                            Math.min(1, Math.abs(networkSpeed) / 128), 1);
                                }
                            }
                        }
                    } catch (LinkageError error) {
                        error.printStackTrace();
                    }
                    soundCD = 64;
                } else soundCD--;
            } else {
                entry.getKey().updateSpeed(0.0f);
            }

            entry.getKey().networkFunc(this);
        }
    }

    public void start() {
        phase1();
        phase2();
        phase3();
    }
}
