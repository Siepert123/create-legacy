package com.melonstudios.createlegacy.tileentity.render;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.tileentity.TileEntityShaft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.nio.FloatBuffer;
import java.util.Random;

public class TileEntityShaftRenderer extends TileEntitySpecialRenderer<TileEntityShaft> {
    private static final ResourceLocation texture = new ResourceLocation(CreateLegacy.MOD_ID, "textures/block/block_brass.png");

    private static final Field fieldPos;

    private FloatBuffer buffer = GLAllocation.createDirectFloatBuffer(16);

    public TileEntityShaftRenderer() {
        buffer = GLAllocation.createDirectFloatBuffer(16);
        rendererDispatcher = TileEntityRendererDispatcher.instance;
    }

    static {
        fieldPos = ReflectionHelper.findField(ActiveRenderInfo.class, "position", "field_178811_e");
        fieldPos.setAccessible(true);
    }

    @Override
    public void render(TileEntityShaft te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        float rx = (float) rendererDispatcher.entityX;
        float ry = (float) rendererDispatcher.entityY;
        float rz = (float) rendererDispatcher.entityZ;

        Vec3d pos;
        try {
            pos = (Vec3d) fieldPos.get(null);
        } catch (IllegalAccessException e) {
            return;
        }

        GlStateManager.disableLighting();

        GlStateManager.pushMatrix();

        this.bindTexture(texture);


    }


    private FloatBuffer addToBuffer(float f, float f1, float f2, float f3) {
        buffer.clear();
        buffer.put(f).put(f1).put(f2).put(f3);
        buffer.flip();
        return buffer;
    }
}
