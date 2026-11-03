package nl.melonstudios.create.init;

import com.melonstudios.melonlib.item.ItemBlockVariants;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.registries.GameData;
import nl.melonstudios.create.CreateLegacy;
import nl.melonstudios.create.block.*;
import nl.melonstudios.create.block.actor.*;
import nl.melonstudios.create.block.deco.*;
import nl.melonstudios.create.block.funnel.BlockFunnelWall;
import nl.melonstudios.create.block.generator.*;
import nl.melonstudios.create.block.redstone.BlockRedstoneLatch;
import nl.melonstudios.create.block.redstone.BlockRedstoneLinker;
import nl.melonstudios.create.block.redstone.BlockRedstoneToggleLatch;
import nl.melonstudios.create.item.ItemBlockBlazeBurner;
import nl.melonstudios.create.item.ItemBlockDepotActor;
import nl.melonstudios.create.item.ItemBlockSail;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Function;

public final class BlockInit {
    public static void load() {}

    public static final ArrayList<Block> BLOCKS = new ArrayList<>();

    public static final BlockRender RENDER = registerBlock(new BlockRender());

    public static final BlockOre ORE = registerBlockWithItem(new BlockOre(), true);
    public static final BlockMetal METAL = registerBlockWithItem(BlockMetal.get(), true);

    //region Kinetics
    public static final BlockCasing CASING = registerBlockWithItem(new BlockCasing(), true);

    public static final BlockShaft SHAFT = registerBlockWithItem(new BlockShaft(Material.ROCK, MapColor.STONE));
    public static final BlockCogwheel COG_SMALL = registerBlockWithItem(new BlockCogwheel(MapColor.WOOD, SoundType.WOOD, false));
    public static final BlockCogwheel COG_LARGE = registerBlockWithItem(new BlockCogwheel(MapColor.WOOD, SoundType.WOOD, true));

    public static final BlockGearbox GEARBOX = registerBlockWithItem(new BlockGearbox(MapColor.WOOD, SoundType.WOOD), true);
    public static final BlockGearshift GEARSHIFT = (BlockGearshift)
            registerBlockWithItem(new BlockGearshift(MapColor.WOOD, SoundType.WOOD)
            .setRegistryName("gearshift").setUnlocalizedName("create.gearshift"));
    public static final BlockClutch CLUTCH = (BlockClutch)
            registerBlockWithItem(new BlockClutch(MapColor.WOOD, SoundType.WOOD)
            .setRegistryName("clutch").setUnlocalizedName("create.clutch"));

    public static final BlockHandCrank HAND_CRANK = registerBlockWithItem(new BlockHandCrank(MapColor.WOOD, SoundType.WOOD));
    public static final BlockWaterWheel WATER_WHEEL = registerBlockWithItem(new BlockWaterWheel(MapColor.WOOD, SoundType.WOOD));
    public static final BlockFurnaceEngine FURNACE_ENGINE = (BlockFurnaceEngine)
            registerBlockWithItem(new BlockFurnaceEngine()
            .setRegistryName("furnace_engine").setUnlocalizedName("create.furnace_engine"), true);
    public static final BlockCreativeMotor CREATIVE_MOTOR = (BlockCreativeMotor)
            registerBlockWithItem(new BlockCreativeMotor()
            .setRegistryName("creative_motor").setUnlocalizedName("create.creative_motor"));

    public static final BlockTurntable TURNTABLE = (BlockTurntable)
            registerBlockWithItem(new BlockTurntable(MapColor.WOOD, SoundType.WOOD)
            .setRegistryName("turntable").setUnlocalizedName("create.turntable"));
    public static final BlockBearing BEARING = (BlockBearing)
            registerBlockWithItem(new BlockBearing()
            .setRegistryName("bearing").setUnlocalizedName("create.bearing"));
    public static final BlockBearingWindmill BEARING_WINDMILL = (BlockBearingWindmill)
            registerBlockWithItem(new BlockBearingWindmill()
                    .setRegistryName("bearing_windmill").setUnlocalizedName("create.bearing_windmill"));

