package com.melonstudios.createlegacy.block;

import com.melonstudios.createlegacy.block.kinetic.*;
import com.melonstudios.createlegacy.block.stone.*;
import com.melonstudios.createlegacy.item.ItemBlockVariants;
import com.melonstudios.createlegacy.item.ModItems;
import com.melonstudios.createlegacy.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public final class ModBlocks {
    public static final List<Block> BLOCKS = new ArrayList<>();

    public static final Block ORE = registerBlockWithItem(new BlockOre(), true);
    public static final Block METAL = registerBlockWithItem(new BlockMetal(), true);

    public static final Block CASING = registerBlockWithItem(new BlockCasing(), true);

    public static final Block ROTATOR = registerBlockWithItem(new BlockRotator(), true);
    public static final Block SHAFT_ENCASED = registerBlockWithItem(new BlockEncasedShaft(), true);
    public static final Block SAW = registerBlockWithItem(new BlockSaw());
    public static final Block BEARING = registerBlockWithItem(new BlockBearing());
    public static final Block PRESS = registerBlockWithItem(new BlockPress());
    public static final Block MILLSTONE = registerBlockWithItem(new BlockMillstone());
    public static final Block FAN = registerBlockWithItem(new BlockFan());
    public static final Block NETWORK_INSPECTOR = registerBlockWithItem(new BlockNetworkInspector(), true);

    public static final Block DEPOT = registerBlockWithItem(new BlockDepot());

    public static final Block HAND_CRANK = registerBlockWithItem(new BlockHandCrank());
    public static final Block WATER_WHEEL = registerBlockWithItem(new BlockWaterWheel());
    public static final Block FURNACE_ENGINE = registerBlockWithItem(new BlockFurnaceEngine(), true);
    public static final Block CREATIVE_MOTOR = registerBlockWithItem(new BlockCreativeMotor());

    public static final Block CHIGWANKER = registerBlockWithItem(new BlockChigwanker());
    public static final Block RENDER = registerBlock(new BlockRender());
    public static final Block RENDER_BEARING_ANCHOR = registerBlock(new BlockRenderBearingAnchor());

    public static final AbstractBlockOrestone ORESTONE = registerOrestoneBlock(new BlockOrestone());
    public static final AbstractBlockOrestone ORESTONE_POLISHED = registerOrestoneBlock(new BlockOrestonePolished());
    public static final AbstractBlockOrestone ORESTONE_BRICKS = registerOrestoneBlock(new BlockOrestoneBricks());
    public static final AbstractBlockOrestone ORESTONE_BRICKS_FANCY = registerOrestoneBlock(new BlockOrestoneBricksFancy());
    public static final AbstractBlockOrestone ORESTONE_PILLAR_Y
            = registerOrestoneBlock(new BlockOrestonePillar(EnumFacing.Axis.Y));
    public static final AbstractBlockOrestone ORESTONE_PILLAR_X
            = (AbstractBlockOrestone) registerBlock(new BlockOrestonePillar(EnumFacing.Axis.X));
    public static final AbstractBlockOrestone ORESTONE_PILLAR_Z
            = (AbstractBlockOrestone) registerBlock(new BlockOrestonePillar(EnumFacing.Axis.Z));
    public static final AbstractBlockOrestone ORESTONE_LAYERED = registerOrestoneBlock(new BlockOrestoneLayered());

    private static Block registerBlock(Block block) {
        BLOCKS.add(block);
        return block;
    }
    private static Block registerBlockWithItem(Block block, Item ib) {
        BLOCKS.add(block);
        ModItems.ITEMS.add(ib);
        return block;
    }
    private static Block registerBlockWithItem(Block block, boolean variants) {
        if (variants) return registerBlockWithItem(block, new ItemBlockVariants(block).setRegistryName(block.getRegistryName()));
        return registerBlockWithItem(block, new ItemBlock(block).setRegistryName(block.getRegistryName()));
    }
    private static Block registerBlockWithItem(Block block) {
        return registerBlockWithItem(block, false);
    }
    private static AbstractBlockOrestone registerOrestoneBlock(AbstractBlockOrestone orestone) {
        return (AbstractBlockOrestone) registerBlockWithItem(orestone, true);
    }

    public static void setTileEntities() {
        registerTE(TileEntityShaft.class, "shaft", new TileEntityShaftRenderer());
        registerTE(TileEntityCog.class, "cog", new TileEntityCogRenderer());

        registerTE(TileEntitySaw.class, "saw", new TileEntitySawRenderer());
        registerTE(TileEntityBearing.class, "bearing", new TileEntityBearingRenderer());
        registerTE(TileEntityPress.class, "press", new TileEntityPressRenderer());
        registerTE(TileEntityMillstone.class, "millstone", new TileEntityMillstoneRenderer());
        registerTE(TileEntityFan.class, "fan", new TileEntityFanRenderer());

        registerTE(TileEntityDepot.class, "depot", new TileEntityDepotRenderer());

        registerTE(TileEntitySpeedometer.class, "speedometer", new TileEntitySpeedometerRenderer());
        registerTE(TileEntityStressometer.class, "stressometer", new TileEntityStressometerRenderer());

        registerTE(TileEntityHandCrank.class, "handcrank", new TileEntityHandCrankRenderer());
        registerTE(TileEntityWaterWheel.class, "water_wheel", new TileEntityWaterWheelRenderer());
        registerTE(TileEntityFlywheel.class, "flywheel", new TileEntityFlywheelRenderer());
        registerTE(TileEntityCreativeMotor.class, "creative_motor", new TileEntityCreativeMotorRenderer());


        registerTE(TileEntityChigwanker.class, "chigwanker", null);
    }
    private static void registerTE(Class<? extends TileEntity> te, String registry, @Nullable TileEntitySpecialRenderer<?> renderer) {
        GameRegistry.registerTileEntity(te, new ResourceLocation("create", registry));
        if (renderer != null) TileEntityRendererDispatcher.instance.renderers.put(te, renderer);
    }
}
