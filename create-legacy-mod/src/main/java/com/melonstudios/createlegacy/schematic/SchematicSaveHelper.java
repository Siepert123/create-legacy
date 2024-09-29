package com.melonstudios.createlegacy.schematic;

import com.melonstudios.createlegacy.util.DisplayLink;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;
import java.io.File;

/**
 * Helps to save schematics to the schematics folder.
 * @author Siepert123
 * @see SchematicEncodingSystem
 * @since 0.1.0
 */
public final class SchematicSaveHelper {
    private SchematicSaveHelper() {}

    private static File SCHEMATIC_DIR;

    @Nonnull
    public static File schematicsDir() {
        if (SCHEMATIC_DIR == null) throw new IllegalStateException("Tried to access schematics directory before creation!");
        return SCHEMATIC_DIR;
    }
    public static void makeSchematicsFolder() {
        SCHEMATIC_DIR = new File(Minecraft.getMinecraft().mcDataDir, "create_schematics");
        if (SCHEMATIC_DIR.mkdir()) DisplayLink.info("Successfully created schematics directory!");
    }
}
