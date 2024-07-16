package com.siepert.createlegacy;

import com.siepert.createlegacy.proxy.CommonProxy;
import com.siepert.createlegacy.tabs.CreateModDecoTab;
import com.siepert.createlegacy.tabs.CreateModOtherTab;
import com.siepert.createlegacy.tabs.CreateModTab;
import com.siepert.createlegacy.util.handlers.RegistryHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = CreateLegacyModData.MOD_ID, name = CreateLegacyModData.NAME, version = CreateLegacyModData.VERSION)
public final class CreateLegacy {

    public static Logger logger;

    public static final CreativeTabs TAB_CREATE = new CreateModTab("tab_create");
    public static final CreativeTabs TAB_CREATE_DECORATIONS = new CreateModDecoTab("tab_create_decorations");
    public static final CreativeTabs TAB_CREATE_OTHER = new CreateModOtherTab();

    @SidedProxy(clientSide = CreateLegacyModData.CLIENT_PROXY, serverSide = CreateLegacyModData.COMMON_PROXY)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        if (!Loader.isModLoaded("ctm")) logger.error("It's recommended to install CTM");
        RegistryHandler.otherPreInitRegistries();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        RegistryHandler.otherInitRegistries();
    }

    @EventHandler
    public void init(FMLPostInitializationEvent event) {
        RegistryHandler.otherPostInitRegistries();
    }
}
