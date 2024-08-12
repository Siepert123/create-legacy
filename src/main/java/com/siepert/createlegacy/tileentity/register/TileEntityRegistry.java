package com.siepert.createlegacy.tileentity.register;

import com.siepert.createlegacy.tileentity.*;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityRegistry {
    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileEntityBlazeBurner.class, "blaze_burner");
        GameRegistry.registerTileEntity(TileEntityChute.class, "chute");
        GameRegistry.registerTileEntity(TileEntityFunnel.class, "funnel");
        GameRegistry.registerTileEntity(TileEntityFunnelAdvanced.class, "funnel_advanced");

        GameRegistry.registerTileEntity(TileEntityAxle.class, "axle");
        GameRegistry.registerTileEntity(TileEntityCogwheel.class, "cogwheel");
        GameRegistry.registerTileEntity(TileEntityFurnaceFlywheel.class, "furnace_flywheel");
        GameRegistry.registerTileEntity(TileEntityHandCrank.class, "hand_crank");
        GameRegistry.registerTileEntity(TileEntityWaterWheel.class, "water_wheel");
        GameRegistry.registerTileEntity(TileEntityCreativeMotor.class, "creative_motor");
        GameRegistry.registerTileEntity(TileEntityMillStone.class, "millstone");
        GameRegistry.registerTileEntity(TileEntityPress.class, "press");
        GameRegistry.registerTileEntity(TileEntityDrill.class, "drill");
        GameRegistry.registerTileEntity(TileEntitySaw.class, "saw");
        GameRegistry.registerTileEntity(TileEntityDeployer.class, "deployer");

        GameRegistry.registerTileEntity(TileEntitySpeedometer.class, "speedometer");
        GameRegistry.registerTileEntity(TileEntityStressometer.class, "stressometer");
    }
}
