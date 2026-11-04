package nl.melonstudios.create.tesr.funnel;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import nl.melonstudios.create.tesr.TESRBase;
import nl.melonstudios.create.tileentity.funnel.TileEntityFunnelWallAdvanced;
import nl.melonstudios.create.util.RenderUtils;
import nl.melonstudios.create.util.SubInteractionBox;

public class TESRFunnelWallAdvanced extends TESRBase<TileEntityFunnelWallAdvanced> {
    @Override
    public void render(TileEntityFunnelWallAdvanced te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (x*x + y*y + z*z > 4096) return;
        RenderUtils.prepare(x, y, z);
        if (te.filter != null) {
            ItemStack stack = te.filter.getRenderItem();
            if (!stack.isEmpty()) {
                this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                GlStateManager.pushMatrix();
                RenderHelper.enableStandardItemLighting();
                GlStateManager.enableRescaleNormal();

                IBakedModel model = this.mc.getRenderItem().getItemModelWithOverrides(stack, this.getWorld(), null);
                GlStateManager.translate(0.5F, 0.75F, 0.5F);

                switch (te.getFacing(te.getBlockMetadata())) {
                    case EAST:
                        GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                        break;
                    case NORTH:
                        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                        break;
                    case WEST:
                        GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                        break;
                }
                GlStateManager.translate(0.0F, 0.0F, 0.075F);
                GlStateManager.rotate(22.5F, 1.0F, 0.0F, 0.0F);
                GlStateManager.scale(0.25F, 0.25F, 0.25F);
                this.mc.getRenderItem().renderItem(stack, model);

                GlStateManager.disableRescaleNormal();
                RenderHelper.disableStandardItemLighting();
                GlStateManager.popMatrix();
            }
        }
        GlStateManager.disableBlend();
        SubInteractionBox.renderPotentialInteractionBoxes(this.mc.objectMouseOver, te);
        RenderUtils.finish();
    }
}
