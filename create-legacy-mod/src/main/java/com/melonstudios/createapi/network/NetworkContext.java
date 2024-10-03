package com.melonstudios.createapi.network;

import com.melonstudios.createlegacy.tileentity.AbstractTileEntityKinetic;
import com.melonstudios.createlegacy.util.DisplayLink;
import com.melonstudios.createlegacy.util.INetworkLogger;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public final class NetworkContext {
    private final World world;
    public NetworkContext(World world) {
        this.world = world;
    }
    public World getWorld() {
        return world;
    }

    private Map<AbstractTileEntityKinetic, Boolean> map = new HashMap<>();

    public boolean checked (AbstractTileEntityKinetic te) {
        return map.containsKey(te);
    }
    public boolean checked(BlockPos pos) {
        for (Map.Entry<AbstractTileEntityKinetic, Boolean> entry : map.entrySet()) {
            if (entry.getKey().getPos() == pos) return true;
        }
        return false;
    }
    public boolean isInverted(AbstractTileEntityKinetic te) {
        return map.get(te);
    }
    public boolean isInverted(BlockPos pos) {
        for (Map.Entry<AbstractTileEntityKinetic, Boolean> entry : map.entrySet()) {
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

    public void add(AbstractTileEntityKinetic te, boolean inverted) {
        map.put(te, inverted);
    }

    public boolean overstressed() {
        if (hasInfiniteSU()) return false;
        return consumedSU() > totalSU();
    }

    private void phase1() {
        for (Map.Entry<AbstractTileEntityKinetic, Boolean> entry : map.entrySet()) {
            if (entry.getKey().isGenerator()) {
                addSpeed(entry.getKey().generatedRPM());
                addSU(entry.getKey().generatedSUMarkiplier() * entry.getKey().generatedRPM());

                entry.getKey().setUpdated();
            }
        }
    }
    private void phase2() {
        for (Map.Entry<AbstractTileEntityKinetic, Boolean> entry : map.entrySet()) {
            if (entry.getKey().isConsumer()) {
                consumeSU(entry.getKey().consumedStressMarkiplier() * speed());

                entry.getKey().setUpdated();
            }
        }
    }
    private void phase3() {
        for (Map.Entry<AbstractTileEntityKinetic, Boolean> entry : map.entrySet()) {
            if (!overstressed()) {
                entry.getKey().updateSpeed(isInverted(entry.getKey()) ? -speed() : speed());

                if (entry.getKey() instanceof INetworkLogger) {
                    ((INetworkLogger) entry.getKey()).setSU(consumedSU());
                    ((INetworkLogger) entry.getKey()).setMaxSU(totalSU());
                }
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
