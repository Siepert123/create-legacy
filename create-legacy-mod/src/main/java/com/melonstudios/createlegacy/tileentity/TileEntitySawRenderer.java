package com.melonstudios.createlegacy.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.BlockPos;

public class TileEntitySawRenderer extends TileEntitySpecialRenderer<TileEntitySaw> {
    public TileEntitySawRenderer() {
        super();
        this.rendererDispatcher = TileEntityRendererDispatcher.instance;
    }

    @Override
    public void render(TileEntitySaw te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        BlockPos pos = te.getPos();
        IBlockState state = te.getWorld().getBlockState(pos);

        GlStateManager.pushMatrix();

        GlStateManager.translate(x + 0.5, y, z + 0.5);

        long time = te.getWorld().getTotalWorldTime();
        float angle = (time % 360) * 10f;
        GlStateManager.rotate(angle, 0, 1, 0);

        GlStateManager.translate(-0.5, 0, 0.5);

        BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        IBakedModel bakedModel = dispatcher.getModelForState(state);
        dispatcher.getBlockModelRenderer().renderModelBrightness(bakedModel, state, 1.0f, true);

        GlStateManager.popMatrix();
    }
}
