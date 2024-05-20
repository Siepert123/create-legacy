package com.siepert.createlegacy.world.gen;

import com.siepert.createlegacy.blocks.BlockModernStone;
import com.siepert.createlegacy.blocks.BlockOre;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.util.handlers.EnumHandler;
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

public class WorldGenCustomOres implements IWorldGenerator {
    private WorldGenerator ore_copper;
    private WorldGenerator ore_zinc;
    private WorldGenerator stone_calcite;
    private WorldGenerator stone_tuff;

    private WorldGenerator stone_asurine;
    private WorldGenerator stone_crimsite;
    private WorldGenerator stone_limestone;
    private WorldGenerator stone_ochrum;
    private WorldGenerator stone_scorchia;
    private WorldGenerator stone_scoria;
    private WorldGenerator stone_veridium;
    public WorldGenCustomOres() {
        ore_copper = new WorldGenMinable(ModBlocks.ORE.getDefaultState().withProperty(BlockOre.VARIANT, EnumHandler.OreEnumType.COPPER),
                10, BlockMatcher.forBlock(Blocks.STONE));
        ore_zinc = new WorldGenMinable(ModBlocks.ORE.getDefaultState().withProperty(BlockOre.VARIANT, EnumHandler.OreEnumType.ZINC),
                6, BlockMatcher.forBlock(Blocks.STONE));

        stone_calcite = new WorldGenMinable(ModBlocks.MODERN_STONE.getDefaultState().withProperty(BlockModernStone.VARIANT, EnumHandler.ModernStoneEnumType.CALCITE),
                40, BlockMatcher.forBlock(Blocks.STONE));
        stone_tuff = new WorldGenMinable(ModBlocks.MODERN_STONE.getDefaultState().withProperty(BlockModernStone.VARIANT, EnumHandler.ModernStoneEnumType.CALCITE),
                40, BlockMatcher.forBlock(Blocks.STONE));

        stone_asurine = new WorldGenMinable(ModBlocks.ORE.getDefaultState().withProperty(BlockOre.VARIANT, EnumHandler.OreEnumType.ASURINE),
                30, BlockMatcher.forBlock(Blocks.STONE));
        stone_crimsite = new WorldGenMinable(ModBlocks.ORE.getDefaultState().withProperty(BlockOre.VARIANT, EnumHandler.OreEnumType.CRIMSITE),
                30, BlockMatcher.forBlock(Blocks.STONE));
        stone_limestone = new WorldGenMinable(ModBlocks.ORE.getDefaultState().withProperty(BlockOre.VARIANT, EnumHandler.OreEnumType.LIMESTONE),
                30, BlockMatcher.forBlock(Blocks.STONE));
        stone_ochrum = new WorldGenMinable(ModBlocks.ORE.getDefaultState().withProperty(BlockOre.VARIANT, EnumHandler.OreEnumType.OCHRUM),
                30, BlockMatcher.forBlock(Blocks.STONE));
        stone_scorchia = new WorldGenMinable(ModBlocks.ORE.getDefaultState().withProperty(BlockOre.VARIANT, EnumHandler.OreEnumType.SCORCHIA),
                30, BlockMatcher.forBlock(Blocks.STONE));
        stone_scoria = new WorldGenMinable(ModBlocks.ORE.getDefaultState().withProperty(BlockOre.VARIANT, EnumHandler.OreEnumType.SCORIA),
                30, BlockMatcher.forBlock(Blocks.STONE));
        stone_veridium = new WorldGenMinable(ModBlocks.ORE.getDefaultState().withProperty(BlockOre.VARIANT, EnumHandler.OreEnumType.VERIDIUM),
                30, BlockMatcher.forBlock(Blocks.STONE));
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world,
                         IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        switch (world.provider.getDimension()) {
            case -1:
                runGenerator(stone_scoria, world, random, chunkX, chunkZ, 3, 10, 70);
                runGenerator(stone_scorchia, world, random, chunkX, chunkZ, 3, 60, 110);
                break;

            case 0:
                runGenerator(ore_copper, world, random, chunkX, chunkZ, 8, 25, 128);
                runGenerator(ore_zinc, world, random, chunkX, chunkZ, 7, 5, 40);

                runGenerator(stone_calcite, world, random, chunkX, chunkZ, 4, 20, 40);
                runGenerator(stone_tuff, world, random, chunkX, chunkZ, 4, 0, 20);

                runGenerator(stone_asurine, world, random, chunkX, chunkZ, 1, 10, 40);
                runGenerator(stone_crimsite, world, random, chunkX, chunkZ, 1, 40, 64);
                runGenerator(stone_limestone, world, random, chunkX, chunkZ, 1, 30, 50);
                runGenerator(stone_ochrum, world, random, chunkX, chunkZ, 1, 10, 40);
                runGenerator(stone_veridium, world, random, chunkX, chunkZ, 1, 40, 64);
                break;

            case 1:
                break;
        }
    }

    private void runGenerator(WorldGenerator gen, World world, Random random, int chunkX, int chunkZ,
                              int chance, int minHeight, int maxHeight) {
        if (minHeight > maxHeight) throw new IllegalArgumentException("Illegal ore gen parameters: min height may not surpass max height!");
        if (minHeight < 0) throw new IllegalArgumentException("Illegal ore gen parameters: min height is below 0");
        if (maxHeight > 256) throw new IllegalArgumentException("Illegal ore gen parameters: min height is above 256");

        int heightDiff = maxHeight - minHeight + 1;
        for (int i = 0; i < chance; i++) {
            int x = chunkX * 16 + random.nextInt(16);
            int z = chunkZ * 16 + random.nextInt(16);

            int y = minHeight + random.nextInt(heightDiff);

            gen.generate(world, random, new BlockPos(x, y, z));
        }
    }
}
