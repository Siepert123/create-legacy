package com.melonstudios.createlegacy.tileentity;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class TileEntityChuteRenderer extends TileEntitySpecialRenderer<TileEntityChute> {
    public TileEntityChuteRenderer() {
        super();
        this.rendererDispatcher = TileEntityRendererDispatcher.instance;
    }

    @Override
    public void render(TileEntityChute te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    }
}
