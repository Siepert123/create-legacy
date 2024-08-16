package com.siepert.createlegacy.integration;

import com.siepert.createlegacy.tileentity.TileEntityStressometer;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StressometerPeripheral implements IPeripheral {
    TileEntityStressometer parent;
    public StressometerPeripheral(TileEntityStressometer parent) {
        this.parent = parent;
    }
    @Nonnull
    @Override
    public String getType() {
        return "stressometer";
    }

    @Nonnull
    @Override
    public String[] getMethodNames() {
        return new String[]{
                "getNetworkStress()",
                "isNetworkOverstressed()",
                "isNetworkInfiniteSU()"
        };
    }

    @Nullable
    @Override
    public Object[] callMethod(@Nonnull IComputerAccess iComputerAccess, @Nonnull ILuaContext iLuaContext, int i, @Nonnull Object[] objects) throws LuaException, InterruptedException {
        switch(i) {
            case 0:
                return new Object[]{ new Integer(parent.getLastContext().scheduledConsumedSU) };
            case 1:
                return new Object[]{ new Boolean(parent.getLastContext().isNetworkOverstressed()) };
            case 2:
                return new Object[]{ new Boolean(parent.getLastContext().infiniteSU) };
            default:
                throw new LuaException("Invalid method index '" + i + "' !");
        }
    }

    @Override
    public boolean equals(@Nullable IPeripheral iPeripheral) {
        return this.equals(iPeripheral);
    }

    @Override
    @Nonnull
    public Object getTarget() {
        return this.parent;
    }
}
