package com.melonstudios.createlegacy.util;

import com.melonstudios.createlegacy.tileentity.AbstractTileEntityKinetic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public final class NetworkContext {
    public Map<BlockPos, Boolean> blockMap = new HashMap<>();

    public boolean hasBeenChecked(BlockPos pos) {
        return blockMap.containsKey(pos);
    }
    public void addBlock(BlockPos pos, boolean inv) {
        if (!hasBeenChecked(pos)) {
            blockMap.put(pos, inv);
        }
    }
    public boolean isInvAt(BlockPos pos) {
        TileEntity entity = world.getTileEntity(pos);

        if (entity instanceof AbstractTileEntityKinetic) {
            return ((AbstractTileEntityKinetic) entity).speed() < 0;
        }
        return false;
    }

    public void setInfiniteSU() {
        infiniteSU = true;
    }
    public boolean isInfiniteSU() {
        return infiniteSU;
    }

    private World world;
    public void setWorld(World world) {
        this.world = world;
    }
    public World getWorld() {
        return world;
    }
    public boolean infiniteSU = false;
    public int networkSpeed = 0;
    public int totalSU = 0;
    public int consumedSU = 0;

    public boolean overstressed() {
        return consumedSU > totalSU;
    }

    private void phase1(World world) {
        setWorld(world);

        for (Map.Entry<BlockPos, Boolean> entry : blockMap.entrySet()) {
            AbstractTileEntityKinetic te = (AbstractTileEntityKinetic) world.getTileEntity(entry.getKey());

            if (te != null) {
                networkSpeed = Math.max(networkSpeed, te.generatesRS());
                totalSU += te.generatesSU() * te.generatesRS();
            }
        }
    }

    private void phase2(World world) {
        setWorld(world);

        for (Map.Entry<BlockPos, Boolean> entry : blockMap.entrySet()) {
            AbstractTileEntityKinetic te = (AbstractTileEntityKinetic) world.getTileEntity(entry.getKey());

            if (te != null) {
                consumedSU += te.consumesSU() * networkSpeed;
            }
        }
    }

    private void phase3(World world) {
        setWorld(world);

        for (Map.Entry<BlockPos, Boolean> entry : blockMap.entrySet()) {
            AbstractTileEntityKinetic te = (AbstractTileEntityKinetic) world.getTileEntity(entry.getKey());

            if (te != null) {
                if (!overstressed()) {
                    te.updateSpeed(networkSpeed * (isInvAt(entry.getKey()) ? -1 : 1));
                } else {
                    te.updateSpeed(0);
                }
                if (te instanceof INetworkLogger) {
                    ((INetworkLogger) te).setSU(consumedSU);
                    ((INetworkLogger) te).setMaxSU(totalSU);
                }
            }
        }
    }

    public void phases(World world) {
        try {
            phase1(world);
        } catch (Exception e) {
            DisplayLink.error("Something in phase 1 of network went wrong!!");
            e.printStackTrace();
        } finally {
            try {
                phase2(world);
            } catch (Exception e) {
                DisplayLink.error("Something in phase 2 of network went wrong!!");
                e.printStackTrace();
            } finally {
                try {
                    phase3(world);
                } catch (Exception e) {
                    DisplayLink.error("Something in phase 3 of network went wrong!!");
                    e.printStackTrace();
                }
            }
        }
    }
}
