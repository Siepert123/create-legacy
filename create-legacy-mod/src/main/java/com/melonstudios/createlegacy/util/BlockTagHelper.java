package com.melonstudios.createlegacy.util;

import com.melonstudios.createlegacy.block.BlockCasing;
import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.block.kinetic.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Simulates ore dict for blocks.
 *
 * @author Siepert123
 */
public final class BlockTagHelper {
    private BlockTagHelper() {}

    /**
     * @param state The blockstate to get the item from.
     * @return The item as if you middle-clicked it.
     */
    public static ItemStack getItem(IBlockState state) {
        try {
            return state.getBlock().getPickBlock(state, null, null, null, null);  //highly corrosive
        } catch (NullPointerException e) {
            return new ItemStack(Item.getItemFromBlock(state.getBlock()),
                    1, state.getBlock().damageDropped(state));
        }
    }

    /**
     * @param state The Blockstate to check
     * @param tag The OreDict tag to check for
     * @return Whether the block contains said OreDict tag.
     */
    public static boolean hasTag(IBlockState state, String tag) {
        ItemStack stack = getItem(state);

        for (ItemStack tagged : OreDictionary.getOres(tag)) {
            if (tagged.isItemEqual(stack)) return true;
        }
        return false;
    }

    /**
     * Adds an OreDict tag to a blockstate.
     * @param state The state to tag.
     * @param tag Tag to add to the blockstate.
     */
    public static void addTag(IBlockState state, String tag) {
        ItemStack stack = getItem(state);

        OreDictionary.registerOre(tag, stack);
    }

    //OreDict event
    public static void addBlockTags() {
        addTag(Blocks.PISTON.getDefaultState(), "create:wrenchPickup");
        addTag(Blocks.STICKY_PISTON.getDefaultState(), "create:wrenchPickup");
        addTag(Blocks.LEVER.getDefaultState(), "create:wrenchPickup");
        addTag(Blocks.OBSERVER.getDefaultState(), "create:wrenchPickup");
        addTag(Blocks.DISPENSER.getDefaultState(), "create:wrenchPickup");
        addTag(Blocks.DROPPER.getDefaultState(), "create:wrenchPickup");
        addTag(Blocks.WOODEN_BUTTON.getDefaultState(), "create:wrenchPickup");
        addTag(Blocks.STONE_BUTTON.getDefaultState(), "create:wrenchPickup");
        addTag(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE.getDefaultState(), "create:wrenchPickup");
        addTag(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE.getDefaultState(), "create:wrenchPickup");
        addTag(Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), "create:wrenchPickup");
        addTag(Blocks.STONE_PRESSURE_PLATE.getDefaultState(), "create:wrenchPickup");
        addTag(Blocks.REDSTONE_TORCH.getDefaultState(), "create:wrenchPickup");
        addTag(Blocks.REDSTONE_LAMP.getDefaultState(), "create:wrenchPickup");
        addTag(Blocks.HOPPER.getDefaultState(), "create:wrenchPickup");
        addTag(Blocks.UNPOWERED_REPEATER.getDefaultState(), "create:wrenchPickup");
        addTag(Blocks.POWERED_REPEATER.getDefaultState(), "create:wrenchPickup");
        addTag(Blocks.UNPOWERED_COMPARATOR.getDefaultState(), "create:wrenchPickup");
        addTag(Blocks.POWERED_COMPARATOR.getDefaultState(), "create:wrenchPickup");

