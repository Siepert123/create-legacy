package nl.melonstudios.create.worldgen;

import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import nl.melonstudios.create.CreateLegacy;
import nl.melonstudios.create.block.BlockOre;
import nl.melonstudios.create.init.BlockInit;

import java.util.Random;

@SuppressWarnings("unused")
public class CreateWorldGen implements IWorldGenerator {
    private static boolean generateOres = true;
    private static boolean generateStones = true;
    public static void setGenerationFlags(boolean ores, boolean stones) {
        CreateLegacy.logger.debug("Generation flags set to ores:{} stones:{}", ores, stones);
        generateOres = ores;
        generateStones = stones;
    }

    private final WorldGenerator copper, zinc;
    private final WorldGenerator asurine, crimsite, limestone, ochrum, scorchia, scoria, veridium;
    public CreateWorldGen() {
        this.copper = new WorldGenMinable(BlockOre.copper(), 6, BlockMatcher.forBlock(Blocks.STONE));
        this.zinc = new WorldGenMinable(BlockOre.zinc(), 6, BlockMatcher.forBlock(Blocks.STONE));

        this.asurine = new WorldGenMinable(BlockInit.ORESTONE.getStateFromMeta(0), 128, BlockMatcher.forBlock(Blocks.STONE));
        this.crimsite = new WorldGenMinable(BlockInit.ORESTONE.getStateFromMeta(1), 128, BlockMatcher.forBlock(Blocks.STONE));
        this.limestone = new WorldGenMinable(BlockInit.ORESTONE.getStateFromMeta(2), 128, BlockMatcher.forBlock(Blocks.STONE));
        this.ochrum = new WorldGenMinable(BlockInit.ORESTONE.getStateFromMeta(3), 128, BlockMatcher.forBlock(Blocks.STONE));
        this.scorchia = new WorldGenMinable(BlockInit.ORESTONE.getStateFromMeta(4), 128, BlockMatcher.forBlock(Blocks.NETHERRACK));
        this.scoria = new WorldGenMinable(BlockInit.ORESTONE.getStateFromMeta(5), 128, BlockMatcher.forBlock(Blocks.NETHERRACK));
        this.veridium = new WorldGenMinable(BlockInit.ORESTONE.getStateFromMeta(6), 128, BlockMatcher.forBlock(Blocks.STONE));
    }
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.getDimension() == -1) {
            this.genNether(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
        } else if (world.provider.getDimension() == 1) {
            this.genEnd(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
        } else {
            this.genOverworld(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
        }
    }

    private void genNether(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (generateStones) {
            if (random.nextInt(64) == 0) {
                this.runGenerator(this.scorchia, world, random, chunkX, chunkZ, 1, 0, 64);
            }
            if (random.nextInt(64) == 0) {
                this.runGenerator(this.scoria, world, random, chunkX, chunkZ, 1, 64, 128);
            }
        }
    }
    private void genEnd(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        // NOOP
    }
    private void genOverworld(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (generateOres) {
            this.runGenerator(this.copper, world, random, chunkX, chunkZ, 8 + random.nextInt(8), 32, 128);
            this.runGenerator(this.zinc, world, random, chunkX, chunkZ, 8, 4, 64);
        }
        if (generateStones) {
            if (random.nextInt(64) == 0) {
                this.runGenerator(this.asurine, world, random, chunkX, chunkZ, 1, 0, 64);
            }
            if (random.nextInt(64) == 0) {
                this.runGenerator(this.crimsite, world, random, chunkX, chunkZ, 1, 0, 64);
            }
            if (random.nextInt(64) == 0) {
                this.runGenerator(this.limestone, world, random, chunkX, chunkZ, 1, 0, 64);
            }
            if (random.nextInt(64) == 0) {
                this.runGenerator(this.ochrum, world, random, chunkX, chunkZ, 1, 0, 64);
            }
            if (random.nextInt(64) == 0) {
                this.runGenerator(this.veridium, world, random, chunkX, chunkZ, 1, 0, 64);
            }
        }
    }

    private void runGenerator(WorldGenerator generator, World world, Random random,
                              int chunkX, int chunkZ, int veins, int minHeight, int maxHeight) {
        int heightDiff = maxHeight - minHeight;

        for (int i = 0; i < veins; i++) {
            int x = chunkX * 16 + random.nextInt(16);
            int z = chunkZ * 16 + random.nextInt(16);
            int y = minHeight + random.nextInt(heightDiff);

            generator.generate(world, random, new BlockPos(x, y, z));
        }
    }
}
