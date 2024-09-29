package com.melonstudios.createlegacy;

import net.minecraftforge.common.config.Config;

@Config(modid = CreateLegacy.MOD_ID, name = CreateLegacy.MOD_ID + "/" + CreateLegacy.MOD_ID)
public class CreateConfig {

    @Config.RequiresMcRestart
    @Config.LangKey("config.create.preventBitSplitterTestCrash.name")
    public static boolean preventBitSplitterTestCrash = false;

    @Config.LangKey("config.create.allowInstantSchematicPlacement")
    public static boolean allowInstantSchematicPlacement = true;
}
