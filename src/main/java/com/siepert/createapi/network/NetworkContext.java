package com.siepert.createapi.network;

import com.siepert.createapi.CreateAPI;
import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.CreateLegacyConfigHolder;
import com.siepert.createlegacy.CreateLegacyModData;
import com.siepert.createlegacy.util.IHasRotation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * It's called NetworkContext, but it is also kind of a network controller I guess
 *
 * @author Siepert123
 */
public class NetworkContext {

    public void runThroughPhases(World world) {
        try {
            this.phase1(world);
            try {
                this.phase2(world);
                try {
                    this.phase3(world);
                } catch (Exception phase3) {
                    CreateLegacy.logger.error("Exception occurred in phase 3 of network");
                    phase3.printStackTrace();
                }
            } catch (Exception phase2) {
                CreateLegacy.logger.error("Exception occurred in phase 2 of network");
                phase2.printStackTrace();
            }
        } catch (Exception phase1) {
            CreateLegacy.logger.error("Exception occurred in phase 1 of network");
            phase1.printStackTrace();
        }
    }

    public boolean infiniteSU = CreateLegacyConfigHolder.otherConfig.disableSU;

    public final List<KineticBlockInstance> blocksToActivate = new ArrayList<>();
    public int networkSpeed = 0;
    public int totalSU = 0;
    public int scheduledConsumedSU = 0;

    public void addKineticBlockInstance(KineticBlockInstance instance) {
        if (!blocksToActivate.contains(instance)) {
            blocksToActivate.add(instance);
        }
    }

    @Nullable
    public KineticBlockInstance getInstanceAtPos(BlockPos pos) {
        for (KineticBlockInstance instance : blocksToActivate) {
            if (instance.pos == pos) return instance;
        }
        return null;
    }

    public void phase1(World world) {
        for (KineticBlockInstance instance : blocksToActivate) {
            IKineticTE kineticTE = (IKineticTE) world.getTileEntity(instance.pos);

            if (kineticTE.isGenerator()) {
                networkSpeed = Math.max(networkSpeed, kineticTE.getProducedSpeed());

                kineticTE.setUpdated();

                totalSU += CreateAPI.longToIntSafe(Math.round(kineticTE.getStressCapacity() * kineticTE.getProducedSpeed()));
            }
        }
    }

    public void phase2(World world) {
        for (KineticBlockInstance instance : blocksToActivate) {
            IKineticTE kineticTE = (IKineticTE) world.getTileEntity(instance.pos);

            if (kineticTE.isConsumer() && kineticTE.getMinimalSpeed() < networkSpeed) {
                scheduledConsumedSU += (int) Math.round(kineticTE.getStressImpact() * networkSpeed);
            }
        }
    }

    public boolean isNetworkOverstressed() {
        if (infiniteSU) return false;
        return scheduledConsumedSU > totalSU;
    }

    public void phase3(World world) {
        for (KineticBlockInstance instance : blocksToActivate) {
            IKineticTE kineticTE = (IKineticTE) world.getTileEntity(instance.pos);

            if (!isNetworkOverstressed()) {
                if (world.getBlockState(instance.pos).getBlock() instanceof IHasRotation) {

                    int rot = CreateAPI.discoverRotation(world, instance.pos,
                            ((IHasRotation) world.getBlockState(instance.pos).getBlock())
                                    .rotateAround(world.getBlockState(instance.pos)),
                            networkSpeed, instance.inverted);

                    if (rot == 4) rot = 0;

                    if (!CreateLegacyConfigHolder.otherConfig.enableBlockstatePerformance) {
                        setStateTESafe(world, instance.pos, world.getBlockState(instance.pos).withProperty(IHasRotation.ROTATION, rot));
                    } else {
                        if (world.getBlockState(instance.pos).getValue(IHasRotation.ROTATION) != rot) {
                            setStateTESafe(world, instance.pos, world.getBlockState(instance.pos).withProperty(IHasRotation.ROTATION, rot));
                        }
                    }
                }
            } else {
                if (world.getBlockState(instance.pos).getBlock() instanceof IHasRotation) {

                    int rot = CreateAPI.discoverRotation(world, instance.pos,
                            ((IHasRotation) world.getBlockState(instance.pos).getBlock())
                            .rotateAround(world.getBlockState(instance.pos)),
                            0, instance.inverted);

                    if (!CreateLegacyConfigHolder.otherConfig.enableBlockstatePerformance) {
                        setStateTESafe(world, instance.pos, world.getBlockState(instance.pos).withProperty(IHasRotation.ROTATION, rot));
                    } else {
                        if (world.getBlockState(instance.pos).getValue(IHasRotation.ROTATION) != rot) {
                            setStateTESafe(world, instance.pos, world.getBlockState(instance.pos).withProperty(IHasRotation.ROTATION, rot));
                        }
                    }
                }
            }

            if (!isNetworkOverstressed() || kineticTE.ignoreOverstress())
                if (kineticTE != null) kineticTE.kineticTick(this);
            else {
                if (CreateLegacyModData.random.nextInt(200) == 0) {
                    world.spawnParticle(EnumParticleTypes.CLOUD,
                            instance.pos.getX() + 0.5,
                            instance.pos.getY() + 0.5,
                            instance.pos.getZ() + 0.5,
                            CreateLegacyModData.random.nextFloat() - CreateLegacyModData.random.nextFloat(),
                            CreateLegacyModData.random.nextFloat() - CreateLegacyModData.random.nextFloat(),
                            CreateLegacyModData.random.nextFloat() - CreateLegacyModData.random.nextFloat());
                }
            }
        }
    }

    private void setStateTESafe(World world, BlockPos pos, IBlockState state) {
        TileEntity entity = world.getTileEntity(pos);

        world.setBlockState(pos, state, 3);

        if (entity != null) {
            entity.validate();
            world.setTileEntity(pos, entity);
        }
    }
}
