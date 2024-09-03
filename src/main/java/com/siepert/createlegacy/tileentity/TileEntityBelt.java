package com.siepert.createlegacy.tileentity;

import com.siepert.createapi.network.IKineticTE;
import com.siepert.createapi.network.KineticBlockInstance;
import com.siepert.createapi.network.NetworkContext;
import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.CreateLegacyConfigHolder;
import com.siepert.createlegacy.CreateLegacyModData;
import com.siepert.createlegacy.blocks.kinetic.BlockBelt;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class TileEntityBelt extends TileEntity implements IKineticTE {
    @Override
    public double getStressImpact() {
        return CreateLegacyConfigHolder.kineticConfig.beltStressImpact;
    }

    @Override
    public boolean isConsumer() {
        return getStressImpact() > 0;
    }

    @Override
    public double getStressCapacity() {
        return 0;
    }

    @Override
    public int getProducedSpeed() {
        return 0;
    }

    @Override
    public void kineticTick(NetworkContext context) {
        lastKineticTick = world.getTotalWorldTime();
        lastSpeed = context.networkSpeed;


        if (context.networkSpeed == 0) return;

        EnumFacing dir;

        IBlockState state = world.getBlockState(pos);

        boolean inv = context.blockPosBooleanHashMap.get(pos);

        dir = EnumFacing.getFacingFromAxis(inv ? EnumFacing.AxisDirection.NEGATIVE : EnumFacing.AxisDirection.POSITIVE,
                state.getValue(BlockBelt.AXIS));

        double velocityX =
                dir.getFrontOffsetX() * (context.networkSpeed / 256f);
        double velocityZ =
                dir.getFrontOffsetZ() * (context.networkSpeed / 256f);
        AxisAlignedBB bb = new AxisAlignedBB(pos.getX(), pos.getY() + 0.8, pos.getZ(),
                pos.getX() + 1, pos.getY() + 1.2, pos.getZ() + 1);
        List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, bb);

        for (Entity entity : entities) {
            if (!entity.isSneaking()) {
                boolean flag0 = !entity.getTags().contains(CreateLegacyModData.ITEM_OUTPUT_TAG);
                boolean flag1 = world.getBlockState(pos.up(2)).getBlock() == ModBlocks.PRESS;


                boolean h = Math.abs(velocityX) < Math.abs(entity.motionX);
                boolean j = Math.abs(velocityZ) < Math.abs(entity.motionZ);

                entity.setVelocity(h ? entity.motionX : velocityX,
                        entity.motionY,
                        j ? entity.motionZ : velocityZ);
            }
        }
    }

    @Override
    public void setUpdated() {

    }

    @Override
    public void passNetwork(NetworkContext context, EnumFacing source, boolean srcIsCog, boolean srcCogIsHorizontal, boolean inverted) {
        IBlockState state = world.getBlockState(pos);

        boolean axled = state.getValue(BlockBelt.HAS_AXLE);

        if (!axled || srcIsCog) return;

        EnumFacing.Axis axleAxis = state.getValue(BlockBelt.AXIS); //It is in fact not the axle axis :3

        if (source.getAxis() == EnumFacing.Axis.Y) return;

        if (source.getAxis() != axleAxis) {
            context.addKineticBlockInstance(new KineticBlockInstance(pos, inverted));

            EnumFacing j = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, axleAxis);

            extendBelt(pos.offset(j), j, context, inverted, 0);
            extendBelt(pos.offset(j.getOpposite()), j.getOpposite(), context, inverted, 0);
            TileEntity entity = world.getTileEntity(pos.offset(source.getOpposite()));

            if (entity instanceof IKineticTE && !context.hasBlockBeenChecked(pos.offset(source.getOpposite()))) {
                ((IKineticTE) entity).passNetwork(context, source, false, false, inverted);
            }
        }
    }

    private void extendBelt(BlockPos pos, EnumFacing extendDir, NetworkContext context, boolean inverted, int deepness) {
        if (deepness >= CreateLegacyConfigHolder.otherConfig.maxBeltDeepness) return;

        if (context.hasBlockBeenChecked(pos)) return;

        //CreateLegacy.logger.debug("Extended belt at {} in direction {}", pos, extendDir);

        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof BlockBelt) {

            if (state.getValue(BlockBelt.AXIS) != extendDir.getAxis()) return;

            if (state.getValue(BlockBelt.HAS_AXLE)) return;

            context.addKineticBlockInstance(new KineticBlockInstance(pos, inverted));

            extendBelt(pos.offset(extendDir), extendDir, context, inverted, deepness+1);
        }
    }

    long lastKineticTick = 0;
    int lastSpeed = 0;

    @Override
    public int getRS() {
        return world.getTotalWorldTime() == lastKineticTick + 1 ? lastSpeed : 0;
    }
}
