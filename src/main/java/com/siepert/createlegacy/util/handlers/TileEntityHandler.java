package com.siepert.createlegacy.util.handlers;

import com.siepert.createlegacy.tileentity.*;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityHandler {
    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileEntityCreativeMotor.class, "creative_motor");
        GameRegistry.registerTileEntity(TileEntityBlazeBurner.class, "blaze_burner");
        GameRegistry.registerTileEntity(TileEntityFurnaceFlywheel.class, "furnace_flywheel");
        GameRegistry.registerTileEntity(TileEntityHandCrank.class, "hand_crank");
        GameRegistry.registerTileEntity(TileEntityWaterWheel.class, "water_wheel");
        GameRegistry.registerTileEntity(TileEntityMillStone.class, "millstone");
    }
}
