package com.melonstudios.createlegacy.util.registries;

import com.melonstudios.createlegacy.CreateLegacy;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public final class ModSoundEvents {
    public static SoundEvent ITEM_SANDPAPER_USED, BLOCK_COG_AMBIENT, BLOCK_COG_AMBIENT_2, BLOCK_PRESS_ACTIVATE,
        ITEM_WRENCH_USED_ROTATE, ITEM_WRENCH_USED_DISMANTLE, BLOCK_MILLSTONE_AMBIENT;

    public static void registerSounds() {
        ITEM_SANDPAPER_USED = registerSound("item.sandpaper.used");
        BLOCK_COG_AMBIENT = registerSound("block.cog.ambient");
        BLOCK_COG_AMBIENT_2 = registerSound("block.cog.ambient2");
        BLOCK_PRESS_ACTIVATE = registerSound("block.press.activate");
        ITEM_WRENCH_USED_ROTATE = registerSound("item.wrench.used.rotate");
        ITEM_WRENCH_USED_DISMANTLE = registerSound("item.wrench.used.dismantle");
        BLOCK_MILLSTONE_AMBIENT = registerSound("block.millstone.ambient");
    }

    private static SoundEvent registerSound(String registry) {
        ResourceLocation resource = new ResourceLocation(CreateLegacy.MOD_ID, registry);
        SoundEvent event = new SoundEvent(resource);
        event.setRegistryName(registry);
        ForgeRegistries.SOUND_EVENTS.register(event);
        return event;
    }
}
