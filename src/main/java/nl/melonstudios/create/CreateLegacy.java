package nl.melonstudios.create;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nl.melonstudios.create.extensions.IExtensionEntityRenderer;
import nl.melonstudios.create.init.*;
import nl.melonstudios.create.kinetics.BlockStressValues;
import nl.melonstudios.create.kinetics.contraption.Contraption;
import nl.melonstudios.create.network.CreateLegacyPacketManager;
import nl.melonstudios.create.proxy.CommonProxy;
import nl.melonstudios.create.worldgen.CreateWorldGen;
import org.apache.logging.log4j.Logger;

import java.util.Random;

import static nl.melonstudios.create.CreateLegacy.DEPENDENCIES;

@Mod(modid = CreateLegacy.MODID, name = CreateLegacy.NAME, version = CreateLegacy.VERSION, useMetadata = true, dependencies = DEPENDENCIES, acceptedMinecraftVersions = "1.12.2")
public class CreateLegacy {
    private static final boolean inIDE = true;
    public static final String MODID = "create";
    public static final String NAME = "Create Legacy";
    public static final String VERSION = "26w45a";
    static final String DEPENDENCIES = "required-after:melonlib@[1.11.3,);required-after:mixinbooter" + (inIDE ? "" : ";required-after-client:ctm");

    @SidedProxy(
            serverSide = "nl.melonstudios.create.proxy.CommonProxy",
            clientSide = "nl.melonstudios.create.proxy.ClientProxy"
    )
    public static CommonProxy proxy;

    public static Logger logger;
    public static final Random rand = new Random();

    // Pre pre init
    static {
        BlockInit.load();
        ItemInit.load();
        FluidInit.init();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.clientPreInit(event);

        CreateLegacyPacketManager.bus = NetworkRegistry.INSTANCE.newEventDrivenChannel(CreateLegacyPacketManager.CHANNEL);
        CreateLegacyPacketManager.bus.register(new CreateLegacyPacketManager());

        logger = event.getModLog();

        GameRegistry.registerWorldGenerator(new CreateWorldGen(), 0);

        SoundInit.init();

        FluidInit.register();

        proxy.registerRecipeTypes();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.clientInit(event);

        CreateLegacy.proxy.initiatePonders();
        OreDictInit.scanMetalTypes();
        RecipeInit.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.clientPostInit(event);

        proxy.pork();
        BlockStressValues.initialize();
        Contraption.registerValidInventoryClasses();
    }

    @EventHandler
    public void youGotMail(FMLInterModComms.IMCEvent event) {
        for (FMLInterModComms.IMCMessage message : event.getMessages()) {
            if (message.isStringMessage()) {
                if ("addContraptionInventory".equals(message.key)) {
                    Contraption.addValidInventoryFromIMC(message);
                }
            }
        }
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        CommandInit.addCreateCommand(event);
    }

    @SideOnly(Side.CLIENT)
    public static float getRenderTimeF(float pt) {
        return (float)IExtensionEntityRenderer.getRendererUpdateCount(Minecraft.getMinecraft().entityRenderer) + pt;
    }
    @SideOnly(Side.CLIENT)
    public static double getRenderTimeD(double pt) {
        return (double)IExtensionEntityRenderer.getRendererUpdateCount(Minecraft.getMinecraft().entityRenderer) + pt;
    }
}
