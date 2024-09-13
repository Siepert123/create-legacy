package com.siepert.createlegacy.tileentity.register;

import com.siepert.createlegacy.tileentity.*;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityRegistry {
    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileEntityBlazeBurner.class, "create:blaze_burner");
        GameRegistry.registerTileEntity(TileEntityChute.class, "create:chute");
        GameRegistry.registerTileEntity(TileEntityFunnel.class, "create:funnel");
        GameRegistry.registerTileEntity(TileEntityFunnelAdvanced.class, "create:funnel_advanced");

        GameRegistry.registerTileEntity(TileEntityAxle.class, "create:axle");
        GameRegistry.registerTileEntity(TileEntityCogwheel.class, "create:cogwheel");
        GameRegistry.registerTileEntity(TileEntityFurnaceFlywheel.class, "create:furnace_flywheel");
        GameRegistry.registerTileEntity(TileEntityHandCrank.class, "create:hand_crank");
        GameRegistry.registerTileEntity(TileEntityWaterWheel.class, "create:water_wheel");
        GameRegistry.registerTileEntity(TileEntityCreativeMotor.class, "create:creative_motor");
        GameRegistry.registerTileEntity(TileEntityMillStone.class, "create:millstone");
        GameRegistry.registerTileEntity(TileEntityPress.class, "create:press");
        GameRegistry.registerTileEntity(TileEntityDrill.class, "create:drill");
        GameRegistry.registerTileEntity(TileEntitySaw.class, "create:saw");
        GameRegistry.registerTileEntity(TileEntityDeployer.class, "create:deployer");
        GameRegistry.registerTileEntity(TileEntityFan.class, "create:fan");
        GameRegistry.registerTileEntity(TileEntityBelt.class, "create:belt");

        GameRegistry.registerTileEntity(TileEntityGearbox.class, "create:gearbox");
        GameRegistry.registerTileEntity(TileEntityClutch.class, "create:clutch");
        GameRegistry.registerTileEntity(TileEntityGearshift.class, "create:gearshift");

        GameRegistry.registerTileEntity(TileEntitySpeedometer.class, "create:speedometer");
        GameRegistry.registerTileEntity(TileEntityStressometer.class, "create:stressometer");

        GameRegistry.registerTileEntity(TileEntityChigwanker.class, "create:chigwanker");

        GameRegistry.registerTileEntity(TileEntityBasin.class, "create:basin");

        GameRegistry.registerTileEntity(TileEntityMultiblockExtension.class, "create:multiblock_extension");
    }
}
