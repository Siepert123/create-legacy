package com.melonstudios.createlegacy.proxy;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.copycat.TileEntityCopycat;
import com.melonstudios.createlegacy.copycat.TileEntityCopycatRenderer;
import com.melonstudios.createlegacy.tileentity.*;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKineticRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nullable;
import java.io.File;

public class ClientProxy extends CommonProxy {

    public void setItemModel(Item item, int meta, String file) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(CreateLegacy.MOD_ID, file), "inventory"));
    }
    public void setItemModel(Item item, String file) {
        setItemModel(item, 0, file);
    }
    public void setItemModel(Item item) {
        setItemModel(item, item.getRegistryName().getResourcePath());
    }
    public void preInitClientSetup(FMLPreInitializationEvent event) {
        try {
            if (new File(Minecraft.getMinecraft().mcDataDir, "porkchop.gears").exists())
                AbstractTileEntityKineticRenderer.pork();
        } catch (Throwable ignored) {}
    }
    public void setTERenderers() {
        registerTER(TileEntityShaft.class, "shaft", new TileEntityShaftRenderer());
        registerTER(TileEntityCog.class, "cog", new TileEntityCogRenderer());
        registerTER(TileEntityGearbox.class, "gearbox", new TileEntityGearboxRenderer());
        registerTER(TileEntityClutch.class, "clutch", new TileEntityClutchRenderer());
        registerTER(TileEntityGearshift.class, "gearshift", new TileEntityGearshiftRenderer());

        registerTER(TileEntitySaw.class, "saw", new TileEntitySawRenderer());
        registerTER(TileEntityBearing.class, "bearing", new TileEntityBearingRenderer());
        registerTER(TileEntityPress.class, "press", new TileEntityPressRenderer());
        registerTER(TileEntityMillstone.class, "millstone", new TileEntityMillstoneRenderer());
        registerTER(TileEntityFan.class, "fan", new TileEntityFanRenderer());
        registerTER(TileEntityDrill.class, "drill", new TileEntityDrillRenderer());

        registerTER(TileEntityTurntable.class, "turntable", new TileEntityTurntableRenderer());

        registerTER(TileEntityDepot.class, "depot", new TileEntityDepotRenderer());
        registerTER(TileEntityChute.class, "chute", new TileEntityChuteRenderer());
        registerTER(TileEntityFunnel.class, "funnel", null);
        registerTER(TileEntityFunnelAdvanced.class, "funnel_advanced", new TileEntityFunnelAdvancedRenderer());

        registerTER(TileEntitySpeedometer.class, "speedometer", new TileEntitySpeedometerRenderer());
        registerTER(TileEntityStressometer.class, "stressometer", new TileEntityStressometerRenderer());

        registerTER(TileEntityHandCrank.class, "handcrank", new TileEntityHandCrankRenderer());
        registerTER(TileEntityWaterWheel.class, "water_wheel", new TileEntityWaterWheelRenderer());
        registerTER(TileEntityFlywheel.class, "flywheel", new TileEntityFlywheelRenderer());
        registerTER(TileEntityCreativeMotor.class, "creative_motor", new TileEntityCreativeMotorRenderer());

        registerTER(TileEntityCopycat.class, "copycat", new TileEntityCopycatRenderer());

        registerTER(TileEntityBlazeBurner.class, "blazeburner", new TileEntityBlazeBurnerRenderer());


        registerTER(TileEntityChigwanker.class, "chigwanker", null);
    }
    private void registerTER(Class<? extends TileEntity> te, String registry, @Nullable TileEntitySpecialRenderer<?> renderer) {
        try {
            if (renderer != null) TileEntityRendererDispatcher.instance.renderers.put(te, renderer);
        } catch (RuntimeException ignored) {

        }
    }

}
