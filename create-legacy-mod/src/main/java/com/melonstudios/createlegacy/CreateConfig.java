package com.melonstudios.createlegacy;

import net.minecraftforge.common.config.Config;

import com.melonstudios.createlegacy.util.BitSplitter;
import com.melonstudios.createlegacy.item.ItemSchematic;

/**
 * Create Legacy's configuration class!
 */
@Config(modid = CreateLegacy.MOD_ID, name = CreateLegacy.MOD_ID + "/" + CreateLegacy.MOD_ID)
public class CreateConfig {

    /**
     * Prevents the BitSplitter from crashing the game.
     * @since 0.1.0
     * @see BitSplitter
     */
    @Config.RequiresMcRestart
    @Config.LangKey("config.create.preventBitSplitterTestCrash.name")
    public static boolean preventBitSplitterTestCrash = false;

    /**
     * Allows players in Creative Mode to instantly paste a structure using schematics without using schematicannons.
     * @since 0.1.0
     * @see ItemSchematic
     */
    @Config.LangKey("config.create.allowInstantSchematicPlacement")
    public static boolean allowInstantSchematicPlacement = true;

    @Config.LangKey("config.create.worldGenConfig")
    @Config.Name("World generation config")
    public static final WorldGenConfig worldGenConfig = new WorldGenConfig();
    public static class WorldGenConfig {
        public boolean generateAsurine = true;
        public boolean generateCrimsite = true;
        public boolean generateLimestone = true;
        public boolean generateOchrum = true;
        public boolean generateScorchia = true;
        public boolean generateScoria = true;
        public boolean generateVeridium = true;

        public boolean generateCopper = true;
        public boolean generateZinc = true;
    }
}
