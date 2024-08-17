package com.siepert.createlegacy.integration;

import com.siepert.createlegacy.tileentity.TileEntitySpeedometer;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.ILuaObject;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SpeedometerPeripheral implements IPeripheral {
    TileEntitySpeedometer parent;
    public SpeedometerPeripheral(TileEntitySpeedometer parent) {
        this.parent = parent;
    }
    @Nonnull
    @Override
    public String getType() {
        return "speedometer";
    }

    @Nonnull
    @Override
    public String[] getMethodNames() {
        return new String[]{"getNetworkSpeed"};
    }

    @Nullable
    @Override
    public Object[] callMethod(@Nonnull IComputerAccess iComputerAccess, @Nonnull ILuaContext iLuaContext, int i,
                               @Nonnull Object[] objects) throws LuaException, InterruptedException {
        switch(i) {
            case 0:
                return new Object[]{ new Integer(parent.getLastContext().networkSpeed) };
            default:
                throw new LuaException("Invalid method index '" + i + "' !");
        }
    }

    @Override
    public boolean equals(@Nullable IPeripheral iPeripheral) {
        return ((Object)this).equals(iPeripheral);
    }

    @Override
    @Nonnull
    public Object getTarget() {
        return this.parent;
    }
}