    public static final BlockChassisLinear CHASSIS_LINEAR = (BlockChassisLinear)
            registerBlockWithItem(new BlockChassisLinear()
            .setRegistryName("chassis_linear").setUnlocalizedName("create.chassis_linear"), true);
    public static final BlockChassisRadial CHASSIS_RADIAL = (BlockChassisRadial)
            registerBlockWithItem(new BlockChassisRadial()
            .setRegistryName("chassis_radial").setUnlocalizedName("create.chassis_radial"));

    public static final BlockSail SAIL_DOWN = registerBlock(new BlockSail(EnumFacing.DOWN));
    public static final BlockSail SAIL_UP = registerBlock(new BlockSail(EnumFacing.UP));
    public static final BlockSail SAIL_NORTH = registerBlock(new BlockSail(EnumFacing.NORTH));
    public static final BlockSail SAIL_SOUTH = registerBlock(new BlockSail(EnumFacing.SOUTH));
    public static final BlockSail SAIL_WEST = registerBlock(new BlockSail(EnumFacing.WEST));
    public static final BlockSail SAIL_EAST = registerBlock(new BlockSail(EnumFacing.EAST));
    //there is probably a better way to do this... oh well I'm not rewriting it all again
    public static final ItemBlockSail SAIL_ITEM = new ItemBlockSail(SAIL_DOWN);
    static {
        ItemInit.ITEMS.add(SAIL_ITEM);
        GameData.getBlockItemMap().forcePut(SAIL_DOWN, SAIL_ITEM);
        GameData.getBlockItemMap().forcePut(SAIL_UP, SAIL_ITEM);
        GameData.getBlockItemMap().forcePut(SAIL_NORTH, SAIL_ITEM);
        GameData.getBlockItemMap().forcePut(SAIL_SOUTH, SAIL_ITEM);
        GameData.getBlockItemMap().forcePut(SAIL_WEST, SAIL_ITEM);
        GameData.getBlockItemMap().forcePut(SAIL_EAST, SAIL_ITEM);
    }

    public static final BlockPistonPole PISTON_POLE = (BlockPistonPole)
            registerBlockWithItem(new BlockPistonPole()
            .setRegistryName("piston_pole").setUnlocalizedName("create.piston_pole"));
    public static final BlockMechanicalPistonHead PISTON_HEAD = (BlockMechanicalPistonHead)
            registerBlock(new BlockMechanicalPistonHead()
            .setRegistryName("piston_head").setUnlocalizedName("create.piston_head"));

    public static final BlockMechanicalPiston MECHANICAL_PISTON = (BlockMechanicalPiston)
            registerBlockWithItem(new BlockMechanicalPiston(false, false)
            .setRegistryName("mechanical_piston").setUnlocalizedName("create.mechanical_piston"));
    public static final BlockMechanicalPiston MECHANICAL_PISTON_STICKY = (BlockMechanicalPiston)
            registerBlockWithItem(new BlockMechanicalPiston(true, false)
            .setRegistryName("mechanical_piston_sticky").setUnlocalizedName("create.mechanical_piston_sticky"));
    public static final BlockMechanicalPiston MECHANICAL_PISTON_EXTENDED = (BlockMechanicalPiston)
            registerBlock(new BlockMechanicalPiston(false, true)
            .setRegistryName("mechanical_piston_extended").setUnlocalizedName("create.mechanical_piston"));
    public static final BlockMechanicalPiston MECHANICAL_PISTON_STICKY_EXTENDED = (BlockMechanicalPiston)
            registerBlock(new BlockMechanicalPiston(true, true)
            .setRegistryName("mechanical_piston_sticky_extended").setUnlocalizedName("create.mechanical_piston_sticky"));

    public static final BlockGauge SPEEDOMETER = (BlockGauge)
            registerBlockWithItem(new BlockGauge(MapColor.WOOD, SoundType.WOOD, BlockGauge.Type.SPEED)
            .setRegistryName("speedometer").setUnlocalizedName("create.speedometer"));
    public static final BlockGauge STRESSOMETER = (BlockGauge)
            registerBlockWithItem(new BlockGauge(MapColor.WOOD, SoundType.WOOD, BlockGauge.Type.STRESS)
            .setRegistryName("stressometer").setUnlocalizedName("create.stressometer"));

