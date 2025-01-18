package com.melonstudios.createlegacy.util.registries;

import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.block.stone.AbstractBlockOrestone;
import com.melonstudios.createlegacy.item.ModItems;
import com.melonstudios.createlegacy.util.DisplayLink;
import com.melonstudios.melonlib.blockdict.BlockDictionary;
import net.minecraft.block.BlockHopper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Adds items to the Ore Dictionary.
 * @since 0.1.0
 */
public final class OreDictHandler {
    private static boolean initialized = false;
    public static void init() {
        if (initialized) return;
        long startTime = System.currentTimeMillis();
        DisplayLink.debug("Starting Ore Dictionary init");

        OreDictionary.registerOre("dustWheat", new ItemStack(ModItems.FOOD, 1, 8));

        OreDictionary.registerOre("ingotCopper", new ItemStack(ModItems.INGREDIENT, 1, 3));
        OreDictionary.registerOre("ingotZinc", new ItemStack(ModItems.INGREDIENT, 1, 6));
        OreDictionary.registerOre("ingotBrass", new ItemStack(ModItems.INGREDIENT, 1, 9));

        OreDictionary.registerOre("nuggetCopper", new ItemStack(ModItems.INGREDIENT, 1, 4));
        OreDictionary.registerOre("nuggetZinc", new ItemStack(ModItems.INGREDIENT, 1, 7));
        OreDictionary.registerOre("nuggetBrass", new ItemStack(ModItems.INGREDIENT, 1, 10));

        OreDictionary.registerOre("plateIron", new ItemStack(ModItems.INGREDIENT, 1, 1));
        OreDictionary.registerOre("plateGold", new ItemStack(ModItems.INGREDIENT, 1, 2));
        OreDictionary.registerOre("plateCopper", new ItemStack(ModItems.INGREDIENT, 1, 5));
        OreDictionary.registerOre("plateZinc", new ItemStack(ModItems.INGREDIENT, 1, 8));
        OreDictionary.registerOre("plateBrass", new ItemStack(ModItems.INGREDIENT, 1, 11));
        OreDictionary.registerOre("plateObsidian", new ItemStack(ModItems.INGREDIENT, 1, 18));

        OreDictionary.registerOre("oreCopper", new ItemStack(ModBlocks.ORE, 1, 0));
        OreDictionary.registerOre("oreZinc", new ItemStack(ModBlocks.ORE, 1, 1));

        OreDictionary.registerOre("blockCopper", new ItemStack(ModBlocks.METAL, 1, 1));
        OreDictionary.registerOre("blockZinc", new ItemStack(ModBlocks.METAL, 1, 2));
        OreDictionary.registerOre("blockBrass", new ItemStack(ModBlocks.METAL, 1, 3));

        OreDictionary.registerOre("crushedIron", new ItemStack(ModItems.INGREDIENT, 1, 12));
        OreDictionary.registerOre("crushedGold", new ItemStack(ModItems.INGREDIENT, 1, 13));
        OreDictionary.registerOre("crushedCopper", new ItemStack(ModItems.INGREDIENT, 1, 14));
        OreDictionary.registerOre("crushedZinc", new ItemStack(ModItems.INGREDIENT, 1, 15));

        OreDictionary.registerOre("dustNetherrack", new ItemStack(ModItems.INGREDIENT, 1, 16));
        OreDictionary.registerOre("dustObsidian", new ItemStack(ModItems.INGREDIENT, 1, 17));

        OreDictionary.registerOre("gemRoseQuartz", new ItemStack(ModItems.INGREDIENT, 1, 19));
        OreDictionary.registerOre("gemRoseQuartzPolished", new ItemStack(ModItems.INGREDIENT, 1, 20));

        OreDictionary.registerOre("propeller", new ItemStack(ModItems.INGREDIENT, 1, 23));
        OreDictionary.registerOre("whisk", new ItemStack(ModItems.INGREDIENT, 1, 24));
        OreDictionary.registerOre("electronTube", new ItemStack(ModItems.INGREDIENT, 1, 25));
        OreDictionary.registerOre("handBrass", new ItemStack(ModItems.INGREDIENT, 1, 26));

        addOrestoneTags(ModBlocks.ORESTONE);
        addOrestoneTags(ModBlocks.ORESTONE_POLISHED);
        addOrestoneTags(ModBlocks.ORESTONE_BRICKS);
        addOrestoneTags(ModBlocks.ORESTONE_BRICKS_FANCY);
        addOrestoneTags(ModBlocks.ORESTONE_PILLAR_Y);
        addOrestoneTags(ModBlocks.ORESTONE_LAYERED);

        OreDictionary.registerOre("blockGlass", new ItemStack(ModBlocks.INDUSTRIAL_IRON_GLASS, 1));
        OreDictionary.registerOre("blockGlass", new ItemStack(ModBlocks.FRAMED_GLASS, 1, 0));
        OreDictionary.registerOre("blockGlass", new ItemStack(ModBlocks.FRAMED_GLASS, 1, 1));
        OreDictionary.registerOre("blockGlass", new ItemStack(ModBlocks.FRAMED_GLASS, 1, 2));
        OreDictionary.registerOre("blockGlass", new ItemStack(ModBlocks.FRAMED_GLASS, 1, 3));
        OreDictionary.registerOre("blockGlassColorless", new ItemStack(ModBlocks.FRAMED_GLASS, 1, 0));
        OreDictionary.registerOre("blockGlassColorless", new ItemStack(ModBlocks.FRAMED_GLASS, 1, 1));
        OreDictionary.registerOre("blockGlassColorless", new ItemStack(ModBlocks.FRAMED_GLASS, 1, 2));
        OreDictionary.registerOre("blockGlassColorless", new ItemStack(ModBlocks.FRAMED_GLASS, 1, 3));
        OreDictionary.registerOre("paneGlass", new ItemStack(ModBlocks.FRAMED_GLASS_PANE, 1, 0));
        OreDictionary.registerOre("paneGlass", new ItemStack(ModBlocks.FRAMED_GLASS_PANE, 1, 1));
        OreDictionary.registerOre("paneGlass", new ItemStack(ModBlocks.FRAMED_GLASS_PANE, 1, 2));
        OreDictionary.registerOre("paneGlass", new ItemStack(ModBlocks.FRAMED_GLASS_PANE, 1, 3));
        OreDictionary.registerOre("paneGlassColorless", new ItemStack(ModBlocks.FRAMED_GLASS_PANE, 1, 0));
        OreDictionary.registerOre("paneGlassColorless", new ItemStack(ModBlocks.FRAMED_GLASS_PANE, 1, 1));
        OreDictionary.registerOre("paneGlassColorless", new ItemStack(ModBlocks.FRAMED_GLASS_PANE, 1, 2));
        OreDictionary.registerOre("paneGlassColorless", new ItemStack(ModBlocks.FRAMED_GLASS_PANE, 1, 3));

        OreDictionary.registerOre("create:unprocessedItem", new ItemStack(ModItems.INGREDIENT, 1, 28));
        OreDictionary.registerOre("create:unprocessedItem", new ItemStack(ModItems.INGREDIENT, 1, 29));

        OreDictionary.registerOre("create:blazeBurnerSuperheat", new ItemStack(ModItems.FOOD, 1, 1));

        DisplayLink.debug("Ore Dictionary init done in %s ms!", System.currentTimeMillis() - startTime);

        long startTime2 = System.currentTimeMillis();

        BlockDictionary.registerOre("create:wrenchPickup", Blocks.PISTON, 6);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.STICKY_PISTON, 6);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.LEVER, 12);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.OBSERVER, 12);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.DISPENSER, 6);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.DROPPER, 6);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.WOODEN_BUTTON, 12);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.STONE_BUTTON, 12);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, 2);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, 2);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.WOODEN_PRESSURE_PLATE, 2);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.STONE_PRESSURE_PLATE, 2);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.REDSTONE_TORCH, 10);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.REDSTONE_LAMP, false);
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (facing == EnumFacing.UP) continue;
            BlockDictionary.registerOre("create:wrenchPickup", Blocks.HOPPER.getDefaultState()
                    .withProperty(BlockHopper.FACING, facing).withProperty(BlockHopper.ENABLED, false));
            BlockDictionary.registerOre("create:wrenchPickup", Blocks.HOPPER.getDefaultState()
                    .withProperty(BlockHopper.FACING, facing).withProperty(BlockHopper.ENABLED, true));
        }
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.UNPOWERED_REPEATER, true);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.POWERED_REPEATER, true);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.UNPOWERED_COMPARATOR, 8);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.POWERED_COMPARATOR, 8);

        BlockDictionary.registerOre("create:wrenchPickup", ModBlocks.BEARING, 12);
        BlockDictionary.registerOre("create:wrenchPickup", ModBlocks.ROTATOR, 9);
        BlockDictionary.registerOre("create:wrenchPickup", ModBlocks.WATER_WHEEL, 2);
        BlockDictionary.registerOre("create:wrenchPickup", ModBlocks.NETWORK_INSPECTOR, 4);
        BlockDictionary.registerOre("create:wrenchPickup", ModBlocks.SHAFT_ENCASED, 6);
        BlockDictionary.registerOre("create:wrenchPickup", ModBlocks.CASING, 4);
        BlockDictionary.registerOre("create:wrenchPickup", ModBlocks.BLAZE_BURNER, 2);
        BlockDictionary.registerOre("create:wrenchPickup", ModBlocks.BLAZE_BURNER_LIT, false);
        BlockDictionary.registerOre("create:wrenchPickup", ModBlocks.TURNTABLE, false);
        BlockDictionary.registerOre("create:wrenchPickup", ModBlocks.HAND_CRANK, 6);
        BlockDictionary.registerOre("create:wrenchPickup", ModBlocks.CREATIVE_MOTOR, 6);
        BlockDictionary.registerOre("create:wrenchPickup", ModBlocks.FURNACE_ENGINE, 8);
        BlockDictionary.registerOre("create:wrenchPickup", ModBlocks.PRESS, 2);
        BlockDictionary.registerOre("create:wrenchPickup", ModBlocks.DEPOT, false);
        BlockDictionary.registerOre("create:wrenchPickup", ModBlocks.MILLSTONE, false);
        BlockDictionary.registerOre("create:wrenchPickup", ModBlocks.FAN, 6);
        BlockDictionary.registerOre("create:wrenchPickup", ModBlocks.DRILL, 6);
        BlockDictionary.registerOre("create:wrenchPickup", ModBlocks.KINETIC_UTILITY, 6);
        BlockDictionary.registerOre("create:wrenchPickup", ModBlocks.GEARBOX, 3);
        BlockDictionary.registerOre("create:wrenchPickup", ModBlocks.CHUTE, false);
        BlockDictionary.registerOre("create:wrenchPickup", ModBlocks.CHIGWANKER, false);
        BlockDictionary.registerOre("create:wrenchPickup", ModBlocks.SAW, false);
        BlockDictionary.registerOre("create:wrenchPickup", ModBlocks.INDUSTRIAL_IRON, false);
        BlockDictionary.registerOre("create:wrenchPickup", ModBlocks.INDUSTRIAL_IRON_GLASS, false);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.NOTEBLOCK, false);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.TRIPWIRE_HOOK, true);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.DAYLIGHT_DETECTOR_INVERTED, true);
        BlockDictionary.registerOre("create:wrenchPickup", Blocks.DAYLIGHT_DETECTOR, true);
        BlockDictionary.registerOre("create:wrenchPickup", ModBlocks.FUNNEL, 8);

        BlockDictionary.registerOre("create:fanPass", Blocks.IRON_BARS, false);
        BlockDictionary.registerOre("create:fanPass", ModBlocks.BLAZE_BURNER, 2);
        BlockDictionary.registerOre("create:fanPass", ModBlocks.BLAZE_BURNER_LIT, false);
        BlockDictionary.registerOre("create:fanPass", ModBlocks.FUNNEL, 8);
        BlockDictionary.registerOre("create:fanPass", ModBlocks.ROTATOR, 3);
        BlockDictionary.registerOre("create:fanPass", ModBlocks.TURNTABLE, false);
        BlockDictionary.registerOre("create:fanPass", Blocks.END_ROD, 6);
        BlockDictionary.registerOre("create:fanPass", ModBlocks.HAND_CRANK, 6);

        DisplayLink.debug("Block Dictionary init done in %s ms!", System.currentTimeMillis() - startTime2);
        initialized = true;
    }

    private static void addOrestoneTags(AbstractBlockOrestone orestone) {
        OreDictionary.registerOre("stoneAsurine", new ItemStack(orestone, 1, 0));
        OreDictionary.registerOre("stoneCrimsite", new ItemStack(orestone, 1, 1));
        OreDictionary.registerOre("stoneLimestone", new ItemStack(orestone, 1, 2));
        OreDictionary.registerOre("stoneOchrum", new ItemStack(orestone, 1, 3));
        OreDictionary.registerOre("stoneScorchia", new ItemStack(orestone, 1, 4));
        OreDictionary.registerOre("stoneScoria", new ItemStack(orestone, 1, 5));
        OreDictionary.registerOre("stoneVeridium", new ItemStack(orestone, 1, 6));
    }
}
