package com.melonstudios.createlegacy;

import com.melonstudios.createapi.CreateAPI;
import com.melonstudios.createlegacy.copycat.PacketUpdateCopycat;
import com.melonstudios.createlegacy.fluid.ModFluids;
import com.melonstudios.createlegacy.network.*;
import com.melonstudios.createlegacy.proxy.CommonProxy;
import com.melonstudios.createlegacy.recipe.RecipeInit;
import com.melonstudios.createlegacy.schematic.SchematicSaveHelper;
import com.melonstudios.createlegacy.tab.DecorationsTab;
import com.melonstudios.createlegacy.tab.KineticsTab;
import com.melonstudios.createlegacy.util.BitSplitter;
import com.melonstudios.createlegacy.util.registries.ModSoundEvents;
import com.melonstudios.createlegacy.world.gen.WorldGeneratorCreateLegacy;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import static com.melonstudios.createlegacy.CreateLegacy.*;

@Mod(
        modid = MOD_ID,
        name = "Create Legacy",
        version = VERSION,
        dependencies = DEPENDENCIES
)
public final class CreateLegacy {
    static {
        ModFluids.setupFluids();
    }

    public static final boolean test = true;
    public static final String MOD_ID = "create";
    public static final String VERSION = "0.2";
    public static final String DEPENDENCIES =
            test ? "required-before:mysticallib;before:jei" : "required-before-client:ctm;required-before:mysticallib;before:jei";

    public static final int VERSION_NUM = 8;
    public static final int KINETIC_VERSION_NUM = 0;

    public static final CreativeTabs TAB_KINETICS = new KineticsTab();
    public static final CreativeTabs TAB_DECORATIONS = new DecorationsTab();

    private static SimpleNetworkWrapper networkWrapper;
    public static SimpleNetworkWrapper getNetworkWrapper() {
        return networkWrapper;
    }


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
    public static void setItemModel(Block block, int meta, String file) {
        Item item = Item.getItemFromBlock(block);
        if (item != Items.AIR) {
            setItemModel(item, meta, file);
        }
    }
    public static void setItemModel(Block block, String file) {
        Item item = Item.getItemFromBlock(block);
        if (item != Items.AIR) {
            setItemModel(item, file);
        }
    }
    public static void setItemModel(Block block) {
        Item item = Item.getItemFromBlock(block);
        if (item != Items.AIR) {
            setItemModel(item);
        }
    }

    private static int networkId = -1;
    private static int getNetworkDiscriminator() {
        networkId++;
        return networkId;
    }
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInitClientSetup(event);

        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);
        networkWrapper.registerMessage(
                new PacketUpdateDepot.Handler(),
                PacketUpdateDepot.class,
                getNetworkDiscriminator(), Side.CLIENT
        );
        networkWrapper.registerMessage(
                new PacketUpdatePress.Handler(),
                PacketUpdatePress.class,
                getNetworkDiscriminator(), Side.CLIENT
        );
        networkWrapper.registerMessage(
                new PacketUpdateMillstone.Handler(),
                PacketUpdateMillstone.class,
                getNetworkDiscriminator(), Side.CLIENT
        );
        networkWrapper.registerMessage(
                new PacketUpdateChute.Handler(),
                PacketUpdateChute.class,
                getNetworkDiscriminator(), Side.CLIENT
        );
        networkWrapper.registerMessage(
                new PacketUpdateBearing.Handler(),
                PacketUpdateBearing.class,
                getNetworkDiscriminator(), Side.CLIENT
        );
        networkWrapper.registerMessage(
                new PacketUpdateFunnelAdvanced.Handler(),
                PacketUpdateFunnelAdvanced.class,
                getNetworkDiscriminator(), Side.CLIENT
        );
        networkWrapper.registerMessage(
                new PacketUpdateHandCrank.Handler(),
                PacketUpdateHandCrank.class,
                getNetworkDiscriminator(), Side.CLIENT
        );
        networkWrapper.registerMessage(
                new PacketUpdateCopycat.Handler(),
                PacketUpdateCopycat.class,
                getNetworkDiscriminator(), Side.SERVER
        );
        networkWrapper.registerMessage(
                new PacketUpdateCopycat.Handler(),
                PacketUpdateCopycat.class,
                getNetworkDiscriminator(), Side.CLIENT
        );
        networkWrapper.registerMessage(
                new PacketUpdateCreativeMotor.Handler(),
                PacketUpdateCreativeMotor.class,
                getNetworkDiscriminator(), Side.SERVER
        );
        networkWrapper.registerMessage(
                new PacketUpdateCreativeMotor.Handler(),
                PacketUpdateCreativeMotor.class,
                getNetworkDiscriminator(), Side.CLIENT
        );
        networkWrapper.registerMessage(
                new PacketUpdateBlazeBurner.Handler(),
                PacketUpdateBlazeBurner.class,
                getNetworkDiscriminator(), Side.CLIENT
        );
        try {SchematicSaveHelper.makeSchematicsFolder();}
        catch (Exception ignored) {}
        BitSplitter.runTests(!CreateConfig.preventBitSplitterTestCrash);
        CreateAPI.discoverAndSortAddons(event);
        GameRegistry.registerWorldGenerator(new WorldGeneratorCreateLegacy(), 1);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        RecipeInit.init();
        ModSoundEvents.registerSounds();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

    /**
     * Creates a new Bounding Box, but uses pixels instead of blocks.
     */
    public static AxisAlignedBB aabb(int x1, int y1, int z1, int x2, int y2, int z2) {
        return new AxisAlignedBB(x1 / 16.0, y1 / 16.0, z1 / 16.0, x2 / 16.0, y2 / 16.0, z2 / 16.0);
    }

    /**
     * Creates a new Bounding Box of a certain size wrapping a block pos.
     */
    public static AxisAlignedBB aabb(BlockPos pos, int range) {
        return new AxisAlignedBB(
                pos.add(-range, -range, -range),
                pos.add(range, range, range)
        );
    }
}
