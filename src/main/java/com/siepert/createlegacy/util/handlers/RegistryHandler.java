package com.siepert.createlegacy.util.handlers;

import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createlegacy.util.compat.OreDictionaryCompat;
import com.siepert.createlegacy.world.gen.WorldGenCustomOres;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber
public class RegistryHandler {

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
    }

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {
        for (Block block : ModBlocks.BLOCKS) {
            if(block instanceof IHasModel) {
                ((IHasModel) block).registerModels();
            }
        }
        for (Item item : ModItems.ITEMS) {
            if (item instanceof IHasModel) {
                ((IHasModel) item).registerModels();
            }
        }
    }

    public static void otherPreInitRegistries(Logger logger) {
        GameRegistry.registerWorldGenerator(new WorldGenCustomOres(), 0);
    }

    public static void otherInitRegistries(Logger logger) {
        OreDictionaryCompat.registerOres();
        ModSoundHandler.registerSounds();
    }

    public static void otherPostInitRegistries(Logger logger) {
        RecipeHandler.registerOreSmelting(logger);
        RecipeHandler.registerCrushedOreCompatSmelting(logger);
        RecipeHandler.registerWashing(logger);
    }
}
