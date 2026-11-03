package nl.melonstudios.create.init;

import com.melonstudios.melonlib.blockdict.BlockDictionary;
import com.melonstudios.melonlib.misc.MetaBlock;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashSet;

public final class OreDictInit {
    public static void init() {
        //region Create wrench pickups
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.CASING, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.SHAFT, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.COG_SMALL, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.COG_LARGE, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.GEARBOX, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.GEARSHIFT, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.CLUTCH, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.HAND_CRANK, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.WATER_WHEEL, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.CREATIVE_MOTOR, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.TURNTABLE, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.BEARING, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.BEARING_WINDMILL, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.CHASSIS_LINEAR, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.CHASSIS_RADIAL, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.SAIL_DOWN, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.SAIL_UP, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.SAIL_NORTH, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.SAIL_SOUTH, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.SAIL_WEST, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.SAIL_EAST, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.SPEEDOMETER, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.STRESSOMETER, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.PRESS, 2);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.MIXER, false);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.DRILL, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.SAW, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.DEPLOYER, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.AUTO_FARM, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.CONTRAPTION_INTERFACE, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.MILLSTONE, false);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.CRAFTER, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.BLAZE_BURNER, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.DEPOT, false);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.BASIN, false);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.CHUTE, 3);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.BELT_STRAIGHT, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.ITEM_DRAIN, false);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.LATCH, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.LATCH_POWERED, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.TOGGLE_LATCH, true);
        BlockDictionary.registerOre("create:wrenchPickup", BlockInit.TOGGLE_LATCH_POWERED, true);
        //endregion

        //region Vanilla wrench pickups
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.DISPENSER, 6);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.NOTEBLOCK, false);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.PISTON, 6);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.STICKY_PISTON, 6);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.LEVER, true);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.STONE_PRESSURE_PLATE, true);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.WOODEN_PRESSURE_PLATE, true);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.REDSTONE_TORCH, true);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.UNLIT_REDSTONE_TORCH, true);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.STONE_BUTTON, true);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.REDSTONE_LAMP, true);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.LIT_REDSTONE_LAMP, true);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.TRIPWIRE_HOOK, true);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.WOODEN_BUTTON, true);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, true);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, true);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.DAYLIGHT_DETECTOR, true);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.DAYLIGHT_DETECTOR_INVERTED, true);
        for (int i = 0; i < 6; i++) {
            if (i == 1) continue;
            BlockDictionary.registerOre("create:wrenchPickup", MetaBlock.of(Blocks.HOPPER, i));
            BlockDictionary.registerOre("create:wrenchPickup", MetaBlock.of(Blocks.HOPPER, i | 8));
        }
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.DROPPER, 6);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.OBSERVER, true);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.REDSTONE_WIRE, true);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.POWERED_REPEATER, true);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.UNPOWERED_REPEATER, true);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.POWERED_COMPARATOR, true);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.UNPOWERED_COMPARATOR, true);

        BlockDictionary.registerOre("create:wrenchPickup", Blocks.GOLDEN_RAIL, 6);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.DETECTOR_RAIL, 6);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.RAIL, true);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.ACTIVATOR_RAIL, 6);
        //endregion

        //region Upright on belt
        OreDictionary.registerOre("create:uprightOnBelt", Items.GLASS_BOTTLE);
        OreDictionary.registerOre("create:uprightOnBelt", Items.POTIONITEM);
        OreDictionary.registerOre("create:uprightOnBelt", Items.SPLASH_POTION);
        OreDictionary.registerOre("create:uprightOnBelt", Items.LINGERING_POTION);
        OreDictionary.registerOre("create:uprightOnBelt", Items.EXPERIENCE_BOTTLE);

        OreDictionary.registerOre("create:uprightOnBelt", Items.BOWL);
        OreDictionary.registerOre("create:uprightOnBelt", Items.BEETROOT_SOUP);
        OreDictionary.registerOre("create:uprightOnBelt", Items.RABBIT_STEW);
        OreDictionary.registerOre("create:uprightOnBelt", Items.MUSHROOM_STEW);

        OreDictionary.registerOre("create:uprightOnBelt", Items.BUCKET);
        OreDictionary.registerOre("create:uprightOnBelt", Items.WATER_BUCKET);
        OreDictionary.registerOre("create:uprightOnBelt", Items.LAVA_BUCKET);
        OreDictionary.registerOre("create:uprightOnBelt", Items.MILK_BUCKET);
        if (FluidRegistry.isUniversalBucketEnabled())
            OreDictionary.registerOre("create:uprightOnBelt", ForgeModContainer.getInstance().universalBucket);
        //endregion

        //region Windmill sails
        BlockDictionary.registerOre("create:sail", Blocks.WOOL, true);
        BlockDictionary.registerOre("create:sail", BlockInit.SAIL_DOWN, true);
        BlockDictionary.registerOre("create:sail", BlockInit.SAIL_UP, true);
        BlockDictionary.registerOre("create:sail", BlockInit.SAIL_NORTH, true);
        BlockDictionary.registerOre("create:sail", BlockInit.SAIL_SOUTH, true);
        BlockDictionary.registerOre("create:sail", BlockInit.SAIL_WEST, true);
        BlockDictionary.registerOre("create:sail", BlockInit.SAIL_EAST, true);
        //endregion

        //region Immovable
        BlockDictionary.registerOre("create:immovable", Blocks.BEDROCK, false);
        //endregion

        BlockDictionary.registerOre("create:depotActorCompatible", BlockInit.DEPOT, false);
        BlockDictionary.registerOre("create:depotActorCompatible", BlockInit.BASIN, true);
        BlockDictionary.registerOre("create:depotActorCompatible", BlockInit.BELT_STRAIGHT, true);

        BlockDictionary.registerOre("create:sticksToSelf", BlockInit.CHASSIS_LINEAR, true);
        BlockDictionary.registerOre("create:autoAttachToChassisRadial", BlockInit.SAIL_DOWN, true);
        BlockDictionary.registerOre("create:autoAttachToChassisRadial", BlockInit.SAIL_UP, true);
        BlockDictionary.registerOre("create:autoAttachToChassisRadial", BlockInit.SAIL_NORTH, true);
        BlockDictionary.registerOre("create:autoAttachToChassisRadial", BlockInit.SAIL_SOUTH, true);
        BlockDictionary.registerOre("create:autoAttachToChassisRadial", BlockInit.SAIL_WEST, true);
        BlockDictionary.registerOre("create:autoAttachToChassisRadial", BlockInit.SAIL_EAST, true);
        BlockDictionary.registerOre("create:bypassGlue", BlockInit.DEPOT, false);
        BlockDictionary.registerOre("create:plowable", Blocks.DIRT, false);
        BlockDictionary.registerOre("create:plowable", Blocks.GRASS, true);
        BlockDictionary.registerOre("create:plowable", Blocks.GRASS_PATH, true);

        for (int i = 0; i < 16; i++) registerOreBlockItem("create:pouf", BlockInit.POUF, i);

        BlockDictionary.registerOre("create:powersFurnaceEngine", Blocks.LIT_FURNACE, true);

        registerOreBlockItem("oreCopper", BlockInit.ORE, 0);
        registerOreBlockItem("oreZinc", BlockInit.ORE, 1);

        registerOreBlockItem("blockCopper", BlockInit.METAL, 1);
        registerOreBlockItem("blockZinc", BlockInit.METAL, 2);
        registerOreBlockItem("blockBrass", BlockInit.METAL, 3);

        for (int i = 0; i < 4; i++) {
            registerOreBlockItem("blockGlass", BlockInit.FRAMED_GLASS, i);
            registerOreBlockItem("blockGlassColorless", BlockInit.FRAMED_GLASS, i);
        }

        registerOre("terracotta", Item.getItemFromBlock(Blocks.HARDENED_CLAY), 0);
        registerOre("terracotta", Item.getItemFromBlock(Blocks.STAINED_HARDENED_CLAY), OreDictionary.WILDCARD_VALUE);

        registerOre("dustWheat", ItemInit.INGREDIENT, 0);
        registerOre("dough", ItemInit.INGREDIENT, 1);
        registerOre("dustNetherrack", ItemInit.INGREDIENT, 2);
        registerOre("gemRoseQuartz", ItemInit.INGREDIENT, 3);
        registerOre("gemPolishedRoseQuartz", ItemInit.INGREDIENT, 4);
        registerOre("dustObsidian", ItemInit.INGREDIENT, 5);
        registerOre("plateObsidian", ItemInit.INGREDIENT, 6);
        registerOre("propeller", ItemInit.INGREDIENT, 7);
        registerOre("whisk", ItemInit.INGREDIENT, 8);
        registerOre("electronTube", ItemInit.INGREDIENT, 10);
        registerOre("cardboard", ItemInit.INGREDIENT, 13);
        registerOre("ingotCopper", ItemInit.INGREDIENT, 16);
        registerOre("ingotZinc", ItemInit.INGREDIENT, 17);
        registerOre("ingotBrass", ItemInit.INGREDIENT, 18);
        registerOre("nuggetCopper", ItemInit.INGREDIENT, 19);
        registerOre("nuggetZinc", ItemInit.INGREDIENT, 20);
        registerOre("nuggetBrass", ItemInit.INGREDIENT, 21);
        registerOre("plateCopper", ItemInit.INGREDIENT, 22);
        registerOre("plateBrass", ItemInit.INGREDIENT, 23);
        registerOre("plateIron", ItemInit.INGREDIENT, 24);
        registerOre("plateGold", ItemInit.INGREDIENT, 25);
        registerOre("crushedIron", ItemInit.INGREDIENT, 26);
        registerOre("crushedGold", ItemInit.INGREDIENT, 27);
        registerOre("crushedCopper", ItemInit.INGREDIENT, 28);
        registerOre("crushedZinc", ItemInit.INGREDIENT, 29);
        registerOre("create:blazecake", ItemInit.INGREDIENT, 31);
    }

    private static void registerOre(String ore, Item item, int meta) {
        OreDictionary.registerOre(ore, new ItemStack(item, 1, meta));
    }
    private static void registerOre(String ore, Block block, int meta) {
        BlockDictionary.registerOre(ore, MetaBlock.of(block, meta));
    }

    private static void registerOreBlockItem(String ore, Block block, int meta) {
        registerOre(ore, block, meta);
        registerOre(ore, Item.getItemFromBlock(block), meta);
    }

    public static final HashSet<String> INGOT_TYPES = new HashSet<>();
    public static final HashSet<String> PLATE_TYPES = new HashSet<>();

    public static void scanMetalTypes() {
        for (String ore : OreDictionary.getOreNames()) {
            if (ore.startsWith("ingot") && ore.length() > "ingot".length()) {
                INGOT_TYPES.add(ore.substring("ingot".length()));
            } else if (ore.startsWith("plate") && ore.length() > "plate".length()) {
                PLATE_TYPES.add(ore.substring("plate".length()));
            }
        }
    }

    private OreDictInit() {
        throw new AssertionError("no");
    }
}
