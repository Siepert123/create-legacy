package com.siepert.createapi.schematic;

import com.siepert.createlegacy.CreateLegacy;
import net.minecraft.block.Block;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Schematic {
    List<Byte> rawData;
    List<Byte> rawStructureData;
    List<Integer> rawStructureIntData;
    List<Byte> rawMetaData;
    private boolean passedCheck = false;
    private Block[][][] structure;
    private SchematicDataInstance dataInstance;

    public Block[][][] getStructure() {
        return structure;
    }
    public SchematicDataInstance getDataInstance() {
        return dataInstance;
    }


    private void splitRawData() {
        rawMetaData = rawData.subList(0, 3);
        rawStructureData = rawData.subList(3, rawData.size());
    }
    private void extractMetaData() {
        dataInstance = new SchematicDataInstance(rawMetaData.get(0), rawMetaData.get(1), rawMetaData.get(2));
    }
    private void check() {
        if (dataInstance.getSizeX() * dataInstance.getSizeY() * dataInstance.getSizeZ() * 4 == (rawStructureData.size())) {
            passedCheck = true;
        }

        if (!passedCheck) {
            CreateLegacy.logger.warn("Invalid schematic!");
        }
    }

    private void convertBytesToBlocks() {
        rawStructureIntData = new ArrayList<>();
        for (int i = 0; i < rawStructureData.size() / 4; i++) {
            rawStructureIntData.add(ByteBuffer.wrap(new byte[]{rawStructureData.get(i * 4),
                    rawStructureData.get(i * 4 + 1),
                    rawStructureData.get(i * 4 + 2),
                    rawStructureData.get(i * 4 + 3)}).getInt());
        }
        structure = new Block[dataInstance.getSizeX()][dataInstance.getSizeY()][dataInstance.getSizeZ()];
        int i = 0;
        for (int x = 0; x < dataInstance.getSizeX(); x++) {
            for (int y = 0; y < dataInstance.getSizeY(); y++) {
                for (int z = 0; z < dataInstance.getSizeZ(); z++) {
                    structure[x][y][z] = Block.getBlockById(rawStructureIntData.get(i));
                    i++;
                }
            }
        }
    }

    public void init() {
        splitRawData();
        extractMetaData();
        check();
        if (passedCheck) {
            convertBytesToBlocks();
        }
    }
}
