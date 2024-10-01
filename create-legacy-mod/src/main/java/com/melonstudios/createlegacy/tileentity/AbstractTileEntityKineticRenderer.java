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

    protected void spinModel(T te, double x, double y, double z, float partialTicks, EnumFacing.Axis axis, IBlockState state) {
        GlStateManager.pushMatrix();

        GlStateManager.color(1, 1, 1, 1);

        GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);

        long time = te.getWorld().getTotalWorldTime();
        float angle = (time % 36000) / 25f * te.speed();
        GlStateManager.rotate(angle,
                axis == EnumFacing.Axis.X ? 1 : 0,
                axis == EnumFacing.Axis.Y ? 1 : 0,
                axis == EnumFacing.Axis.Z ? 1 : 0);

        GlStateManager.translate(-0.5, -0.5, 0.5);

        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightness(model, state, 1.0f, true);

        GlStateManager.popMatrix();
    }
}
