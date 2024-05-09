package com.siepert.createlegacy.mainRegistry;

import com.siepert.createlegacy.blocks.*;
import com.siepert.createlegacy.blocks.logic.BlockScheduleCook;
import com.siepert.createlegacy.blocks.logic.BlockScheduleWash;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {
    public static final List<Block> BLOCKS = new ArrayList<Block>();

    public static final Block ORE = new BlockOre();
    public static final Block MATERIAL_STORAGE_BLOCK = new BlockMaterialStorage();
    public static final Block CASING_BLOCK = new BlockCasing();
    public static final Block COGWHEEL = new BlockCogwheel("cogwheel");

    public static final Block SCHEDULE_WASH = new BlockScheduleWash();
    public static final Block SCHEDULE_COOK = new BlockScheduleCook();
}
