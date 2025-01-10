package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKineticRenderer;
import com.melonstudios.createlegacy.util.RenderUtil;
import net.minecraft.block.state.IBlockState;

public class TileEntityCreativeMotorRenderer extends AbstractTileEntityKineticRenderer<TileEntityCreativeMotor> {
    @Override
    public void render(TileEntityCreativeMotor te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        IBlockState shaft = te.getAssociatedShaftPart();

        spinModel(te, x, y, z, partialTicks, te.facing().getAxis(), shaft);

        String s = String.valueOf((int)te.generatedRPM());
        switch (te.facing()) {
            case NORTH: {
                RenderUtil.renderText(rendererDispatcher.fontRenderer, s, (float) x + 0.5f, (float) (y + 0.5f), (float) z + 1,
                        0, 180, 0, true, true);
                break;
            }
            case SOUTH: {
                RenderUtil.renderText(rendererDispatcher.fontRenderer, s, (float) x + 0.5f, (float) y + 0.5f, (float) z,
                        0, 0, 0, true, true);
                break;
            }
            case EAST: {
                RenderUtil.renderText(rendererDispatcher.fontRenderer, s, (float) x, (float) y + 0.5f, (float) z + 0.5f,
                        0, 90, 0, true, true);
                break;
            }
            case WEST: {
                RenderUtil.renderText(rendererDispatcher.fontRenderer, s, (float) x + 1, (float) y + 0.5f, (float) z + 0.5f,
                        0, 270, 0, true, true);
                break;
            }
            case DOWN: {
                RenderUtil.renderText(rendererDispatcher.fontRenderer, s, (float) x + 0.5f, (float) y + 1, (float) z + 0.5f,
                        0, 0, 90, true, true);
                break;
            }
            case UP: {
                RenderUtil.renderText(rendererDispatcher.fontRenderer, s, (float) x + 0.5f, (float) y, (float) z + 0.5f,
                        0, 0, -90, true, true);
                break;
            }
        }
    }
}
