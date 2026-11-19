package nl.melonstudios.create.cfg;

import net.minecraftforge.common.config.Config;

@Config(modid = "create", name = "create_common", category = "common")
public class CommonConfig {
    @Config.RequiresMcRestart
    @Config.Comment("Too many kinetic updates break a component if true")
    public static boolean enableFlickerTally  = true;

    @Config.Comment("Whether ores (copper, zinc) should generate.")
    public static boolean generateOres = true;
    @Config.Comment("Whether orestones (crimsite, veridium, etc.) should generate.")
    public static boolean generateStones = true;
}