    public static final BlockPress PRESS = (BlockPress)
            registerBlockWithItem(new BlockPress(MapColor.WOOD, SoundType.METAL)
            .setRegistryName("press").setUnlocalizedName("create.press"),
            ItemBlockDepotActor::new);
    public static final BlockMixer MIXER = (BlockMixer)
            registerBlockWithItem(new BlockMixer()
            .setRegistryName("mixer").setUnlocalizedName("create.mixer"),
            ItemBlockDepotActor::new);

    public static final BlockDrill DRILL = (BlockDrill)
            registerBlockWithItem(new BlockDrill(MapColor.STONE, SoundType.METAL)
            .setRegistryName("drill").setUnlocalizedName("create.drill"));
    public static final BlockSaw SAW = (BlockSaw)
            registerBlockWithItem(new BlockSaw(MapColor.IRON, SoundType.METAL)
            .setRegistryName("saw").setUnlocalizedName("create.saw"));
    public static final BlockDeployer DEPLOYER = (BlockDeployer)
            registerBlockWithItem(new BlockDeployer(MapColor.STONE, SoundType.WOOD)
            .setRegistryName("deployer").setUnlocalizedName("create.deployer"),
            ItemBlockDepotActor::new);

    public static final BlockAutoFarm AUTO_FARM = (BlockAutoFarm)
            registerBlockWithItem(new BlockAutoFarm()
            .setRegistryName("auto_farm").setUnlocalizedName("create.auto_farm"), true);
    public static final BlockContraptionInterface CONTRAPTION_INTERFACE = (BlockContraptionInterface)
            registerBlockWithItem(new BlockContraptionInterface()
            .setRegistryName("contraption_interface").setUnlocalizedName("create.contraption_interface"), true);

    public static final BlockMillstone MILLSTONE = (BlockMillstone)
            registerBlockWithItem(new BlockMillstone(Material.ROCK, MapColor.STONE)
            .setRegistryName("millstone").setUnlocalizedName("create.millstone"));

    public static final BlockCrafter CRAFTER = (BlockCrafter)
            registerBlockWithItem(new BlockCrafter()
            .setRegistryName("crafter").setUnlocalizedName("create.crafter"));

    public static final BlockBlazeBurner BLAZE_BURNER = (BlockBlazeBurner)
            registerBlockWithItem(new BlockBlazeBurner()
            .setRegistryName("blaze_burner").setUnlocalizedName("create.blaze_burner"),
            ItemBlockBlazeBurner::new);

    public static final BlockDepot DEPOT = (BlockDepot)
            registerBlockWithItem(new BlockDepot()
            .setRegistryName("depot").setUnlocalizedName("create.depot"));
    public static final BlockBasin BASIN = (BlockBasin)
            registerBlockWithItem(new BlockBasin()
            .setRegistryName("basin").setUnlocalizedName("create.basin"));
    public static final BlockChute CHUTE = (BlockChute)
            registerBlockWithItem(new BlockChute()
            .setRegistryName("chute").setUnlocalizedName("create.chute"));

    public static final BlockBeltStraight BELT_STRAIGHT = (BlockBeltStraight)
            registerBlock(new BlockBeltStraight()
            .setRegistryName("belt_straight").setUnlocalizedName("create.belt"));

    public static final BlockItemDrain ITEM_DRAIN = (BlockItemDrain)
            registerBlockWithItem(new BlockItemDrain()
            .setRegistryName("item_drain").setUnlocalizedName("create.item_drain"));

    public static final BlockFunnelWall FUNNEL_ANDESITE_WALL = (BlockFunnelWall)
            registerBlock(new BlockFunnelWall("andesite", false)
            .setRegistryName("funnel_andesite_wall").setUnlocalizedName("create.funnel_andesite"));
    public static final BlockFunnelWall FUNNEL_BRASS_WALL = (BlockFunnelWall)
            registerBlock(new BlockFunnelWall("brass", true)
            .setRegistryName("funnel_brass_wall").setUnlocalizedName("create.funnel_brass"));
    //endregion

    //region Redstone components
    public static final BlockRedstoneLatch LATCH = (BlockRedstoneLatch)
            registerBlockWithItem(new BlockRedstoneLatch(false)
            .setRegistryName("latch").setUnlocalizedName("create.latch"));
    public static final BlockRedstoneLatch LATCH_POWERED = (BlockRedstoneLatch)
            registerBlock(new BlockRedstoneLatch(true)
                    .setRegistryName("latch_powered").setUnlocalizedName("create.latch"));

