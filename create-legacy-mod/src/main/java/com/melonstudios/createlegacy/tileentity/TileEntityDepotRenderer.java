package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;

public class TileEntityDepotRenderer extends TileEntitySpecialRenderer<TileEntityDepot> {
    public TileEntityDepotRenderer() {
        this.rendererDispatcher = TileEntityRendererDispatcher.instance;
    }

    @Override
    public void render(TileEntityDepot te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        String s = String.format("%s & %s", te.getStack(), te.getOutput());

        setLightmapDisabled(true);
        RenderUtil.renderText(rendererDispatcher.fontRenderer, s,
                (float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f, 0,
                te.getWorld().getTotalWorldTime() + partialTicks,
                0, true, true);
        RenderUtil.renderText(rendererDispatcher.fontRenderer, s,
                (float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f, 0,
                te.getWorld().getTotalWorldTime() + 180 + partialTicks,
                0, true, true);
        setLightmapDisabled(false);

        renderItem(te, x, y, z);
    }

    protected void renderItem(TileEntityDepot te, double x, double y, double z) {
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
        GlStateManager.enableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y + (15f / 16f), z + 0.25 + 0.0625f);
        GlStateManager.rotate(90, 1, 0, 0);

        if (!te.getStack().isEmpty()) {
            for (int i = 0; i < getStackSize(te.getStack().getCount()); i++) {
                GlStateManager.pushMatrix();
                GlStateManager.rotate(i, 0, 0, 1);
                GlStateManager.translate(0, 0, -i / 32.0);
                IBakedModel model = Minecraft.getMinecraft().getRenderItem()
                        .getItemModelWithOverrides(te.getStack(), te.getWorld(), null);
                model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);

                Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                Minecraft.getMinecraft().getRenderItem().renderItem(te.getStack(), model);
                GlStateManager.popMatrix();
            }
        }
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();



        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
        GlStateManager.enableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.25, y + (15f / 16f) - 0.05, z + 0.25 + 0.0625f + 0.5);
        GlStateManager.rotate(90, 1, 0, 0);

        if (!te.getOutput().isEmpty()) {
            for (int i = 0; i < getStackSize(te.getOutput().getCount()); i++) {
                GlStateManager.pushMatrix();
                GlStateManager.rotate(i, 0, 0, 1);
                GlStateManager.translate(0, 0, -i / 32.0);
                IBakedModel model2 = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(te.getOutput(), te.getWorld(), null);
                model2 = ForgeHooksClient.handleCameraTransforms(model2, ItemCameraTransforms.TransformType.GROUND, false);

                Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                Minecraft.getMinecraft().getRenderItem().renderItem(te.getOutput(), model2);
                GlStateManager.popMatrix();
            }
        }
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    protected int getStackSize(int items) {
        return Math.max(items / 8, 1);
    }
}
