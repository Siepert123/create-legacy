package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.block.BlockRender;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKineticRenderer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.math.MathHelper;

public class TileEntityFlywheelRenderer extends AbstractTileEntityKineticRenderer<TileEntityFlywheel> {
    @Override
    public void render(TileEntityFlywheel te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        spinModel(te, x, y, z, partialTicks, te.facing().getAxis(), te.getAssociatedFlywheelPart());

        if (te.connects()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            switch (te.facing().rotateY()) {
                case NORTH: GlStateManager.translate(0, 0, -3); break;
                case EAST: GlStateManager.translate(3, 0, 0); break;
                case SOUTH: GlStateManager.translate(0, 0, 3); break;
                case WEST: GlStateManager.translate(-3, 0, 0); break;
            }
            modelRender(BlockRender.getRenderPart(BlockRender.Type.FURNACE_HAT));
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            float angle = calculateAngle(te, te.facing().getAxis(), partialTicks, 1, false);
            GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
            GlStateManager.rotate(te.getRotationDeg(), 0, 1, 0);
            GlStateManager.translate(-0.5, -0.5, -0.5);
            GlStateManager.pushMatrix();
            transformConnector(false, true, angle);
            modelRender(te.renderPart(0));
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            transformConnector(false, false, angle);
            modelRender(te.renderPart(1));
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            transformConnector(true, true, angle);
            modelRender(te.renderPart(2));
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            transformConnector(true, false, angle);
            modelRender(te.renderPart(3));
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
        }
    }

    protected void transformConnector(boolean upper, boolean rotating, float angle) {
        float shift = upper ? 1 / 4f : -1 / 8f;
        float offset = 1 / 4f;
        float radians = (float) (angle / 180 * Math.PI);
        float shifting = MathHelper.sin(radians) * shift + offset;

        float maxAngle = upper ? -5 : -15;
        float minAngle = upper ? -45 : 5;
        float barAngle = 0;

        if (rotating)
            barAngle = (float) MathHelper.clampedLerp(minAngle, maxAngle, (MathHelper.sin((float) (radians + Math.PI / 2)) + 1) / 2);

        float pivotX = (upper ? 8f : 3f) / 16;
        float pivotY = (upper ? 8f : 2f) / 16;
        float pivotZ = (upper ? 23f : 21.5f) / 16f;

        GlStateManager.translate(pivotX, pivotY, pivotZ + shifting);
        if (rotating)
            GlStateManager.rotate(barAngle, 1, 0, 0);
        GlStateManager.translate(-pivotX, -pivotY, -pivotZ);

        if (false && !upper)
            GlStateManager.translate(9 / 16f, 0, 0);
    }

    protected void modelRender(IBlockState state) {
        GlStateManager.pushMatrix();

        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
        GlStateManager.rotate(-90, 0, 1, 0);
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightness(model, state, 1.0f, true);

        GlStateManager.popMatrix();
    }
}
