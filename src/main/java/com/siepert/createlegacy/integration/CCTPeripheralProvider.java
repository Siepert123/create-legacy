package com.siepert.createlegacy.integration;

import com.siepert.createapi.network.IKineticTE;
import com.siepert.createlegacy.blocks.kinetic.BlockNetworkMeter;
import com.siepert.createlegacy.tileentity.TileEntitySpeedometer;
import com.siepert.createlegacy.tileentity.TileEntityStressometer;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CCTPeripheralProvider implements IPeripheralProvider {
    public static final CCTPeripheralProvider INSTANCE = new CCTPeripheralProvider();
    public static void register() {
        ComputerCraftAPI.registerPeripheralProvider(INSTANCE);
    }
    @Nullable
    @Override
    public IPeripheral getPeripheral(@Nonnull World world, @Nonnull BlockPos blockPos, @Nonnull EnumFacing enumFacing) {
        if (!(world.getBlockState(blockPos).getBlock() instanceof BlockNetworkMeter))
            return null;
        TileEntity te = world.getTileEntity(blockPos);
        assert te instanceof IKineticTE;
        if (te instanceof TileEntitySpeedometer) {
            return (IPeripheral) ((TileEntitySpeedometer) te).speedometerPeripheral;
        } else if (te instanceof TileEntityStressometer) {
            return (IPeripheral) ((TileEntityStressometer) te).stressometerPeripheral;
        }
        else {
            return null;
        }
    }
}
