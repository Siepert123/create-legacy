package com.melonstudios.createlegacy;

import com.melonstudios.createapi.CreateAPI;
import com.melonstudios.createlegacy.proxy.CommonProxy;
import com.melonstudios.createlegacy.recipe.RecipeInit;
import com.melonstudios.createlegacy.schematic.SchematicSaveHelper;
import com.melonstudios.createlegacy.tab.DecorationsTab;
import com.melonstudios.createlegacy.tab.KineticsTab;
import com.melonstudios.createlegacy.util.BitSplitter;
import com.melonstudios.createlegacy.world.gen.WorldGeneratorCreateLegacy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static com.melonstudios.createlegacy.CreateLegacy.MOD_ID;
import static com.melonstudios.createlegacy.CreateLegacy.VERSION;

@Mod(modid = MOD_ID, name = "Create Legacy", version = VERSION)
public final class CreateLegacy {
    public static final String MOD_ID = "create";
    public static final String VERSION = "0.1.0";

    public static final int VERSION_NUM = 0;
    public static final int KINETIC_VERSION_NUM = 0;

    public static final CreativeTabs TAB_KINETICS = new KineticsTab();
    public static final CreativeTabs TAB_DECORATIONS = new DecorationsTab();

    @SidedProxy(serverSide = "com.melonstudios.createlegacy.proxy.CommonProxy",
        clientSide = "com.melonstudios.createlegacy.proxy.ClientProxy")
    public static CommonProxy proxy;
    public static void setItemModel(Item item, int meta, String file) {
        proxy.setItemModel(item, meta, file);
    }
    public static void setItemModel(Item item, String file) {
        proxy.setItemModel(item, file);
    }
    public static void setItemModel(Item item) {
        proxy.setItemModel(item);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        SchematicSaveHelper.makeSchematicsFolder();
        BitSplitter.runTests(!CreateConfig.preventBitSplitterTestCrash);
        CreateAPI.discoverAndSortAddons(event);
        GameRegistry.registerWorldGenerator(new WorldGeneratorCreateLegacy(), 1);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        RecipeInit.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

    public static AxisAlignedBB aabb(int x1, int y1, int z1, int x2, int y2, int z2) {
        return new AxisAlignedBB(x1 / 16.0, y1 / 16.0, z1 / 16.0, x2 / 16.0, y2 / 16.0, z2 / 16.0);
    }
}
