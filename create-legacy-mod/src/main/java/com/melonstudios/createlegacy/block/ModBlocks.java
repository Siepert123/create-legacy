package com.melonstudios.createlegacy.block;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.block.kinetic.*;
import com.melonstudios.createlegacy.block.stone.*;
import com.melonstudios.createlegacy.copycat.BlockCopycat;
import com.melonstudios.createlegacy.copycat.BlockCopycatPanel;
import com.melonstudios.createlegacy.copycat.BlockCopycatStep;
import com.melonstudios.createlegacy.item.ItemBlockVariants;
import com.melonstudios.createlegacy.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

public final class ModBlocks {
    public static final List<Block> BLOCKS = new ArrayList<>();

    public static final BlockOre ORE = registerBlockWithItem(new BlockOre(), true);
    public static final BlockMetal METAL = registerBlockWithItem(new BlockMetal(), true);

    public static final BlockCasing CASING = registerBlockWithItem(new BlockCasing(), true);

    public static final BlockRotator ROTATOR = registerBlockWithItem(new BlockRotator(), true);
    public static final BlockEncasedShaft SHAFT_ENCASED = registerBlockWithItem(new BlockEncasedShaft(), true);
    public static final BlockGearbox GEARBOX = registerBlockWithItem(new BlockGearbox(), true);
    public static final BlockKineticUtility KINETIC_UTILITY = registerBlockWithItem(new BlockKineticUtility(), true);
    public static final BlockSaw SAW = registerBlockWithItem(new BlockSaw());
    public static final BlockBearing BEARING = registerBlockWithItem(new BlockBearing());
    public static final BlockPress PRESS = registerBlockWithItem(new BlockPress());
    public static final BlockMillstone MILLSTONE = registerBlockWithItem(new BlockMillstone());
    public static final BlockFan FAN = registerBlockWithItem(new BlockFan());
    public static final BlockDrill DRILL = registerBlockWithItem(new BlockDrill());
    public static final BlockNetworkInspector NETWORK_INSPECTOR = registerBlockWithItem(new BlockNetworkInspector(), true);

    public static final BlockTurntable TURNTABLE = registerBlockWithItem(new BlockTurntable());

    public static final BlockDepot DEPOT = registerBlockWithItem(new BlockDepot());
    public static final BlockChute CHUTE = registerBlockWithItem(new BlockChute());
    public static final BlockFunnel FUNNEL = registerBlockWithItem(new BlockFunnel(), true);

    public static final BlockHandCrank HAND_CRANK = registerBlockWithItem(new BlockHandCrank());
    public static final BlockWaterWheel WATER_WHEEL = registerBlockWithItem(new BlockWaterWheel());
    public static final BlockFurnaceEngine FURNACE_ENGINE = registerBlockWithItem(new BlockFurnaceEngine(), true);
    public static final BlockCreativeMotor CREATIVE_MOTOR = registerBlockWithItem(new BlockCreativeMotor());

    public static final BlockBlazeBurner BLAZE_BURNER = registerBlockWithItem(new BlockBlazeBurner(), true);

    public static final BlockChigwanker CHIGWANKER = registerBlockWithItem(new BlockChigwanker());
    public static final BlockRender RENDER = registerBlock(new BlockRender());
    public static final BlockRenderBearingAnchor RENDER_BEARING_ANCHOR = registerBlock(new BlockRenderBearingAnchor());

    public static final AbstractBlockOrestone ORESTONE = registerOrestoneBlock(new BlockOrestone());
    public static final AbstractBlockOrestone ORESTONE_POLISHED = registerOrestoneBlock(new BlockOrestonePolished());
    public static final AbstractBlockOrestone ORESTONE_BRICKS = registerOrestoneBlock(new BlockOrestoneBricks());
    public static final AbstractBlockOrestone ORESTONE_BRICKS_FANCY = registerOrestoneBlock(new BlockOrestoneBricksFancy());
    public static final AbstractBlockOrestone ORESTONE_PILLAR_Y
            = registerOrestoneBlock(new BlockOrestonePillar(EnumFacing.Axis.Y));
    public static final AbstractBlockOrestone ORESTONE_PILLAR_X
            = registerBlock(new BlockOrestonePillar(EnumFacing.Axis.X));
    public static final AbstractBlockOrestone ORESTONE_PILLAR_Z
            = registerBlock(new BlockOrestonePillar(EnumFacing.Axis.Z));
    public static final AbstractBlockOrestone ORESTONE_LAYERED = registerOrestoneBlock(new BlockOrestoneLayered());

    public static final Block INDUSTRIAL_IRON = registerBlockWithItem(new Block(Material.IRON, MapColor.GRAY).setCreativeTab(CreateLegacy.TAB_DECORATIONS)
            .setRegistryName("industrial_iron").setUnlocalizedName("create.industrial_iron").setHardness(15f).setResistance(50f));
    static {
        INDUSTRIAL_IRON.setHarvestLevel("pickaxe", 2);
    }
    public static final BlockIndustrialIronGlass INDUSTRIAL_IRON_GLASS = registerBlockWithItem(new BlockIndustrialIronGlass());

    public static final BlockFramedGlass FRAMED_GLASS = registerBlockWithItem(new BlockFramedGlass(), true);
    public static final BlockFramedGlassPane FRAMED_GLASS_PANE = registerBlockWithItem(new BlockFramedGlassPane(), true);

    public static final BlockCopycat COPYCAT_PANEL = registerBlockWithItem(new BlockCopycatPanel());
    public static final BlockCopycat COPYCAT_STEP = registerBlockWithItem(new BlockCopycatStep());

    private static <T extends Block> T registerBlock(T block) {
        BLOCKS.add(block);
        return block;
    }
    private static <T extends Block> T registerBlockWithItem(T block, Item ib) {
        BLOCKS.add(block);
        ModItems.ITEMS.add(ib);
        return block;
    }
    private static <T extends Block> T registerBlockWithItem(T block, boolean variants) {
        if (variants) return registerBlockWithItem(block, new ItemBlockVariants(block).setRegistryName(block.getRegistryName()));
        return registerBlockWithItem(block, new ItemBlock(block).setRegistryName(block.getRegistryName()));
    }
    private static <T extends Block> T registerBlockWithItem(T block) {
        return registerBlockWithItem(block, false);
    }
    private static AbstractBlockOrestone registerOrestoneBlock(AbstractBlockOrestone orestone) {
        return registerBlockWithItem(orestone, true);
    }

    public static void setTileEntities() {
        CreateLegacy.proxy.setTileEntities(); // CommonProxy
        CreateLegacy.proxy.setTERenderers(); // ClientProxy
    }
}
