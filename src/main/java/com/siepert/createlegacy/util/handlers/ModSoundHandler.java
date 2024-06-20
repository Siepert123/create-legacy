package com.siepert.createlegacy.util.handlers;

import com.siepert.createlegacy.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModSoundHandler {

    public static SoundEvent ITEM_SANDPAPER_USED, BLOCK_COGWHEEL_AMBIENT, BLOCK_COGWHEEL_AMBIENT_2, BLOCK_PRESS_ACTIVATION,
        ITEM_WRENCH_ROTATE, ITEM_WRENCH_DISMANTLE, BLOCK_MILLSTONE_AMBIENT;

    public static void registerSounds() {
        ITEM_SANDPAPER_USED = registerSound("item.sandpaper.usage");
        BLOCK_COGWHEEL_AMBIENT = registerSound("block.cogwheel.ambient");
        BLOCK_COGWHEEL_AMBIENT_2 = registerSound("block.cogwheel.ambient2");
        BLOCK_PRESS_ACTIVATION = registerSound("block.press.activation");
        ITEM_WRENCH_DISMANTLE = registerSound("item.wrench.usage.dismantle");
        ITEM_WRENCH_ROTATE = registerSound("item.wrench.usage.rotate");
        BLOCK_MILLSTONE_AMBIENT = registerSound("block.millstone.ambient");
    }

    private static SoundEvent registerSound(String name) {
        ResourceLocation location = new ResourceLocation(Reference.MOD_ID, name);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(name);
        ForgeRegistries.SOUND_EVENTS.register(event);
        return event;
    }
}
