package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createapi.kinetic.INeedsRecalculating;
import com.melonstudios.createlegacy.block.BlockRender;
import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.block.kinetic.BlockFan;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKinetic;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class TileEntityFan extends AbstractTileEntityKinetic implements INeedsRecalculating {
    @Override
    protected String namePlate() {
        return "Fan";
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        return facing().getOpposite() == side ? connection(1) : connection(0);
    }
    protected EnumFacing facing() {
        return getState().getValue(BlockFan.FACING);
    }
    public IBlockState getAssociatedPropellerPart() {
        IBlockState render = ModBlocks.RENDER.getDefaultState();
        switch (facing()) {
            case UP: return render.withProperty(BlockRender.TYPE, BlockRender.Type.FAN_U);
            case DOWN: return render.withProperty(BlockRender.TYPE, BlockRender.Type.FAN_D);
            case NORTH: return render.withProperty(BlockRender.TYPE, BlockRender.Type.FAN_N);
            case EAST: return render.withProperty(BlockRender.TYPE, BlockRender.Type.FAN_E);
            case SOUTH: return render.withProperty(BlockRender.TYPE, BlockRender.Type.FAN_S);
            case WEST: return render.withProperty(BlockRender.TYPE, BlockRender.Type.FAN_W);
        }
        return render;
    }

    protected AirCurrent current;

    @Override
    protected void tick() {
        if (current == null) current = new AirCurrent(this);
        else current.tick();
    }

    protected static double max(double d0, double d1) {
        if (Math.abs(d0) > Math.abs(d1)) return d0;
        return d1;
    }

    @Override
    public void recalculate() {
        current = new AirCurrent(this);
    }

    public static class AirCurrent {
        public final World world;
        public final BlockPos source;
        public final EnumFacing facing;
        public final TileEntityFan fan;
        public float strength = 0.0f;
        public int maxDistance = 0;
        public int actualMaxDistance = 0;

        public AirCurrent(TileEntityFan fan) {
            this.world = fan.getWorld();
            this.source = fan.getPos();
            this.facing = fan.facing();
            this.fan = fan;

            recalculate();
        }

        public void recalculate() {
            this.strength = fan.speed() / 256f;
            this.maxDistance = this.actualMaxDistance = Math.max(Math.round(Math.abs(fan.speed()) / 16), 3);
            for (int i = 1; i <= maxDistance; i++) {
                IBlockState state = world.getBlockState(source.offset(facing, i));
                if (state.getMaterial().blocksMovement()) {
                    actualMaxDistance = i;
                    break;
                }
            }
        }

        public void tick() {
            if (fan.speed() == 0) return;
            recalculate();
            final AxisAlignedBB boundingBox = new AxisAlignedBB(source, source.offset(facing, actualMaxDistance).add(1, 1, 1));

            List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, boundingBox);

            for (Entity entity : entities) {
                if (entity instanceof EntityPlayer) {
                    if (world.isRemote) {
                        entity.setVelocity(max(entity.motionX / 4, strength * facing.getFrontOffsetX()),
                                max(entity.motionY / 4, strength * facing.getFrontOffsetY()),
                                max(entity.motionZ / 4, strength * facing.getFrontOffsetZ()));
                        if (strength > 0) entity.fallDistance = 0.0f;
                    }
                } else {
                    entity.setVelocity(max(entity.motionX / 4, strength * facing.getFrontOffsetX()),
                                max(entity.motionY / 4, strength * facing.getFrontOffsetY()),
                                max(entity.motionZ / 4, strength * facing.getFrontOffsetZ()));
                    if (strength > 0) entity.fallDistance = 0.0f;
                }
            }
        }
    }
}
