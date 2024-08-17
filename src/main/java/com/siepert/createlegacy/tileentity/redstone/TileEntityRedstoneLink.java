package com.siepert.createlegacy.tileentity.redstone;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static com.siepert.createlegacy.blocks.redstone.BlockRedstoneLink.*;

public class TileEntityRedstoneLink extends TileEntity implements ITickable {
    public static final List<TileEntityRedstoneLink> REDSTONE_LINKS = new ArrayList<>();

    private static boolean shouldPower(@Nullable EnumDyeColor filter) {
        for (TileEntityRedstoneLink link : REDSTONE_LINKS) {
            if (link.isTransmitter()) {
                if (filter == link.getFilter()) return true;
            }
        }
        return false;
    }

    @Nullable
    private EnumDyeColor filter = null;

    public void setFilter(@Nullable EnumDyeColor filter) {
        this.filter = filter;
    }

    public @Nullable EnumDyeColor getFilter() {
        return this.filter;
    }

    @Override
    public void onLoad() {
        REDSTONE_LINKS.add(this);
    }

    public boolean isTransmitter() {
        return !world.getBlockState(pos).getValue(RECEIVER);
    }

    public void onBreak() {
        REDSTONE_LINKS.remove(this);
    }

    @Override
    public void update() {
        IBlockState state = world.getBlockState(pos);
        if (shouldPower(this.getFilter())) {
            if (!state.getValue(POWERED)) {
                world.setBlockState(pos, state.withProperty(POWERED, true), 3);

                this.validate();
                world.setTileEntity(pos, this);
            }
        } else {
            if (state.getValue(POWERED)) {
                world.setBlockState(pos, state.withProperty(POWERED, false), 3);

                this.validate();
                world.setTileEntity(pos, this);
            }
        }
    }
}