    public static final BlockRedstoneToggleLatch TOGGLE_LATCH = (BlockRedstoneToggleLatch)
            registerBlockWithItem(new BlockRedstoneToggleLatch(false)
            .setRegistryName("toggle_latch").setUnlocalizedName("create.toggle_latch"));
    public static final BlockRedstoneToggleLatch TOGGLE_LATCH_POWERED = (BlockRedstoneToggleLatch)
            registerBlock(new BlockRedstoneToggleLatch(true)
            .setRegistryName("toggle_latch_powered").setUnlocalizedName("create.toggle_latch"));

    public static final BlockRedstoneLinker REDSTONE_LINKER = (BlockRedstoneLinker)
            registerBlockWithItem(new BlockRedstoneLinker(false)
            .setRegistryName("redstone_linker").setUnlocalizedName("create.redstone_linker"));
    public static final BlockRedstoneLinker REDSTONE_LINKER_RECEIVER = (BlockRedstoneLinker)
            registerBlock(new BlockRedstoneLinker(true)
            .setRegistryName("redstone_linker_receiving").setUnlocalizedName("create.redstone_linker"));
    //endregion

    //region Decorations
    public static final BlockFramedGlass FRAMED_GLASS = registerBlockWithItem(new BlockFramedGlass(), true);
    public static final BlockWindowWood WINDOW_WOOD = registerBlockWithItem(new BlockWindowWood(), true);
    public static final BlockWindowIron WINDOW_IRON = registerBlockWithItem(new BlockWindowIron(), true);

    public static final BlockPouf POUF = (BlockPouf)
            registerBlockWithItem(new BlockPouf()
            .setRegistryName("pouf").setUnlocalizedName("create.pouf"), true);

    public static final BlockOrestone ORESTONE = registerBlockWithItem(new BlockOrestone("natural"), true);
    public static final BlockOrestone ORESTONE_CUT = registerBlockWithItem(new BlockOrestone("cut"), true);
    public static final BlockOrestone ORESTONE_POLISHED = registerBlockWithItem(new BlockOrestone("polished"), true);
    public static final BlockOrestone ORESTONE_BRICKS = registerBlockWithItem(new BlockOrestone("bricks"), true);
    public static final BlockOrestone ORESTONE_BRICKS_FANCY = registerBlockWithItem(new BlockOrestone("bricks_fancy"), true);
    public static final BlockOrestone ORESTONE_LAYERED = registerBlockWithItem(new BlockOrestone("layered"), true);
    public static final BlockOrestonePillar ORESTONE_PILLAR_X = registerBlock(new BlockOrestonePillar(EnumFacing.Axis.X));
    public static final BlockOrestonePillar ORESTONE_PILLAR_Y = registerBlockWithItem(new BlockOrestonePillar(EnumFacing.Axis.Y), true);
    public static final BlockOrestonePillar ORESTONE_PILLAR_Z = registerBlock(new BlockOrestonePillar(EnumFacing.Axis.Z));
    //endregion

    private static <T extends Block> T registerBlock(T block) {
        BLOCKS.add(block);
        return block;
    }
    private static <T extends Block> T registerBlockWithItem(@Nonnull T block, @Nonnull Item item) {
        BLOCKS.add(block);
        ItemInit.ITEMS.add(item);
        return block;
    }
    private static <T extends Block> T registerBlockWithItem(@Nonnull T block, boolean variants) {
        Objects.requireNonNull(block.getRegistryName(), "Block has no registry name!!");
        if (variants) return registerBlockWithItem(block, new ItemBlockVariants(block).setRegistryName(block.getRegistryName()));
        return registerBlockWithItem(block, new ItemBlock(block).setRegistryName(block.getRegistryName()));
    }
    private static <T extends Block> T registerBlockWithItem(@Nonnull T block) {
        return registerBlockWithItem(block, false);
    }
    private static <T extends Block> T registerBlockWithItem(@Nonnull T block, Function<T, ? extends Item> item) {
        Objects.requireNonNull(block.getRegistryName(), "Block has no registry name!!");
        return registerBlockWithItem(block, item.apply(block).setRegistryName(block.getRegistryName()));
    }

    public static void registerTileEntities() {
        CreateLegacy.proxy.registerTileEntities();
    }
}
