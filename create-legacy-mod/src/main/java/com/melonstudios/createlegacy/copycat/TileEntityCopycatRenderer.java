package com.melonstudios.createlegacy.copycat;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

/**
 * The renderer for every copycat block. The actual rendering is done in the subclasses of {@link BlockCopycat}!
 */
public final class TileEntityCopycatRenderer extends TileEntitySpecialRenderer<TileEntityCopycat> {
    public TileEntityCopycatRenderer() {
        this.rendererDispatcher = TileEntityRendererDispatcher.instance;
    }
    @Override
    public void render(TileEntityCopycat te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (te.mustRender()) {
            GlStateManager.pushMatrix();
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            IBlockState state = te.getWorld().getBlockState(te.getPos());
            ((BlockCopycat) state.getBlock()).render(state, te, x, y, z);
            GlStateManager.popMatrix();
        }
    }
}