        addTag(ModBlocks.BEARING.getDefaultState(), "create:wrenchPickup");
        addTag(ModBlocks.ROTATOR.getDefaultState()
                .withProperty(BlockRotator.VARIANT, BlockRotator.Variant.SHAFT), "create:wrenchPickup");
        addTag(ModBlocks.ROTATOR.getDefaultState()
                .withProperty(BlockRotator.VARIANT, BlockRotator.Variant.COG), "create:wrenchPickup");
        addTag(ModBlocks.WATER_WHEEL.getDefaultState(), "create:wrenchPickup");
        addTag(ModBlocks.NETWORK_INSPECTOR.getDefaultState()
                .withProperty(BlockNetworkInspector.ALT, false), "create:wrenchPickup");
        addTag(ModBlocks.NETWORK_INSPECTOR.getDefaultState()
                .withProperty(BlockNetworkInspector.ALT, true), "create:wrenchPickup");
        addTag(ModBlocks.SHAFT_ENCASED.getDefaultState()
                .withProperty(BlockEncasedShaft.BRASS, false), "create:wrenchPickup");
        addTag(ModBlocks.SHAFT_ENCASED.getDefaultState()
                .withProperty(BlockEncasedShaft.BRASS, true), "create:wrenchPickup");
        addTag(ModBlocks.CASING.getDefaultState()
                .withProperty(BlockCasing.VARIANT, BlockCasing.Variant.ANDESITE), "create:wrenchPickup");
        addTag(ModBlocks.CASING.getDefaultState()
                .withProperty(BlockCasing.VARIANT, BlockCasing.Variant.COPPER), "create:wrenchPickup");
        addTag(ModBlocks.CASING.getDefaultState()
                .withProperty(BlockCasing.VARIANT, BlockCasing.Variant.BRASS), "create:wrenchPickup");
        addTag(ModBlocks.CASING.getDefaultState()
                .withProperty(BlockCasing.VARIANT, BlockCasing.Variant.TRAIN), "create:wrenchPickup");
        addTag(ModBlocks.HAND_CRANK.getDefaultState(), "create:wrenchPickup");
        addTag(ModBlocks.CREATIVE_MOTOR.getDefaultState(), "create:wrenchPickup");
        addTag(ModBlocks.FURNACE_ENGINE.getDefaultState()
                .withProperty(BlockFurnaceEngine.VARIANT, BlockFurnaceEngine.Variant.ENGINE), "create:wrenchPickup");
        addTag(ModBlocks.FURNACE_ENGINE.getDefaultState()
                .withProperty(BlockFurnaceEngine.VARIANT, BlockFurnaceEngine.Variant.FLYWHEEL), "create:wrenchPickup");
        addTag(ModBlocks.PRESS.getDefaultState(), "create:wrenchPickup");
        addTag(ModBlocks.DEPOT.getDefaultState(), "create:wrenchPickup");
        addTag(ModBlocks.MILLSTONE.getDefaultState(), "create:wrenchPickup");
        addTag(ModBlocks.FAN.getDefaultState(), "create:wrenchPickup");
        addTag(ModBlocks.DRILL.getDefaultState(), "create:wrenchPickup");
        addTag(ModBlocks.KINETIC_UTILITY.getDefaultState()
                .withProperty(BlockKineticUtility.SHIFT, false), "create:wrenchPickup");
        addTag(ModBlocks.KINETIC_UTILITY.getDefaultState()
                .withProperty(BlockKineticUtility.SHIFT, true), "create:wrenchPickup");
        addTag(ModBlocks.GEARBOX.getDefaultState(), "create:wrenchPickup");
        addTag(ModBlocks.CHUTE.getDefaultState(), "create:wrenchPickup");
        addTag(ModBlocks.CHIGWANKER.getDefaultState(), "create:wrenchPickup");
        addTag(ModBlocks.SAW.getDefaultState(), "create:wrenchPickup");
        addTag(ModBlocks.INDUSTRIAL_IRON.getDefaultState(), "create:wrenchPickup");
        addTag(ModBlocks.INDUSTRIAL_IRON_GLASS.getDefaultState(), "create:wrenchPickup");
        addTag(Blocks.NOTEBLOCK.getDefaultState(), "create:wrenchPickup");
        addTag(Blocks.TRIPWIRE_HOOK.getDefaultState(), "create:wrenchPickup");
        addTag(Blocks.DAYLIGHT_DETECTOR_INVERTED.getDefaultState(), "create:wrenchPickup");
        addTag(Blocks.DAYLIGHT_DETECTOR.getDefaultState(), "create:wrenchPickup");
    }

    //Overloaded method
    public static boolean isWood(IBlockState state) {
        return hasTag(state, "logWood");
    }
    //Overloaded method
    public static boolean isLeaves(IBlockState state) {
        return hasTag(state, "treeLeaves");
    }
}
