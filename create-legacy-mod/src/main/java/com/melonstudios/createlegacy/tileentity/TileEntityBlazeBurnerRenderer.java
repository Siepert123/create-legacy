package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.util.RenderUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class TileEntityBlazeBurnerRenderer extends TileEntitySpecialRenderer<TileEntityBlazeBurner> {
    public TileEntityBlazeBurnerRenderer() {
        super();
        this.rendererDispatcher = TileEntityRendererDispatcher.instance;
    }

    @Override
    public void render(TileEntityBlazeBurner te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        rotateModel(-rendererDispatcher.entityYaw,
                x, y + (Math.sin(Math.toRadians(te.getWorld().getTotalWorldTime() + partialTicks)) / 16.0), z,
                te.getAssociatedBlazePart());
    }

    private void rotateModel(float angle, double x, double y, double z, IBlockState state) {
        GlStateManager.pushMatrix();

        GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);

        GlStateManager.rotate(angle, 0, 1, 0);

        GlStateManager.translate(-0.5, -0.5, -0.5);

        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
        GlStateManager.rotate(-90, 0, 1, 0);
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightness(model, state, 1.0f, true);

        GlStateManager.popMatrix();
    }
}
