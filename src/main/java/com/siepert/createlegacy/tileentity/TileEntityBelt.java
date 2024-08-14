package com.siepert.createlegacy.tileentity;

import com.siepert.createapi.network.IKineticTE;
import com.siepert.createapi.network.KineticBlockInstance;
import com.siepert.createapi.network.NetworkContext;
import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.CreateLegacyConfigHolder;
import com.siepert.createlegacy.blocks.kinetic.BlockBelt;
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
        if (context.networkSpeed == 0) return;

        EnumFacing dir;

        IBlockState state = world.getBlockState(pos);

        boolean inv = !context.getInstanceAtPos(pos).inverted;



        dir = EnumFacing.getFacingFromAxis(inv ? EnumFacing.AxisDirection.NEGATIVE : EnumFacing.AxisDirection.POSITIVE,
                state.getValue(BlockBelt.AXIS));

        double velocityX =
                inv ? dir.getFrontOffsetX() * (context.networkSpeed / 256f) * -1
                        : dir.getFrontOffsetX() * (context.networkSpeed / 256f);
        double velocityZ =
                inv ? dir.getFrontOffsetZ() * (context.networkSpeed / 256f) * -1
                        : dir.getFrontOffsetZ() * (context.networkSpeed / 256f);
        AxisAlignedBB bb = new AxisAlignedBB(pos.getX(), pos.getY() + 0.8, pos.getZ(),
                                             pos.getX() + 1, pos.getY() + 1.2, pos.getZ() + 1);
        List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, bb);

        for (Entity entity : entities) {
            if (!entity.isSneaking()) {
                entity.setVelocity(Math.max(velocityX, entity.motionX), entity.motionY, Math.max(velocityZ, entity.motionZ));
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

            if (entity instanceof IKineticTE) {
                ((IKineticTE) entity).passNetwork(context, source, false, false, inverted);
            }
        }
    }

    private void extendBelt(BlockPos pos, EnumFacing extendDir, NetworkContext context, boolean inverted, int deepness) {
        if (deepness >= CreateLegacyConfigHolder.otherConfig.maxBeltDeepness) return;

        if (context.getInstanceAtPos(pos) != null) return;

        //CreateLegacy.logger.debug("Extended belt at {} in direction {}", pos, extendDir);

        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof BlockBelt) {

            if (state.getValue(BlockBelt.AXIS) != extendDir.getAxis()) return;

            if (state.getValue(BlockBelt.HAS_AXLE)) return;

            context.addKineticBlockInstance(new KineticBlockInstance(new BlockPos(pos.getX(), pos.getY(), pos.getZ()), inverted));

            extendBelt(pos.offset(extendDir), extendDir, context, inverted, deepness+1);
        }
    }
}
