package com.siepert.createlegacy.util.handlers;

import com.siepert.createlegacy.tileentity.TileEntityBlazeBurner;
import com.siepert.createlegacy.tileentity.TileEntityCreativeMotor;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityHandler {
    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileEntityCreativeMotor.class, "creative_motor");
        GameRegistry.registerTileEntity(TileEntityBlazeBurner.class, "blaze_burner");
    }
}
