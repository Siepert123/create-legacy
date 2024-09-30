package com.melonstudios.createlegacy.block;

import com.melonstudios.createlegacy.block.stone.*;
import com.melonstudios.createlegacy.item.ItemBlockVariants;
import com.melonstudios.createlegacy.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

public final class ModBlocks {
    public static final List<Block> BLOCKS = new ArrayList<>();

    public static final Block ORE = registerBlockWithItem(new BlockOre(), true);
    public static final Block METAL = registerBlockWithItem(new BlockMetal(), true);

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

    }
}
