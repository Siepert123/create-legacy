package com.melonstudios.createlegacy.tileentity.abstractions;

import com.melonstudios.createlegacy.CreateConfig;
import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.block.kinetic.BlockRotator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;

/**
 * Parent of ALL Kinetic Renderers.
 * PLEASE call the super!!
 * @param <T> TileEntity
 * @since 0.1.0
 * @author siepert123
 */
public abstract class AbstractTileEntityKineticRenderer<T extends AbstractTileEntityKinetic> extends TileEntitySpecialRenderer<T> {
    protected AbstractTileEntityKineticRenderer() {
        super();
        this.rendererDispatcher = TileEntityRendererDispatcher.instance;
    }

    private boolean renderDebugText = true;
    protected void disableDebugText() {
        renderDebugText = false;
    }

    @Override
    public void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (CreateConfig.debug && renderDebugText) {
            ITextComponent itextcomponent = te.getDisplayName();

            if (itextcomponent != null && this.rendererDispatcher.cameraHitResult != null && te.getPos().equals(this.rendererDispatcher.cameraHitResult.getBlockPos())) {
                this.setLightmapDisabled(true);
                this.drawNameplate(te, itextcomponent.getFormattedText(), x, y, z, 12);
                this.setLightmapDisabled(false);
            }
        }

        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    }

    protected final void spinModel(AbstractTileEntityKinetic te, double x, double y, double z, float partialTicks, EnumFacing.Axis axis, IBlockState state) {
        spinModel(te, x, y, z, partialTicks, axis, state, 1.0f);
    }

    protected final void spinShaftModel(AbstractTileEntityKinetic te, double x, double y, double z, float partialTicks, EnumFacing.Axis axis) {
        spinModel(te, x, y, z, partialTicks, axis, ModBlocks.ROTATOR.getDefaultState()
                .withProperty(BlockRotator.VARIANT, BlockRotator.Variant.SHAFT)
                .withProperty(BlockRotator.AXIS, axis));
    }
    protected final void spinCogModel(AbstractTileEntityKinetic te, double x, double y, double z, float partialTicks, EnumFacing.Axis axis) {
        spinModel(te, x, y, z, partialTicks, axis, ModBlocks.ROTATOR.getDefaultState()
                .withProperty(BlockRotator.VARIANT, BlockRotator.Variant.COG)
                .withProperty(BlockRotator.AXIS, axis));
    }

    /**
     * Rotates a model along an axis at the TE speed
     * @param te TileEntity
     * @param x X-coordinate
     * @param y Y-coordinate
     * @param z Z-coordinate
     * @param partialTicks Partial tick
     * @param axis Axis to rotate the model on
     * @param state Blockstate to be rotated
     * @param markiplier speed multiplier
     */
    protected void spinModel(AbstractTileEntityKinetic te, double x, double y, double z, float partialTicks, EnumFacing.Axis axis, IBlockState state, float markiplier) {
        rotateModel(calculateAngle(te, axis, partialTicks, markiplier, true), x, y, z, axis, state);
    }

    protected void rotateModel(float angle, double x, double y, double z, EnumFacing.Axis axis, IBlockState state) {
        GlStateManager.pushMatrix();

        GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);

        GlStateManager.rotate(angle,
                axis == EnumFacing.Axis.X ? 1 : 0,
                axis == EnumFacing.Axis.Y ? 1 : 0,
                axis == EnumFacing.Axis.Z ? 1 : 0);

        GlStateManager.translate(-0.5, -0.5, -0.5);

        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
        GlStateManager.rotate(-90, 0, 1, 0);
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightness(model, state, 1.0f, true);

        GlStateManager.popMatrix();
    }

    protected void renderModel(double x, double y, double z, IBlockState state) {
        rotateModel(0.0f, x, y, z, EnumFacing.Axis.Y, state);
    }

    protected final float calculateAngle(AbstractTileEntityKinetic te, EnumFacing.Axis axis, float partialTicks, float markiplier, boolean addOffset) {
        if (te.speed() == 0) return te.shifted(axis) && addOffset ? 22.5f : 0;
        long time = te.getWorld().getTotalWorldTime();

        return ((((time + partialTicks) * 0.3f * te.speed() * markiplier) % 360)) + (te.shifted(axis) && addOffset ? 22.5f : 0);
    }
}
