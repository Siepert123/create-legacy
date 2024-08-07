package com.siepert.createlegacy;

import net.minecraftforge.common.config.Config;

@Config(modid = CreateLegacyModData.MOD_ID, name = CreateLegacyModData.MOD_ID + '/' + CreateLegacyModData.MOD_ID)
public class ConfigHolder {
    @Config.Comment("Config options for Create Legacy World Generation")
    @Config.Name("Worldgen Options")
    @Config.RequiresMcRestart
    public static WorldgenOptions worldgen = new WorldgenOptions();
    public static class WorldgenOptions {
        @Config.Comment({ "Whether to generate ores from Create Legacy (for example copper ore)",
                "Default: true" })
        public boolean generateCreateOres = true;
        @Config.Comment({ "Whether to generate stone types from Create Legacy (for example limestone)",
                "Default: true" })
        public boolean generateCreateStoneTypes = true;
        @Config.Comment({ "Whether to generate structures from Create Legacy",
                "Default: true" })
        public boolean generateCreateStructures = true;
    }
}
