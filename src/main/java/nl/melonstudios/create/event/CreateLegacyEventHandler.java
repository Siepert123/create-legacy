package nl.melonstudios.create.event;

import com.melonstudios.melonlib.blockdict.BlockDictionary;
import com.melonstudios.melonlib.render.RenderMelon;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nl.melonstudios.create.CreateLegacy;
import nl.melonstudios.create.cfg.ClientConfig;
import nl.melonstudios.create.entity.*;
import nl.melonstudios.create.extensions.IExtensionWorld;
import nl.melonstudios.create.init.BlockInit;
import nl.melonstudios.create.init.ItemInit;
import nl.melonstudios.create.init.OreDictInit;
import nl.melonstudios.create.init.PonderInit;
import nl.melonstudios.create.item.ItemGoggles;
import nl.melonstudios.create.kinetics.KNManager;
import nl.melonstudios.create.kinetics.contraption.Contraption;
import nl.melonstudios.create.kinetics.contraption.ContraptionRendering;
import nl.melonstudios.create.kinetics.contraption.ITileEntityWithContraption;
import nl.melonstudios.create.recipe.sequence.SequenceRecipe;
import nl.melonstudios.create.recipe.sequence.SequenceStep;
import nl.melonstudios.create.recipe.sequence.SequencedRecipes;
import nl.melonstudios.create.util.PerFrameDebugInfo;
import nl.melonstudios.create.util.TextBuilder;
import nl.melonstudios.create.util.interfaces.IBypassBlockUse;
import nl.melonstudios.create.util.interfaces.IGoggleInfo;
import nl.melonstudios.ponder.event.RegisterPondersEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = "create")
public class CreateLegacyEventHandler {
    //Registration
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerItemModels(ModelRegistryEvent event) {
        ItemInit.setItemModels();
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(BlockInit.BLOCKS.toArray(new Block[0]));
        BlockInit.registerTileEntities();
    }
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ItemInit.ITEMS.toArray(new Item[0]));
        OreDictInit.init();
    }
    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().register(EntityEntryBuilder.create().entity(EntityGlue.class)
                .factory(EntityGlue::new).id("create:glue", 0).name("create.glue")
                .tracker(64, 10, false).build());
        event.getRegistry().register(EntityEntryBuilder.create().entity(EntityPouf.class)
                .factory(EntityPouf::new).id("create:pouf", 1).name("create.pouf")
                .tracker(256, 1, false).build());
        event.getRegistry().register(EntityEntryBuilder.create().entity(EntityContraptionBearing.class)
                .factory(EntityContraptionBearing::new).id("create:contraption_bearing", 10).name("create.contraption")
                .tracker(256, 10, false).build());
        event.getRegistry().register(EntityEntryBuilder.create().entity(EntityContraptionPiston.class)
                .factory(EntityContraptionPiston::new).id("create:contraption_piston", 11).name("create.contraption")
                .tracker(256, 10, false).build());
        CreateLegacy.proxy.registerEntityRenderers();
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerPonders(RegisterPondersEvent event) {
        PonderInit.register(event.getRegistrar());
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void stitchTerrain(TextureStitchEvent.Pre event) {
        TextureMap map = event.getMap();
        if ("textures".equals(map.getBasePath())) {
            CreateLegacy.logger.info("Adding fluid textures to texture atlas");
            map.registerSprite(new ResourceLocation("create:fluid/milk_still"));
            map.registerSprite(new ResourceLocation("create:fluid/milk_flowing"));
            map.registerSprite(new ResourceLocation("create:fluid/chocolate_still"));
            map.registerSprite(new ResourceLocation("create:fluid/chocolate_flowing"));
            map.registerSprite(new ResourceLocation("create:fluid/tea_still"));
            map.registerSprite(new ResourceLocation("create:fluid/tea_flowing"));
            CreateLegacy.logger.info("Adding scrolling belt textures to texture atlas");
            map.registerSprite(new ResourceLocation("create:block/belt_scroll"));
            for (EnumDyeColor color : EnumDyeColor.values()) {
                map.registerSprite(new ResourceLocation("create:block/belt_scroll_" + color.getDyeColorName()));
            }
        }
    }

    //Other
    @SubscribeEvent
    public static void gatherExtraCollisions(GetCollisionBoxesEvent event) {
        AxisAlignedBB aabb = event.getAabb();
        if (aabb == null) return;
        List<AxisAlignedBB> collisions = event.getCollisionBoxesList();
        for (ITileEntityWithContraption contraption : ((IExtensionWorld)event.getWorld()).create$getContraptionTileEntities()) {
            contraption.collectCollisions(aabb, collisions);
        }
    }

    @SubscribeEvent
    public static void glueBypassBlockUse(PlayerInteractEvent.RightClickBlock event) {
        if (event.getUseBlock() == Event.Result.DENY) return;
        ItemStack stack = event.getItemStack();
        if (stack.getItem() instanceof IBypassBlockUse) {
            if (((IBypassBlockUse)stack.getItem()).bypass(stack)) {
                if (!BlockDictionary.isBlockTagged(event.getWorld().getBlockState(event.getPos()), "create:bypassGlue")) {
                    event.setUseBlock(Event.Result.DENY);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        KNManager.loadWorld(event.getWorld());
    }
    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event) {
        KNManager.unloadWorld(event.getWorld());
    }

    @SubscribeEvent
    public static void registerStressValues(RegisterStressValuesEvent event) {
        event.registerCapacity(BlockInit.HAND_CRANK, 8.0F);
        event.registerCapacity(BlockInit.WATER_WHEEL, 16.0F);
        event.registerCapacity(BlockInit.FURNACE_ENGINE, 64.0F);
        event.registerCapacity(BlockInit.CREATIVE_MOTOR, 65536.0F);
        event.registerCapacity(BlockInit.BEARING_WINDMILL, 512.0F);
        event.registerStress(BlockInit.TURNTABLE, 4.0F);
        event.registerStress(BlockInit.BEARING, 4.0F);
        event.registerStress(BlockInit.MECHANICAL_PISTON, 4.0F);
        event.registerStress(BlockInit.MECHANICAL_PISTON_EXTENDED, 4.0F);
        event.registerStress(BlockInit.MECHANICAL_PISTON_STICKY, 4.0F);
        event.registerStress(BlockInit.MECHANICAL_PISTON_STICKY_EXTENDED, 4.0F);
        event.registerStress(BlockInit.PRESS, 8.0F);
        event.registerStress(BlockInit.MIXER, 4.0F);
        event.registerStress(BlockInit.DRILL, 4.0F);
        event.registerStress(BlockInit.SAW, 4.0F);
        event.registerStress(BlockInit.DEPLOYER, 4.0F);
        event.registerStress(BlockInit.MILLSTONE, 4.0F);
    }

    private static final ItemStack goggles = new ItemStack(ItemInit.GOGGLES);
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void renderGoggleOverlay(RenderGameOverlayEvent.Pre event) {
        final Minecraft mc = Minecraft.getMinecraft();
        if (mc.getRenderManager().options == null) return;
        final FontRenderer font = RenderMelon.getDefaultFontRenderer();
        if (mc.player != null && mc.world != null &&
                !mc.getRenderManager().options.hideGUI &&
                event.getType() == RenderGameOverlayEvent.ElementType.ALL &&
                mc.getRenderViewEntity() == mc.player) {
            final ItemStack helmet = mc.player.inventory.armorInventory.get(3);
            if (helmet.getItem() instanceof ItemGoggles) {
                RayTraceResult result = mc.objectMouseOver;
                if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
                    BlockPos pos = result.getBlockPos();
                    World world = mc.world;
                    IBlockState state = world.getBlockState(pos);
                    if (state.getBlock() instanceof IGoggleInfo) {
                        List<String> info = ((IGoggleInfo)state.getBlock()).getGoggleInfo(world, pos, state);
                        if (!info.isEmpty()) {
                            GlStateManager.pushMatrix();
                            float x = event.getResolution().getScaledWidth() / 2.0F;
                            float y = event.getResolution().getScaledHeight() / 2.0F;
                            mc.getRenderItem().renderItemIntoGUI(goggles, (int)x - 24, (int)y - 8);
                            GuiUtils.drawHoveringText(goggles, info, (int)x, (int)y,
                                    event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight(),
                                    Integer.MAX_VALUE, font
                            );
                            GlStateManager.disableLighting();
                            GlStateManager.popMatrix();
                        }
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void renderDebugGoggleOverlay(RenderGameOverlayEvent.Post event) {
        if (PerFrameDebugInfo.renderAgain) {
            PerFrameDebugInfo.renderAgain = false;
            final Minecraft mc = Minecraft.getMinecraft();
            final FontRenderer font = RenderMelon.getDefaultFontRenderer();
            if (mc.world != null && mc.getRenderManager().options != null && mc.getRenderManager().options.hideGUI) {
                font.drawStringWithShadow("CREATE LEGACY DEBUG INTERFACE", 2, 2, -1);
                font.drawStringWithShadow("TESRKinetics rendered: " + PerFrameDebugInfo.kineticTileEntitiesRendered,
                        2, 12, -1);
                font.drawStringWithShadow("Total contraption TEs in client world: " +
                                ((IExtensionWorld)mc.world).create$getContraptionTileEntities().size(),
                        2, 22, -1);
                font.drawStringWithShadow("Rendered contraptions (by layer): " +
                        ContraptionRendering.formatList(PerFrameDebugInfo.contraptionsRendered),
                        2, 32, -1);
                font.drawStringWithShadow("Skipped contraptions (by layer): " +
                                ContraptionRendering.formatList(PerFrameDebugInfo.contraptionsSkipped),
                        2, 41, -1);
                font.drawStringWithShadow("Model render time ms: " + (PerFrameDebugInfo.renderTimeMs()),
                        2, 51, -1);
                font.drawStringWithShadow("Fast model renderer: " + ClientConfig.fastKineticRendering,
                        2, 60, -1);
                PerFrameDebugInfo.reset();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void renderCardboardPlayer(RenderPlayerEvent.Pre event) {
        EntityPlayer player = event.getEntityPlayer();
        if (player.isSneaking()) {
            ItemStack boots = player.inventory.armorInventory.get(0);
            ItemStack leggings = player.inventory.armorInventory.get(1);
            ItemStack chestplate = player.inventory.armorInventory.get(2);
            ItemStack helmet = player.inventory.armorInventory.get(3);
            if (boots.isEmpty() || boots.getItem() != ItemInit.BOOTS_CARDBOARD) return;
            if (leggings.isEmpty() || leggings.getItem() != ItemInit.LEGGINGS_CARDBOARD) return;
            if (chestplate.isEmpty() || chestplate.getItem() != ItemInit.CHESTPLATE_CARDBOARD) return;
            if (helmet.isEmpty() || helmet.getItem() != ItemInit.HELMET_CARDBOARD) return;
            event.setCanceled(true);

            //TODO: render cardboard buddy
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void renderTickEvent(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            for (EntityContraptionBase entity : ContraptionRendering.CONTRAPTIONS_TO_REMOVE) {
                Contraption contraption = entity.attachedContraption();
                if (contraption != null && ContraptionRendering.available(contraption)) {
                    ContraptionRendering.contraptionFinalized(contraption);
                }
            }
            ContraptionRendering.CONTRAPTIONS_TO_REMOVE.clear();
        }
        if (event.phase == TickEvent.Phase.END) {
            PerFrameDebugInfo.renderAgain = true;
            for (EntityContraptionBase entity : ContraptionRendering.getCollectedContraptions()) {
                Contraption contraption = entity.attachedContraption();
                if (contraption != null && !ContraptionRendering.available(contraption)) {
                    ContraptionRendering.getList(contraption);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void addTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (SequenceRecipe.isInSequence(stack)) {
            NBTTagCompound data = stack.getSubCompound("SequencedAssembly");
            if (data == null) {
                event.getToolTip().add("[sequence error]");
                return;
            }
            String id = data.getString("id");
            int step = data.getInteger("step");
            SequenceRecipe recipe = SequencedRecipes.instance.getRecipe(id);
            List<String> tooltips = event.getToolTip();
            TextBuilder builder = new TextBuilder();
            builder.formatting(TextFormatting.GOLD);
            builder.translate("sequence.next_steps");
            int max = recipe.steps.size() * recipe.repetitions;
            for (int i = 0; i < 3; i++) {
                int s = step+i;
                if (s < max) {
                    String pre = s == step ? "->" : "  ";
                    SequenceStep next = recipe.getStep(s);
                    builder.enter().formatting(TextFormatting.AQUA).text(pre).text(SequenceRecipe.getFormat(next.name).getDisplayName(next));
                }
            }
            int remaining = max - (step+3);
            if (remaining > 0) {
                if (remaining > 1) {
                    builder.enter().formatting(TextFormatting.DARK_AQUA).translate("sequence.remaining_steps", remaining);
                } else {
                    builder.enter().formatting(TextFormatting.DARK_AQUA).translate("sequence.remaining_step");
                }
            }
            tooltips.addAll(builder.build());
        }
    }
}