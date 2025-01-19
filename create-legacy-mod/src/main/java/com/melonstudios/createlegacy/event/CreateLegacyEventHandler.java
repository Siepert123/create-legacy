package com.melonstudios.createlegacy.event;

import com.melonstudios.createlegacy.block.IGoggleInfo;
import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.item.ItemGoggles;
import com.melonstudios.createlegacy.item.ModItems;
import com.melonstudios.createlegacy.util.DisplayLink;
import com.melonstudios.createlegacy.util.registries.OreDictHandler;
import com.melonstudios.melonlib.misc.Localizer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
                "Steel", "Mingrade", "Saturnite", "Cadmium",
                "Silver", "Aluminum", "Chinesium",
                "Cobalt", "Ardite", "Manyullyn");
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void overlayRender(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player != null && mc.world != null) {
            EntityPlayer player = mc.player;
            ItemStack helmet = player.inventory.armorInventory.get(3);
            if (helmet.getItem() instanceof ItemGoggles) {
                RayTraceResult result = mc.player.rayTrace(3, event.getPartialTicks());
                if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
                    BlockPos pos = result.getBlockPos();
                    World world = mc.world;
                    IBlockState state = world.getBlockState(pos);
                    if (state.getBlock() instanceof IGoggleInfo) {
                        NonNullList<String> info = ((IGoggleInfo)state.getBlock()).getGoggleInformation(world, pos, state);
                        if (!info.isEmpty()) {
                            mc.fontRenderer.drawStringWithShadow(Localizer.translate("goggles.header"),
                                    event.getResolution().getScaledWidth() / 2f,
                                    event.getResolution().getScaledHeight() / 2f - 10, -1);
                            for (int i = 0; i < info.size(); i++) {
                                mc.fontRenderer.drawStringWithShadow(info.get(i),
                                        event.getResolution().getScaledWidth() / 2f,
                                        event.getResolution().getScaledHeight() / 2f + i * 8, -1);
                            }
                        }
                    }
                }
            }
        }
    }
}
