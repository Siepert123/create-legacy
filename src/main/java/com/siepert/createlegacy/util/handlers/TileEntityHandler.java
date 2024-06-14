package com.siepert.createlegacy.util.handlers;

import com.siepert.createlegacy.tileentity.TileEntityBlazeBurner;
import com.siepert.createlegacy.tileentity.TileEntityCreativeMotor;
import com.siepert.createlegacy.tileentity.TileEntityFurnaceFlywheel;
import com.siepert.createlegacy.tileentity.TileEntityHandCrank;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityHandler {
    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileEntityCreativeMotor.class, "creative_motor");
        GameRegistry.registerTileEntity(TileEntityBlazeBurner.class, "blaze_burner");
        GameRegistry.registerTileEntity(TileEntityFurnaceFlywheel.class, "furnace_flywheel");
        GameRegistry.registerTileEntity(TileEntityHandCrank.class, "hand_crank");
    }
}
