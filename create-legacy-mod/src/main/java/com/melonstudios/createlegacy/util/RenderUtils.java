package com.melonstudios.createlegacy.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;

public final class RenderUtils {

    public static float smoothen(float previousAngle, float nextAngle, float partialTick) {
        float f1 = nextAngle * partialTick;
        float f2 = previousAngle * (1 - partialTick);
        return f1 + f2;
    }

    public static void renderItemLikeGround(TileEntity te, BlockPos pos, ItemStack stack, float partialTicks) {
        renderItemLikeGround(te, pos.getX(), pos.getY(), pos.getZ(), stack, partialTicks);
    }
    public static void renderItemLikeGround(TileEntity te, double x, double y, double z, ItemStack stack, float partialTicks) {
        if (!stack.isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();
            GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
            GlStateManager.enableBlend();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
            GlStateManager.pushMatrix();
            double offset = Math.sin((te.getWorld().getTotalWorldTime() + partialTicks) / 8) / 4.0;
            GlStateManager.translate(x + 0.5, y + 1.25 + offset, z + 0.5);
            GlStateManager.rotate((te.getWorld().getTotalWorldTime() + partialTicks) * 4, 0, 1, 0);

            IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, te.getWorld(), null);
            model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);

            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);

            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }


}
