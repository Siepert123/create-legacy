package com.siepert.createapi.schematic;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SchematicCompiler {
    public void saveToFile(Block[][][] structure, String filename) {
        Path mcPath = Minecraft.getMinecraft().mcDataDir.toPath();

        File dir = new File(mcPath.toFile(), "schematics");
        dir.mkdir();

        List<Byte> toSave = new ArrayList<>();

        toSave.add((byte) structure.length);
        toSave.add((byte) structure[0].length);
        toSave.add((byte) structure[0][0 ].length);
    }

}
