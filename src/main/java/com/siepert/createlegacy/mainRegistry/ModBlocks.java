package com.siepert.createlegacy.mainRegistry;

import com.siepert.createlegacy.blocks.*;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {
    public static final List<Block> BLOCKS = new ArrayList<Block>();

    public static final Block ORE = new BlockOre();
    public static final Block MATERIAL_STORAGE_BLOCK = new BlockMaterialStorage();
    public static final Block CASING_BLOCK = new BlockCasing();
    public static final Block COGWHEEL = new BlockCogwheel("cogwheel");
}
