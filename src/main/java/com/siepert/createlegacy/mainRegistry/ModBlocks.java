package com.siepert.createlegacy.mainRegistry;

import com.siepert.createlegacy.blocks.BlockCasing;
import com.siepert.createlegacy.blocks.BlockMaterialStorage;
import com.siepert.createlegacy.blocks.BlockOre;
import com.siepert.createlegacy.blocks.BlockStone;
import com.siepert.createlegacy.blocks.decoration.*;
import com.siepert.createlegacy.blocks.kinetic.*;
import com.siepert.createlegacy.blocks.logic.BlockScheduleCook;
import com.siepert.createlegacy.blocks.logic.BlockScheduleWash;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

/** The main block registry class.
 * Pretty self-explanatory.
 * Apparently registered after the items. */
public class ModBlocks {
    public static final List<Block> BLOCKS = new ArrayList<Block>();

    public static final Block ORE = new BlockOre();
    public static final Block MATERIAL_STORAGE_BLOCK = new BlockMaterialStorage();
    public static final Block CASING_BLOCK = new BlockCasing();
    public static final Block AXLE = new BlockAxle("axle");
    public static final Block COGWHEEL = new BlockCogwheel("cogwheel");
    public static final Block HAND_CRANK = new BlockHandCrank("hand_crank");
    public static final Block WATER_WHEEL = new BlockWaterWheel("water_wheel");
    public static final Block FURNACE_ENGINE = new BlockFurnaceEngine();
    public static final Block CREATIVE_MOTOR = new BlockCreativeMotor("creative_motor");
    public static final Block CHASSIS = new BlockChassis("chassis");
    public static final Block KINETIC_UTILITY = new BlockKineticUtility();
    public static final Block ITEM_HOLDER = new BlockItemHolder("item_holder");
    public static final Block BLAZE_BURNER = new BlockBlazeBurner("blaze_burner");

    public static final Block BELT = new BlockBelt("belt");
    public static final Block CHUTE = new BlockChute("chute");
    public static final Block PRESS = new BlockMechanicalPress("press");
    public static final Block DRILL = new BlockDrill("drill");
    public static final Block SAW = new BlockSaw("saw");
    public static final Block FAN = new BlockFan("fan");
    public static final Block MILLSTONE = new BlockMillStone();
    public static final Block PISTON = new BlockMechanicalPiston("piston");
    public static final Block PISTON_ERECTOR = new BlockPistonErector("piston_pole");

    public static final Block SCHEDULE_WASH = new BlockScheduleWash();
    public static final Block SCHEDULE_COOK = new BlockScheduleCook();


    public static final Block STONE = new BlockStone(true);
    public static final Block STONE_POLISHED = new BlockStonePolished();
    public static final Block STONE_CUT = new BlockStoneCut();
    public static final Block STONE_LAYERED = new BlockStoneLayered();
    public static final Block STONE_BRICKS = new BlockStoneBricks();
    public static final Block STONE_BRICKS_FANCY = new BlockStoneBricksFancy();
    public static final Block STONE_PILLAR = new BlockStonePillar();
    public static final Block STONE_REINFORCED = new BlockStoneReinforced();
}
