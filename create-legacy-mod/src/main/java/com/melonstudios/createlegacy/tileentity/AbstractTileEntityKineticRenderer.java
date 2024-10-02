package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.CreateConfig;
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

    @Override
    public void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (CreateConfig.debug) {
            ITextComponent itextcomponent = te.getDisplayName();

            if (itextcomponent != null && this.rendererDispatcher.cameraHitResult != null && te.getPos().equals(this.rendererDispatcher.cameraHitResult.getBlockPos())) {
                this.setLightmapDisabled(true);
                this.drawNameplate(te, itextcomponent.getFormattedText(), x, y, z, 12);
                this.setLightmapDisabled(false);
            }
        }

        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    }

    protected final void spinModel(T te, double x, double y, double z, float partialTicks, EnumFacing.Axis axis, IBlockState state) {
        spinModel(te, x, y, z, partialTicks, axis, state, 1.0f);
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
     */
    protected void spinModel(T te, double x, double y, double z, float partialTicks, EnumFacing.Axis axis, IBlockState state, float markiplier) {
        GlStateManager.pushMatrix();

        GlStateManager.color(1, 1, 1, 1);

        GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);

        GlStateManager.rotate(calculateAngle(te, axis, partialTicks, markiplier),
                axis == EnumFacing.Axis.X ? 1 : 0,
                axis == EnumFacing.Axis.Y ? 1 : 0,
                axis == EnumFacing.Axis.Z ? 1 : 0);

        GlStateManager.translate(-0.5, -0.5, 0.5);

        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightness(model, state, 1.0f, true);

        GlStateManager.popMatrix();
    }

    protected final float calculateAngle(T te, EnumFacing.Axis axis, float partialTicks, float markiplier) {
        if (te.speed() == 0) return te.shifted(axis) ? 22.5f : 0;
        long time = te.getWorld().getTotalWorldTime();

        return (((time + partialTicks) % 360) * 0.3f * te.speed() * markiplier) + (te.shifted(axis) ? 22.5f : 0);
    }
}
