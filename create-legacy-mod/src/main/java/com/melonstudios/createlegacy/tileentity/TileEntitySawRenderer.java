package com.melonstudios.createlegacy.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TileEntitySawRenderer extends AbstractTileEntityKineticRenderer<TileEntitySaw> {
    public TileEntitySawRenderer() {
        super();
        this.rendererDispatcher = TileEntityRendererDispatcher.instance;
    }

    @Override
    public void render(TileEntitySaw te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        BlockPos pos = te.getPos();
        IBlockState state = te.getWorld().getBlockState(pos);

        spinModel(te, x, y, z, partialTicks, EnumFacing.Axis.X, state);
    }
}
