package com.melonstudios.createlegacy.tileentity;

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
        GlStateManager.translate(x + 0.5, y + (14.5f / 16f), z + 0.5);
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.rotate(90, 1, 0, 0);

        if (!te.getStack().isEmpty()) {
            for (int i = 0; i < getStackSize(te.getStack().getCount()); i++) {
                GlStateManager.pushMatrix();
                GlStateManager.rotate(i, 0, 0, 1);
                GlStateManager.translate(0, 0, -i / 16.0);
                IBakedModel model = Minecraft.getMinecraft().getRenderItem()
                        .getItemModelWithOverrides(te.getStack(), te.getWorld(), null);
                model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.NONE, false);

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
        GlStateManager.translate(x + 0.2, y + (14.5f / 16f), z + 0.2);
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.rotate(35, 0, 1, 0);
        GlStateManager.rotate(90, 1, 0, 0);

        if (!te.getOutput().isEmpty()) {
            for (int i = 0; i < getStackSize(te.getOutput().getCount()); i++) {
                GlStateManager.pushMatrix();
                GlStateManager.rotate(i, 0, 0, 1);
                GlStateManager.translate(0, 0, -i / 16.0);
                IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(te.getOutput(), te.getWorld(), null);
                model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.NONE, false);

                Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                Minecraft.getMinecraft().getRenderItem().renderItem(te.getOutput(), model);
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
        GlStateManager.translate(x + 0.8, y + (14.5f / 16f), z + 0.8);
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.rotate(-35, 0, 1, 0);
        GlStateManager.rotate(90, 1, 0, 0);

        if (!te.getOutput2().isEmpty()) {
            for (int i = 0; i < getStackSize(te.getOutput2().getCount()); i++) {
                GlStateManager.pushMatrix();
                GlStateManager.rotate(i, 0, 0, 1);
                GlStateManager.translate(0, 0, -i / 16.0);
                IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(te.getOutput2(), te.getWorld(), null);
                model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.NONE, false);

                Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                Minecraft.getMinecraft().getRenderItem().renderItem(te.getOutput2(), model);
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
