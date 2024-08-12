package com.siepert.createlegacy;

import com.siepert.createapi.AddonLoadException;
import com.siepert.createapi.CreateAPI;
import com.siepert.createapi.CreateAddon;
import com.siepert.createapi.ICreateAddon;
import com.siepert.createlegacy.proxy.CommonProxy;
import com.siepert.createlegacy.tabs.CreateModDecoTab;
import com.siepert.createlegacy.tabs.CreateModOtherTab;
import com.siepert.createlegacy.tabs.CreateModTab;
import com.siepert.createlegacy.util.handlers.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
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
    public void init(FMLInitializationEvent event) throws AddonLoadException {
        //Create addon loading
        logger.info("Consuming all registered addons");
        CreateAPI.consumeAddons();
        logger.info("Found {} addons to load", CreateAPI.getAddons().size());
        int totalErrors = 0;
        int totalAddons = 0;
        for (ICreateAddon addon : CreateAPI.getAddons()) {
            int errors = 0;
            logger.info("Begun loading addon {} (priority index {})",
                    addon.getModId(), addon.getLoadPriority());
            if (addon.getCreateVersion() < CreateAPI.getVersion()) {
                logger.warn("Addon {} was made for Create version {}, current version is {}",
                        addon.getModId(), addon.getCreateVersion(), CreateAPI.getVersion());
                errors++;
            }
            if (addon.getKineticVersion() != CreateAPI.getKineticVersion()) {
                logger.error("Addon {} was made for kinetic version {}, current version is {}!",
                        addon.getModId(), addon.getKineticVersion(), CreateAPI.getKineticVersion());
                AddonLoadException.kineticVersionMismatch(addon.getModId(), addon.getKineticVersion());
            }
            addon.onLoad(event);
            logger.info("Successfully loaded addon {} with {} error(s)", addon.getModId(), errors);
            totalErrors += errors;
            totalAddons++;
        }
        if (totalAddons > 0) {
            logger.info("Loaded {} addons, {} error(s) total", totalAddons, totalErrors);
        }

        RegistryHandler.otherInitRegistries();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        RegistryHandler.otherPostInitRegistries();
    }
}
