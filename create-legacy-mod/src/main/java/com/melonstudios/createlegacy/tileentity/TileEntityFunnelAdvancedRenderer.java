package com.melonstudios.createlegacy.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;

public class TileEntityFunnelAdvancedRenderer extends TileEntitySpecialRenderer<TileEntityFunnelAdvanced> {
    public TileEntityFunnelAdvancedRenderer() {
        super();
        this.rendererDispatcher = TileEntityRendererDispatcher.instance;
    }

    @Override
    public void render(TileEntityFunnelAdvanced te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        if (!te.getFilter().isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();
            GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
            GlStateManager.enableBlend();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
            GlStateManager.pushMatrix();

            IBakedModel model = ForgeHooksClient.handleCameraTransforms(
                    Minecraft.getMinecraft().getRenderItem()
                            .getItemModelWithOverrides(te.getFilter(), te.getWorld(), null),
                    ItemCameraTransforms.TransformType.NONE, false);
            GlStateManager.translate(x + 0.5, y + 0.8125, z + 0.5);
            GlStateManager.rotate(getAngle(te.facing()), 0, 1, 0);
            GlStateManager.scale(0.25f, 0.25f, 0.25f);

            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            Minecraft.getMinecraft().getRenderItem().renderItem(te.getFilter(), model);

            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    protected float getAngle(EnumFacing direction) {
        switch (direction) {
            case NORTH: return 0.0f;
            case EAST: return 270.0f;
            case SOUTH: return 180.0f;
            case WEST: return 90.0f;
        }
        return 0;
    }
}
