package nl.melonstudios.create.init;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nl.melonstudios.create.CreateLegacy;
import nl.melonstudios.create.block.BlockCasing;
import nl.melonstudios.create.block.deco.BlockFramedGlass;
import nl.melonstudios.create.block.deco.BlockWindowIron;
import nl.melonstudios.create.block.deco.BlockWindowWood;
import nl.melonstudios.create.block.state.EnumOrestoneVariant;
import nl.melonstudios.create.item.*;
import nl.melonstudios.create.util.FunnelSets;
import nl.melonstudios.create.util.ModTabs;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ItemInit {
    public static void load() {}

    public static final CreativeTabs TAB_CREATE = new ModTabs("create", () -> new ItemStack(BlockInit.COG_SMALL));
    public static final CreativeTabs TAB_CREATE_DECORATIONS = new ModTabs("create.decorations", () -> new ItemStack(BlockInit.WINDOW_IRON));

    public static final ArrayList<Item> ITEMS = new ArrayList<>();

    public static final ItemWrench WRENCH = (ItemWrench)
            registerItem(new ItemWrench()
            .setRegistryName("wrench").setUnlocalizedName("create.wrench"));
    public static final ItemIngredient INGREDIENT = registerItem(new ItemIngredient());
    public static final ItemGoggles GOGGLES = registerItem(new ItemGoggles());
    public static final ItemSandpaper SANDPAPER = registerItem(new ItemSandpaper());
    public static final ItemBeltConnector BELT_CONNECTOR = (ItemBeltConnector)
            registerItem(new ItemBeltConnector()
            .setRegistryName("belt_connector").setUnlocalizedName("create.belt_connector"));
    public static final ItemTreeFertilizer TREE_FERTILIZER = (ItemTreeFertilizer)
            registerItem(new ItemTreeFertilizer()
            .setRegistryName("tree_fertilizer").setUnlocalizedName("create.tree_fertilizer"));
    public static final ItemGlue SUPER_GLUE = (ItemGlue)
            registerItem(new ItemGlue(256)
            .setRegistryName("superglue").setUnlocalizedName("create.superglue"));
    public static final Item CRAFTER_COVER = registerItem(new Item()
            .setMaxStackSize(64).setCreativeTab(TAB_CREATE)
            .setRegistryName("crafter_cover").setUnlocalizedName("create.crafter_cover"));
    public static final ItemArmorCardboard HELMET_CARDBOARD = registerItem(new ItemArmorCardboard(EntityEquipmentSlot.HEAD, "helmet"));
    public static final ItemArmorCardboard CHESTPLATE_CARDBOARD = registerItem(new ItemArmorCardboard(EntityEquipmentSlot.CHEST, "chestplate"));
    public static final ItemArmorCardboard LEGGINGS_CARDBOARD = registerItem(new ItemArmorCardboard(EntityEquipmentSlot.LEGS, "leggings"));
    public static final ItemArmorCardboard BOOTS_CARDBOARD = registerItem(new ItemArmorCardboard(EntityEquipmentSlot.FEET, "boots"));
    public static final ItemAssembly ASSEMBLY = registerItem(new ItemAssembly());
    public static final ItemSceneWand SCENE_WAND = registerItem(new ItemSceneWand());

    public static final ItemBlockFunnel FUNNEL_ANDESITE = registerItem(new ItemBlockFunnel("andesite"));
    public static final ItemBlockFunnel FUNNEL_BRASS = registerItem(new ItemBlockFunnel("brass"));

    private static <T extends Item> T registerItem(T item) {
        ITEMS.add(item);
        return item;
    }

    @SideOnly(Side.CLIENT)
    public static void setItemModels() {
        // items
        CreateLegacy.proxy.setItemModel(WRENCH);
        for (int i = 0; i < ItemIngredient.NAME_LOOKUP.length; i++) {
            CreateLegacy.proxy.setItemModel(INGREDIENT, i, "ingredient/" + ItemIngredient.NAME_LOOKUP[i]);
        }
        CreateLegacy.proxy.setItemModel(GOGGLES);
        CreateLegacy.proxy.setItemModel(SANDPAPER);
        CreateLegacy.proxy.setItemModel(BELT_CONNECTOR);
        CreateLegacy.proxy.setItemModel(TREE_FERTILIZER);
        CreateLegacy.proxy.setItemModel(SUPER_GLUE);
        CreateLegacy.proxy.setItemModel(CRAFTER_COVER);
        CreateLegacy.proxy.setItemModel(HELMET_CARDBOARD);
        CreateLegacy.proxy.setItemModel(CHESTPLATE_CARDBOARD);
        CreateLegacy.proxy.setItemModel(LEGGINGS_CARDBOARD);
        CreateLegacy.proxy.setItemModel(BOOTS_CARDBOARD);
        CreateLegacy.proxy.setItemModel(SCENE_WAND);
        CreateLegacy.proxy.setItemModel(ASSEMBLY, 0, "assembly/" + ItemAssembly.NAME_LOOKUP[0]);
        CreateLegacy.proxy.setItemModel(ASSEMBLY, 1, "assembly/" + ItemAssembly.NAME_LOOKUP[1]);
        CreateLegacy.proxy.setItemModel(FUNNEL_ANDESITE);
        CreateLegacy.proxy.setItemModel(FUNNEL_BRASS);

        // blocks
        CreateLegacy.proxy.setItemModel(BlockInit.ORE, 0, "ore_copper");
        CreateLegacy.proxy.setItemModel(BlockInit.ORE, 1, "ore_zinc");

        CreateLegacy.proxy.setItemModel(BlockInit.METAL, 0, "block_andesite_alloy");
        CreateLegacy.proxy.setItemModel(BlockInit.METAL, 1, "block_copper");
        CreateLegacy.proxy.setItemModel(BlockInit.METAL, 2, "block_zinc");
        CreateLegacy.proxy.setItemModel(BlockInit.METAL, 3, "block_brass");

        for (int i = 0; i < 4; i++) {
            CreateLegacy.proxy.setItemModel(BlockInit.CASING, i, "casing_" + BlockCasing.Variant.byID(i).getName());
        }

        CreateLegacy.proxy.setItemModel(BlockInit.SHAFT);
        CreateLegacy.proxy.setItemModel(BlockInit.COG_SMALL);
        CreateLegacy.proxy.setItemModel(BlockInit.COG_LARGE);
        CreateLegacy.proxy.setItemModel(BlockInit.GEARBOX, 0, "gearbox");
        CreateLegacy.proxy.setItemModel(BlockInit.GEARBOX, 1, "gearbox_vertical");
        CreateLegacy.proxy.setItemModel(BlockInit.GEARSHIFT);
        CreateLegacy.proxy.setItemModel(BlockInit.CLUTCH);
        CreateLegacy.proxy.setItemModel(BlockInit.HAND_CRANK);
        CreateLegacy.proxy.setItemModel(BlockInit.WATER_WHEEL);
        CreateLegacy.proxy.setItemModel(BlockInit.FURNACE_ENGINE, 0, "furnace_engine");
        CreateLegacy.proxy.setItemModel(BlockInit.FURNACE_ENGINE, 1, "furnace_flywheel");
        CreateLegacy.proxy.setItemModel(BlockInit.CREATIVE_MOTOR);
        CreateLegacy.proxy.setItemModel(BlockInit.TURNTABLE);
        CreateLegacy.proxy.setItemModel(BlockInit.BEARING);
        CreateLegacy.proxy.setItemModel(BlockInit.BEARING_WINDMILL);
        CreateLegacy.proxy.setItemModel(BlockInit.CHASSIS_RADIAL);
        CreateLegacy.proxy.setItemModel(BlockInit.CHASSIS_LINEAR, 0, "chassis_linear");
        CreateLegacy.proxy.setItemModel(BlockInit.CHASSIS_LINEAR, 1, "chassis_linear_secondary");
        CreateLegacy.proxy.setItemModel(BlockInit.SAIL_ITEM);
        CreateLegacy.proxy.setItemModel(BlockInit.PISTON_POLE);
        CreateLegacy.proxy.setItemModel(BlockInit.MECHANICAL_PISTON);
        CreateLegacy.proxy.setItemModel(BlockInit.MECHANICAL_PISTON_STICKY);
        CreateLegacy.proxy.setItemModel(BlockInit.SPEEDOMETER);
        CreateLegacy.proxy.setItemModel(BlockInit.STRESSOMETER);
        CreateLegacy.proxy.setItemModel(BlockInit.PRESS);
        CreateLegacy.proxy.setItemModel(BlockInit.MIXER);
        CreateLegacy.proxy.setItemModel(BlockInit.DRILL);
        CreateLegacy.proxy.setItemModel(BlockInit.SAW);
        CreateLegacy.proxy.setItemModel(BlockInit.DEPLOYER);
        CreateLegacy.proxy.setItemModel(BlockInit.AUTO_FARM, 0, "plough");
        CreateLegacy.proxy.setItemModel(BlockInit.AUTO_FARM, 1, "harvester");
        CreateLegacy.proxy.setItemModel(BlockInit.CONTRAPTION_INTERFACE, 0, "interface_storage");
        CreateLegacy.proxy.setItemModel(BlockInit.CONTRAPTION_INTERFACE, 1, "interface_fluid");
        CreateLegacy.proxy.setItemModel(BlockInit.MILLSTONE);
        CreateLegacy.proxy.setItemModel(BlockInit.CRAFTER);
        CreateLegacy.proxy.setItemModel(BlockInit.BLAZE_BURNER, 0, "blaze_burner_empty");
        CreateLegacy.proxy.setItemModel(BlockInit.BLAZE_BURNER, 1, "blaze_burner");
        CreateLegacy.proxy.setItemModel(BlockInit.DEPOT);
        CreateLegacy.proxy.setItemModel(BlockInit.BASIN);
        CreateLegacy.proxy.setItemModel(BlockInit.CHUTE);
        CreateLegacy.proxy.setItemModel(BlockInit.ITEM_DRAIN);

        CreateLegacy.proxy.setItemModel(BlockInit.LATCH);
        CreateLegacy.proxy.setItemModel(BlockInit.TOGGLE_LATCH);
        CreateLegacy.proxy.setItemModel(BlockInit.REDSTONE_LINKER);

        for (int i = 0; i < 4; i++) {
            String type = BlockFramedGlass.Variant.byId(i).getName();
            CreateLegacy.proxy.setItemModel(BlockInit.FRAMED_GLASS, i, i != 0 ? "framed_glass_" + type : "framed_glass");
        }
        for (int i = 0; i < 6; i++) {
            String type = BlockWindowWood.Variant.byId(i).getName();
            CreateLegacy.proxy.setItemModel(BlockInit.WINDOW_WOOD, i, "window_" + type);
        }
        for (int i = 0; i < 3; i++) {
            String type = BlockWindowIron.Variant.byId(i).getName();
            CreateLegacy.proxy.setItemModel(BlockInit.WINDOW_IRON, i, "window_" + type);
        }

        for (int i = 0; i < 16; i++) {
            EnumDyeColor color = EnumDyeColor.byMetadata(i);
            CreateLegacy.proxy.setItemModel(BlockInit.POUF, i, "pouf/" + color.getDyeColorName());
        }

        for (int i = 0; i < 7; i++) {
            String type = EnumOrestoneVariant.byId(i).getName();
            CreateLegacy.proxy.setItemModel(BlockInit.ORESTONE, i ,"orestone/" + type);
            CreateLegacy.proxy.setItemModel(BlockInit.ORESTONE_CUT, i ,"orestone/cut/" + type);
            CreateLegacy.proxy.setItemModel(BlockInit.ORESTONE_POLISHED, i, "orestone/polished/" + type);
            CreateLegacy.proxy.setItemModel(BlockInit.ORESTONE_BRICKS, i ,"orestone/bricks/" + type);
            CreateLegacy.proxy.setItemModel(BlockInit.ORESTONE_BRICKS_FANCY, i ,"orestone/bricks_fancy/" + type);
            CreateLegacy.proxy.setItemModel(BlockInit.ORESTONE_LAYERED, i, "orestone/layered/" + type);
            CreateLegacy.proxy.setItemModel(BlockInit.ORESTONE_PILLAR_Y, i, "orestone/pillar/" + type);
        }
    }

    private ItemInit() {
        throw new AssertionError("no");
    }
}
