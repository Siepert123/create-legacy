package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createapi.network.NetworkContext;
import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.block.kinetic.BlockWaterWheel;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKinetic;
import com.melonstudios.createlegacy.util.AdvancementUtil;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class TileEntityWaterWheel extends AbstractTileEntityKinetic {
    public TileEntityWaterWheel() {
        super();
    }
    @Override
    protected String namePlate() {
        return "Water wheel";
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        EnumFacing.Axis axis = world.getBlockState(pos).getValue(BlockWaterWheel.AXIS);
        return side.getAxis() == axis ? EnumKineticConnectionType.SHAFT : EnumKineticConnectionType.NONE;
    }

    @Override
    protected void onFirstGeneration() {
        if (!world.isRemote) {
            Advancement advancement = CreateLegacy.serverHack.getAdvancementManager()
                    .getAdvancement(new ResourceLocation("create", "generators/water_wheel"));
            List<EntityPlayerMP> players = world.getEntities(EntityPlayerMP.class, (player) -> player.getDistanceSq(pos) < 256);
            for (EntityPlayerMP player : players) {
                AdvancementUtil.grantAchievement(player, advancement);
            }
        }
    }

    @Override
    protected void tick() {
        if (isUpdated() || generatedRPM() == 0) return;
        NetworkContext context = new NetworkContext(world);

        passNetwork(null, null, context, false);

        context.start();
    }

    @Override
    public float generatedRPM() {
        return world.getBlockState(pos.down()).getMaterial().isLiquid() ? 8.0f : 0.0f;
    }

    @Override
    public float generatedSUMarkiplier() {
        return 32.0f;
    }
}
