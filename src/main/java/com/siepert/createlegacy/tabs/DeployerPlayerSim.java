package com.siepert.createlegacy.tabs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;

import java.util.UUID;

public class DeployerPlayerSim extends EntityPlayer {
    public DeployerPlayerSim(EntityPlayer toCopy) {
        super(toCopy.world, toCopy.getGameProfile());

        this.setUniqueId(UUID.randomUUID());
    }

    EnumFacing overrideFacing = EnumFacing.NORTH;

    public void setData(EnumFacing facing) {
        overrideFacing = facing;
    }

    @Override
    public EnumFacing getHorizontalFacing() {
        return overrideFacing;
    }

    @Override
    public EnumFacing getAdjustedHorizontalFacing() {
        return overrideFacing;
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isCreative() {
        return false;
    }
}
