package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKinetic;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;

import java.util.List;

public class TileEntityTurntable extends AbstractTileEntityKinetic {
    @Override
    protected String namePlate() {
        return "Turntable";
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        return side == EnumFacing.DOWN ? connection(1) : connection(0);
    }

    @Override
    protected void tick() {
        List<Entity> entities = world.getEntitiesWithinAABB(Entity.class,
                CreateLegacy.aabb(0, 8, 0, 16, 10, 16)
                        .offset(pos));

        for (Entity entity : entities) {
            entity.rotationYaw -= speed() / 3.0f;
        }
    }

    @Override
    public float consumedStressMarkiplier() {
        return 1.0f;
    }
}
