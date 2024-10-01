package com.melonstudios.createlegacy.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;

public abstract class AbstractTileEntityKineticRenderer<T extends AbstractTileEntityKinetic> extends TileEntitySpecialRenderer<T> {
    protected void spinModel(T te, double x, double y, double z, float partialTicks, EnumFacing.Axis axis, IBlockState state) {
        GlStateManager.pushMatrix();

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
