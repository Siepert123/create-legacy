package nl.melonstudios.create.proxy;

import com.melonstudios.melonlib.recipe.RecipeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleBreaking;
import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nl.melonstudios.create.CreateLegacy;
import nl.melonstudios.create.block.actor.BlockGauge;
import nl.melonstudios.create.entity.*;
import nl.melonstudios.create.init.SoundInit;
import nl.melonstudios.create.kinetics.FastStateRendering;
import nl.melonstudios.create.recipe.client.*;
import nl.melonstudios.create.tesr.*;
import nl.melonstudios.create.tesr.actor.*;
import nl.melonstudios.create.tesr.funnel.TESRFunnelWallAdvanced;
import nl.melonstudios.create.tesr.generator.*;
import nl.melonstudios.create.tileentity.*;
import nl.melonstudios.create.tileentity.actor.*;
import nl.melonstudios.create.tileentity.funnel.TileEntityFunnelWall;
import nl.melonstudios.create.tileentity.funnel.TileEntityFunnelWallAdvanced;
import nl.melonstudios.create.tileentity.generator.*;
import nl.melonstudios.create.tileentity.redstone.TileEntityRedstoneLinker;
import nl.melonstudios.ponder.PonderRegistry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {
    @Override
    public Side getSide() {
        return Side.CLIENT;
    }

    @Override
    public void setItemModel(Item item, int meta, String file) {
        ModelLoader.setCustomModelResourceLocation(item, meta,
                new ModelResourceLocation(new ResourceLocation("create", file), "inventory"));
    }

    @Override
    public void clientPreInit(FMLPreInitializationEvent event) {
        FastStateRendering.load();
    }

    @Override
    public void registerTileEntities() {
        this.registerTESR(TileEntityShaft.class, "shaft", new TESRShaft());
        this.registerTESR(TileEntityCogwheel.class, "cogwheel", new TESRCogwheel());
        this.registerTESR(TileEntityGearbox.class, "gearbox", new TESRGearbox<>());
        this.registerTESR(TileEntityGearshift.class, "gearshift", new TESRSplitShaft<>());
        this.registerTESR(TileEntityClutch.class, "clutch", new TESRSplitShaft<>());
        this.registerTESR(TileEntityHandCrank.class, "hand_crank", new TESRHandCrank());
        this.registerTESR(TileEntityWaterWheel.class, "water_wheel", new TESRWaterWheel());
        this.registerTESR(TileEntityWaterWheelTemp.class, "water_wheel_temp", null);
        this.registerTESR(TileEntityFurnaceEngine.class, "furnace_engine", null);
        this.registerTESR(TileEntityFurnaceFlywheel.class, "furnace_flywheel", new TESRFurnaceFlywheel());
        this.registerTESR(TileEntityCreativeMotor.class, "creative_motor", new TESRCreativeMotor());
        this.registerTESR(TileEntityTurntable.class, "turntable", new TESRTurntable());
        this.registerTESR(TileEntityBearing.class, "bearing", new TESRBearing());
        this.registerTESR(TileEntityBearingWindmill.class, "bearing_windmill", new TESRBearingWindmill<>());
        this.registerTESR(TileEntityDistanceController.class, "distance_controller", new TESRDistanceController());
        this.registerTESR(TileEntityMechanicalPiston.class, "mechanical_piston", new TESRMechanicalPiston<>());
        this.registerTESR(TileEntitySpeedometer.class, "speedometer", new TESRGauge<>(BlockGauge.Type.SPEED));
        this.registerTESR(TileEntityStressometer.class, "stressometer", new TESRGauge<>(BlockGauge.Type.STRESS));
        this.registerTESR(TileEntityPress.class, "press", new TESRPress<>());
        this.registerTESR(TileEntityMixer.class, "mixer", new TESRMixer());
        this.registerTESR(TileEntityDrill.class, "drill", new TESRDrill<>());
        this.registerTESR(TileEntitySaw.class, "saw", new TESRSaw());
        this.registerTESR(TileEntitySawProcessing.class, "saw_processing", new TESRSawProcessing());
        this.registerTESR(TileEntityDeployer.class, "deployer", new TESRDeployer());
        this.registerTESR(TileEntityPlough.class, "plough", null);
        this.registerTESR(TileEntityHarvester.class, "harvester", new TESRHarvester());
        this.registerTESR(TileEntityStorageInterface.class, "storage_interface", new TESRContraptionInterface<>());
        this.registerTESR(TileEntityMillstone.class, "millstone", new TESRMillstone());
        this.registerTESR(TileEntityCrafter.class, "crafter", new TESRCrafter());
        this.registerTESR(TileEntityBlazeBurner.class, "blaze_burner", new TESRBlazeBurner());
        this.registerTESR(TileEntityDepot.class, "depot", new TESRDepot());
        this.registerTESR(TileEntityBasin.class, "basin", new TESRBasin());
        this.registerTESR(TileEntityChute.class, "chute", new TESRChute());
        this.registerTESR(TileEntityBeltStraight.class, "belt_straight", new TESRBeltStraight());
        this.registerTESR(TileEntityBeltDiagonal.class, "belt_diagonal", null);
        this.registerTESR(TileEntityItemDrain.class, "item_drain", new TESRItemDrain());
        this.registerTESR(TileEntityFunnelWall.class, "funnel_wall", null);
        this.registerTESR(TileEntityFunnelWallAdvanced.class, "funnel_wall_advanced", new TESRFunnelWallAdvanced());
        this.registerTESR(TileEntityRedstoneLinker.class, "redstone_linker", null);
    }

    @Override
    public void registerEntityRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityGlue.class, RenderGlue::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPouf.class, RenderNone::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityContraptionBearing.class, RenderContraptionBearing::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityContraptionPiston.class, RenderNone::new);
    }

    @Override
    public void pork() {
        TESRKineticBase.pork();
    }

    private void registerTESR(Class<? extends TileEntity> te, String name, @Nullable TileEntitySpecialRenderer<?> renderer) {
        GameRegistry.registerTileEntity(te, create(name));
        if (renderer != null) TileEntityRendererDispatcher.instance.renderers.put(te, renderer);
    }

    @Override
    public void spawnRedstoneFX(double x, double y, double z, double mx, double my, double mz,
                                float size, float r, float g, float b) {
        ParticleRedstone particle = (ParticleRedstone) Objects.requireNonNull(Minecraft.getMinecraft().effectRenderer
                .spawnEffectParticle(EnumParticleTypes.REDSTONE.getParticleID(), x, y, z, mx, my, mz));
        particle.setRBGColorF(r, g, b);
        particle.multipleParticleScaleBy(size);
    }

    @Override
    public void spawnItemFX(double x, double y, double z, double mx, double my, double mz, int id, int meta) {
        Minecraft.getMinecraft().effectRenderer.spawnEffectParticle(
                EnumParticleTypes.ITEM_CRACK.getParticleID(),
                x, y, z, mx, my, mz, id, meta
        );
    }

    @Override
    public void millstoneFX(TileEntityMillstone millstone) {
        BlockPos pos = millstone.getPos();
        Random rnd = millstone.getWorld().rand;
        ItemStack in = millstone.input;
        if (in.isEmpty()) return;
        int id = Item.getIdFromItem(in.getItem());
        int meta = in.getMetadata();
        for (int i = 0; i < 5; i++) {
            millstone.getWorld().playSound(Minecraft.getMinecraft().player, pos,
                    SoundInit.block_millstone_ambient, SoundCategory.BLOCKS,
                    0.35F, 0.8F + millstone.getWorld().rand.nextFloat() * 0.3F
            );
            double xOffset = rnd.nextDouble() - 0.5;
            double zOffset = rnd.nextDouble() - 0.5;
            Minecraft.getMinecraft().effectRenderer
                    .spawnEffectParticle(EnumParticleTypes.ITEM_CRACK.getParticleID(),
                            pos.getX() + xOffset + 0.5, pos.getY() + 0.5, pos.getZ() + zOffset + 0.5,
                            xOffset * 0.2, 0.1, zOffset * 0.2, id, meta);
            Minecraft.getMinecraft().effectRenderer
                    .spawnEffectParticle(EnumParticleTypes.CRIT.getParticleID(),
                            pos.getX() + rnd.nextDouble(), pos.getY() + 0.5, pos.getZ() + rnd.nextDouble(),
                            0, 0, 0);
        }
    }
    @Override
    public void mixerFX(TileEntityBasin basin, double x, double y, double z) {
        List<FluidStack> fluids = basin.fluid.getHandlers().stream().map(FluidTank::getFluid).collect(Collectors.toList());
        TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();
        double offset = CreateLegacy.rand.nextDouble();
        if (!fluids.isEmpty()) {
            for (int i = 0; i < 5; i++) {
                FluidStack fluid = fluids.get(CreateLegacy.rand.nextInt(fluids.size()));
                TextureAtlasSprite sprite = map.getAtlasSprite(fluid.getFluid().getStill(fluid).toString());
                double angle = (1.2566370614359172) * (i+offset);
                double vx = Math.sin(angle) * 0.2;
                double vz = Math.cos(angle) * 0.2;
                this.getTexturePieceParticle(x, y, z, vx, 0.5, vz, sprite);
            }
        }
        if (!basin.inventory.isEmpty()) {
            for (int i = 0; i < 5; i++) {
                ItemStack item = basin.inventory.get(CreateLegacy.rand.nextInt(basin.inventory.size()));
                double angle = (1.2566370614359172) * (i+offset+0.1);
                double vx = Math.sin(angle) * 0.2;
                double vz = Math.cos(angle) * 0.2;
                this.spawnItemFX(x, y, z, vx, 0.5, vz, item);
            }
        }
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (basin.hasAnyFluid()) {
            basin.getWorld().playSound(player, x, y, z, SoundEvents.ENTITY_BOAT_PADDLE_WATER, SoundCategory.BLOCKS, 0.5F, 0.5F);
        }
        basin.getWorld().playSound(player, x, y, z, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 0.5F, 0.5F);
    }

    public void getTexturePieceParticle(double x, double y, double z, double vx, double vy, double vz, TextureAtlasSprite sprite) {
        ParticleBreaking particle = (ParticleBreaking) Minecraft.getMinecraft().effectRenderer
                .spawnEffectParticle(EnumParticleTypes.ITEM_CRACK.getParticleID(), x, y, z, vx, vy, vz, 0, 0);
        Objects.requireNonNull(particle, "null particle!");
        particle.setParticleTexture(sprite);
    }

    @Override
    public void initiatePonders() {
        PonderRegistry.bootstrap();
    }

    @Override
    public void registerRecipeTypes() {
        super.registerRecipeTypes();
        RecipeRegistry.registerClient("create:pressing", PressingRecipesClient.instance);
        RecipeRegistry.registerClient("create:cutting", CuttingRecipesClient.instance);
        RecipeRegistry.registerClient("create:mixing", MixingRecipesClient.instance);
        RecipeRegistry.registerClient("create:deploying", DeployingRecipesClient.instance);
        RecipeRegistry.registerClient("create:sequence", SequencedRecipesClient.instance);
    }
}
