package com.siepert.createlegacy.util.handlers;

import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createlegacy.util.compat.OreDictionaryCompat;
import com.siepert.createlegacy.util.compat.TinkersCompat;
import com.siepert.createlegacy.world.gen.WorldGenCustomOres;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class RegistryHandler {

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
        TileEntityHandler.registerTileEntities();
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

    public static void otherPreInitRegistries() {
        GameRegistry.registerWorldGenerator(new WorldGenCustomOres(), 0);
    }

    public static void otherInitRegistries() {
        OreDictionaryCompat.registerOres();
        OreDictionaryCompat.registerStoneTypes();
        ModSoundHandler.registerSounds();
    }

    public static void otherPostInitRegistries() {
        RecipeHandler.registerOreSmelting();
        RecipeHandler.registerCrushedOreCompatSmelting();

        RecipeHandler.registerWashing();
        RecipeHandler.registerCompatPressRecipes();
        RecipeHandler.registerCompatCompactingRecipes();
        RecipeHandler.registerCompatCrushingRecipes();

        RecipeHandler.registerOther();

        TinkersCompat.registerCompact();
    }
}
