package com.siepert.createlegacy;

import net.minecraftforge.common.config.Config;

@Config(modid = CreateLegacyModData.MOD_ID, name = CreateLegacyModData.MOD_ID)
public class CreateLegacyConfigHolder {
    @Config.Comment("Config options for Create Legacy World Generation")
    @Config.Name("World Gen Config")
    @Config.RequiresWorldRestart // I'm not 100% sure if it requires an MC restart, but you should probably make a new world
    public static WorldGenConfig worldGen = new WorldGenConfig();
    public static class WorldGenConfig {
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

    @Config.Comment("Configuration for kinetic stuff")
    @Config.Name("Kinetic Config")
    public static KineticConfig kineticConfig = new KineticConfig();
    public static class KineticConfig {

        @Config.RangeDouble(min = 0)
        @Config.Comment({"Stress capacity for the hand crank",
                "Default: 4.0"})
        public double handCrankStressCapacity = 4.0;

        @Config.RangeDouble(min = 0)
        @Config.Comment({"Stress capacity for the water wheel",
                "Default: 8.0"})
        public double waterWheelStressCapacity = 8.0;

        @Config.RangeDouble(min = 0)
        @Config.Comment({"Stress capacity for the furnace engine",
                "Default: 16.0"})
        public double furnaceEngineStressCapacity = 16.0;

        @Config.RangeDouble(min = 0)
        @Config.Comment({"Stress capacity for the creative motor",
                "Default: 256.0"})
        public double creativeMotorStressCapacity = 256.0;

        @Config.RangeDouble(min = 0)
        @Config.Comment({"Stress impact of the mechanical press",
                "Default: 8.0"})
        public double mechanicalPressStressImpact = 8.0;

        @Config.RangeDouble(min = 0)
        @Config.Comment({"Stress impact of the mechanical mixer",
                "Default: 4.0"})
        public double mechanicalMixerStressImpact = 4.0;

        @Config.RangeDouble(min = 0)
        @Config.Comment({"Stress impact of the encased fan",
                "Default: 4.0"})
        public double encasedFanStressImpact = 4.0;

        @Config.RangeDouble(min = 0)
        @Config.Comment({"Stress impact of the mechanical drill",
                "Default: 6.0"})
        public double mechanicalDrillStressImpact = 6.0;

        @Config.RangeDouble(min = 0)
        @Config.Comment({"Stress impact of the mechanical saw",
                "Default: 6.0"})
        public double mechanicalSawStressImpact = 6.0;

        @Config.RangeDouble(min = 0)
        @Config.Comment({"Stress impact of the deployer",
                "Default: 6.0"})
        public double deployerStressImpact = 6.0;

        @Config.RangeDouble(min = 0)
        @Config.Comment({"Stress impact of the mechanical piston",
                "Default: 4.0"})
        public double mechanicalPistonStressImpact = 4.0;

        @Config.RangeDouble(min = 0)
        @Config.Comment({"Stress impact of the millstone",
                "Default: 4.0"})
        public double millstoneStressImpact = 4.0;

        @Config.RangeDouble(min = 0)
        @Config.Comment({"Stress impact of the shaft",
                "Default: 0.0"})
        public double shaftStressImpact = 0.0;

        @Config.RangeDouble(min = 0)
        @Config.Comment({"Stress impact of the cogwheel",
                "Default: 0.0"})
        public double cogwheelStressImpact = 0.0;

        @Config.RangeDouble(min = 0)
        @Config.Comment({"Stress impact of the mechanical belt",
                "Default: 0.0"})
        public double beltStressImpact = 0.0;

        @Config.RangeDouble(min = 0)
        @Config.Comment({"Stress impact of the gearbox",
                "Default: 0.0"})
        public double gearboxStressImpact = 0.0;

        @Config.RangeDouble(min = 0)
        @Config.Comment({"Stress impact of the clutch",
                "Default: 0.0"})
        public double clutchStressImpact = 0.0;

        @Config.RangeDouble(min = 0)
        @Config.Comment({"Stress impact of the gearshift",
                "Default: 0.0"})
        public double gearshiftStressImpact = 0.0;

        @Config.RangeDouble(min = 0)
        @Config.Comment({"Stress impact of the speedometer",
                "Default: 0.0"})
        public double speedometerStressImpact = 0.0;

        @Config.RangeDouble(min = 0)
        @Config.Comment({"Stress impact of the stressometer",
                "Default: 0.0"})
        public double stressometerStressImpact = 0.0;
    }

    @Config.Comment("Other miscellaneous configurations")
    @Config.Name("Other configurations")
    public static OtherConfig otherConfig = new OtherConfig();
    public static class OtherConfig {
        @Config.RangeInt(min = 4, max = 16)
        @Config.SlidingOption
        @Config.Comment({"How many items large the basin inventory should be.",
                "Default: 4"})
        public int maxBasinItems = 4;

        @Config.Comment({"Whether to disable the stress system entirely",
                "Default: false"})
        public boolean disableSU = false;

        @Config.Comment("i forgor :skull:")
        @Config.RequiresMcRestart
        public boolean sillyStuff = false;

        @Config.Comment("Default: true (recommended to not turn off)")
        public boolean enableBlockstatePerformance = true;
    }
}
