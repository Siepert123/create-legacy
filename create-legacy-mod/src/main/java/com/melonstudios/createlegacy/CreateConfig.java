package com.melonstudios.createlegacy;

import com.melonstudios.createlegacy.item.ItemSchematic;
import com.melonstudios.createlegacy.util.BitSplitter;
import net.minecraftforge.common.config.Config;

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
    @Config.Comment("Prevent BitSplitter test crashing the game")
    public static boolean preventBitSplitterTestCrash = false;

    /**
     * Allows players in Creative Mode to instantly paste a structure using schematics without using schematicannons.
     * @since 0.1.0
     * @see ItemSchematic
     */
    @Config.Comment("Allow instant schematic placement in Creative Mode")
    public static boolean allowInstantSchematicPlacement = true;

    @Config.Comment("Render distance of kinetic blocks")
    @Config.RangeInt(min = Byte.MAX_VALUE, max = Short.MAX_VALUE)
    public static int kineticBlocksRenderDistanceSquared = Short.MAX_VALUE;

    @Config.Comment("Enable debug stuff?")
    @Config.RequiresMcRestart
    public static boolean debug = true;

    @Config.Comment("Configure world generation settings")
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
