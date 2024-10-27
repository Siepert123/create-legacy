package com.melonstudios.createlegacy.event;

import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.item.ModItems;
import com.melonstudios.createlegacy.util.DisplayLink;
import com.melonstudios.createlegacy.util.registries.OreDictHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public final class CreateLegacyEventHandler {

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {
        ModItems.setItemModels();
    }

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        DisplayLink.debug("Registering %s items", ModItems.ITEMS.size());
        event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
        OreDictHandler.init();
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        DisplayLink.debug("Registering %s blocks", ModBlocks.BLOCKS.size());
        event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
        ModBlocks.setTileEntities();
    }

    @SubscribeEvent
    public static void onMetalTypeQuery(MetalTypesQueryEvent event) {
        event.addTypes("Iron", "Gold", "Copper",
                "Zinc", "Brass", "Mingrade", "Lead",
                "Electrum", "Bismuth", "Chrome",
                "Nickel", "Uranium", "Thorium",
                "Titanium", "Tin", "Zirconium",
                "Tungsten", "Californium", "Constantan",
                "Steel");
    }
}
