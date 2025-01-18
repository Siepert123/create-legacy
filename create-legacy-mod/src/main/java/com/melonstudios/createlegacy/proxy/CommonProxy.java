package com.melonstudios.createlegacy.proxy;

import com.melonstudios.createlegacy.copycat.TileEntityCopycat;
import com.melonstudios.createlegacy.tileentity.*;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {
    public void setItemModel(Item item, int meta, String file) {}
    public void setItemModel(Item item, String file) {}
    public void setItemModel(Item item) {}
    public void preInitClientSetup(FMLPreInitializationEvent event) {}
    public void setTileEntities() {
        registerTE(TileEntityShaft.class, "shaft");
        registerTE(TileEntityCog.class, "cog");
        registerTE(TileEntityGearbox.class, "gearbox");
        registerTE(TileEntityClutch.class, "clutch");
        registerTE(TileEntityGearshift.class, "gearshift");

        registerTE(TileEntitySaw.class, "saw");
        registerTE(TileEntityBearing.class, "bearing");
        registerTE(TileEntityPress.class, "press");
        registerTE(TileEntityMillstone.class, "millstone");
        registerTE(TileEntityFan.class, "fan");
        registerTE(TileEntityDrill.class, "drill");

        registerTE(TileEntityTurntable.class, "turntable");

        registerTE(TileEntityDepot.class, "depot");
        registerTE(TileEntityChute.class, "chute");
        registerTE(TileEntityFunnel.class, "funnel");
        registerTE(TileEntityFunnelAdvanced.class, "funnel_advanced");
        registerTE(TileEntityBeltLegacy.class, "belt_legacy");

        registerTE(TileEntitySpeedometer.class, "speedometer");
        registerTE(TileEntityStressometer.class, "stressometer");

        registerTE(TileEntityHandCrank.class, "handcrank");
        registerTE(TileEntityWaterWheel.class, "water_wheel");
        registerTE(TileEntityFlywheel.class, "flywheel");
        registerTE(TileEntityCreativeMotor.class, "creative_motor");

        registerTE(TileEntityCopycat.class, "copycat");

        registerTE(TileEntityBlazeBurner.class, "blazeburner");


        registerTE(TileEntityChigwanker.class, "chigwanker");
    }
    private void registerTE(Class<? extends TileEntity> te, String registry) {
        GameRegistry.registerTileEntity(te, new ResourceLocation("create", registry));
    }
    public void setTERenderers() {

    }


    public MinecraftServer getMCServer() {
        return FMLCommonHandler.instance().getMinecraftServerInstance(); // Works when remote?
    }
}
