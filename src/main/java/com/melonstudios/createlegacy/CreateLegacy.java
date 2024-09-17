package com.melonstudios.createlegacy;

import com.melonstudios.createapi.CreateAPI;
import com.melonstudios.createlegacy.proxy.CommonProxy;
import com.melonstudios.createlegacy.tab.DecorationsTab;
import com.melonstudios.createlegacy.tab.KineticsTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import static com.melonstudios.createlegacy.CreateLegacy.MOD_ID;
import static com.melonstudios.createlegacy.CreateLegacy.VERSION;

@Mod(modid = MOD_ID, name = "Create Legacy", version = VERSION)
public class CreateLegacy {
    public static final String MOD_ID = "create";
    public static final String VERSION = "1.0.0";

    public static final int VERSION_NUM = 0;
    public static final int KINETIC_VERSION_NUM = 0;

    public static final CreativeTabs TAB_KINETICS = new KineticsTab();
    public static final CreativeTabs TAB_DECORATIONS = new DecorationsTab();

    @SidedProxy(serverSide = "com.melonstudios.createlegacy.proxy.CommonProxy",
        clientSide = "com.melonstudios.createlegacy.proxy.ClientProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        CreateAPI.discoverAddons(event);
        CreateAPI.sortAddons();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
