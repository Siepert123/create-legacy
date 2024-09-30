package com.melonstudios.createlegacy.world.gen;

import com.melonstudios.createlegacy.CreateConfig;
import com.melonstudios.createlegacy.block.BlockOre;
import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.block.stone.AbstractBlockOrestone;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public final class WorldGeneratorCreateLegacy implements IWorldGenerator {
    private final WorldGenerator asurine, crimsite, limestone, ochrum, scorchia, scoria, veridium, copper, zinc;
    public WorldGeneratorCreateLegacy() {
        asurine = new WorldGenMinable(ModBlocks.ORESTONE.getDefaultState()
                .withProperty(AbstractBlockOrestone.STONE_TYPE, AbstractBlockOrestone.StoneType.ASURINE),
                30, BlockMatcher.forBlock(Blocks.STONE));
        crimsite = new WorldGenMinable(ModBlocks.ORESTONE.getDefaultState()
                .withProperty(AbstractBlockOrestone.STONE_TYPE, AbstractBlockOrestone.StoneType.CRIMSITE),
                30, BlockMatcher.forBlock(Blocks.STONE));
        limestone = new WorldGenMinable(ModBlocks.ORESTONE.getDefaultState()
                .withProperty(AbstractBlockOrestone.STONE_TYPE, AbstractBlockOrestone.StoneType.LIMESTONE),
                30, BlockMatcher.forBlock(Blocks.STONE));
        ochrum = new WorldGenMinable(ModBlocks.ORESTONE.getDefaultState()
                .withProperty(AbstractBlockOrestone.STONE_TYPE, AbstractBlockOrestone.StoneType.OCHRUM),
                30, BlockMatcher.forBlock(Blocks.STONE));
        scorchia = new WorldGenMinable(ModBlocks.ORESTONE.getDefaultState()
                .withProperty(AbstractBlockOrestone.STONE_TYPE, AbstractBlockOrestone.StoneType.SCORCHIA),
                30, BlockMatcher.forBlock(Blocks.NETHERRACK));
        scoria = new WorldGenMinable(ModBlocks.ORESTONE.getDefaultState()
                .withProperty(AbstractBlockOrestone.STONE_TYPE, AbstractBlockOrestone.StoneType.SCORIA),
                30, BlockMatcher.forBlock(Blocks.NETHERRACK));
        veridium = new WorldGenMinable(ModBlocks.ORESTONE.getDefaultState()
                .withProperty(AbstractBlockOrestone.STONE_TYPE, AbstractBlockOrestone.StoneType.VERIDIUM),
                30, BlockMatcher.forBlock(Blocks.STONE));

        copper = new WorldGenMinable(ModBlocks.ORE.getDefaultState(),
                6, BlockMatcher.forBlock(Blocks.STONE));
        zinc = new WorldGenMinable(ModBlocks.ORE.getDefaultState()
                .withProperty(BlockOre.VARIANT, BlockOre.Variant.ZINC),
                6, BlockMatcher.forBlock(Blocks.STONE));
    }
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        switch (world.provider.getDimension()) {
            case -1:
                if (CreateConfig.worldGenConfig.generateScorchia)
                    runGenerator(scorchia, world, random, chunkX, chunkZ, 3, 60, 110);
                if (CreateConfig.worldGenConfig.generateScoria)
                    runGenerator(scoria, world, random, chunkX, chunkZ, 3, 10, 70);
            case 0:
                if (CreateConfig.worldGenConfig.generateCopper)
                    runGenerator(copper, world, random, chunkX, chunkZ, 16, 32, 128, 2);
                if (CreateConfig.worldGenConfig.generateZinc)
                    runGenerator(zinc, world, random, chunkX, chunkZ, 8, 16, 64);
                if (CreateConfig.worldGenConfig.generateAsurine)
                    runGenerator(asurine, world, random, chunkX, chunkZ, 1, 10, 40, 5);
                if (CreateConfig.worldGenConfig.generateCrimsite)
                    runGenerator(crimsite, world, random, chunkX, chunkZ, 1, 40, 64, 2);
                if (CreateConfig.worldGenConfig.generateLimestone)
                    runGenerator(limestone, world, random, chunkX, chunkZ, 1, 30, 50, 5);
                if (CreateConfig.worldGenConfig.generateOchrum)
                    runGenerator(ochrum, world, random, chunkX, chunkZ, 1, 10, 40, 5);
                if (CreateConfig.worldGenConfig.generateVeridium)
                    runGenerator(veridium, world, random, chunkX, chunkZ, 1, 40, 64, 5);
        }
    }

    private void runGenerator(WorldGenerator gen, World world, Random random, int chunkX, int chunkZ,
                              int chance, int minHeight, int maxHeight, int additionalChance) {
        if (minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal ore gen parameters: min height may not surpass max height!");
        if (minHeight < 0)
            throw new IllegalArgumentException("Illegal ore gen parameters: min height is below 0");
        if (maxHeight > 256)
            throw new IllegalArgumentException("Illegal ore gen parameters: min height is above 256");
        if (additionalChance < 1)
            throw new IllegalArgumentException("Illegal additional chance: may not be below 1, currently is: " + additionalChance);

        int heightDiff = maxHeight - minHeight;

        for (int i = 0; i < chance; i++) {
            if (random.nextInt(additionalChance) == 0) {
                int x = chunkX * 16 + random.nextInt(16);
                int z = chunkZ * 16 + random.nextInt(16);

                int y = minHeight + random.nextInt(heightDiff);

                gen.generate(world, random, new BlockPos(x, y, z));
            }
        }
    }
    private void runGenerator(WorldGenerator gen, World world, Random random, int chunkX, int chunkZ,
                              int chance, int minHeight, int maxHeight) {
        runGenerator(gen, world, random, chunkX, chunkZ, chance, minHeight, maxHeight, 1);
    }
}
