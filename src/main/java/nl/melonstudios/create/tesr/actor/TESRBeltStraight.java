package nl.melonstudios.create.tesr.actor;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nl.melonstudios.create.block.actor.BlockBeltStraight;
import nl.melonstudios.create.block.state.EnumBeltPart;
import nl.melonstudios.create.tileentity.actor.TileEntityBeltStraight;
import nl.melonstudios.create.util.RenderUtils;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TESRBeltStraight extends TESRBeltBase<TileEntityBeltStraight> {
    public TESRBeltStraight() {
        super();
    }

    @Override
    protected void render(TileEntityBeltStraight te, float pt, float alpha) {
        super.render(te, pt, alpha);

        RenderUtils.prepare(0, 0, 0);

        IBlockState state = te.getState();
        EnumBeltPart part = state.getValue(BlockBeltStraight.PART);

        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.enableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder renderer = tessellator.getBuffer();
        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        TextureAtlasSprite sprite = this.mc.getTextureMapBlocks()
                .getAtlasSprite("create:block/belt_scroll" + (te.color != null ? "_" + te.color.getDyeColorName() : ""));

        final int r = 255;
        final int g = 255;
        final int b = 255;
        
        int brightness = this.getWorld().getCombinedLight(te.getPos(), 0);
        int l1 = brightness >> 0x10 & 0xFFFF;
        int l2 = brightness & 0xFFFF;

        double scroll = ((te.getSpeed() * 0.0625 * 0.025) * (this.getAdjustedTime(pt)));
        scroll = scroll - Math.floor(scroll);
        if (scroll < 0) scroll += 1;

        double ascroll = ((te.getSpeed() * 0.0625 * -0.025) * (this.getAdjustedTime(pt)));
        ascroll = ascroll - Math.floor(ascroll);
        if (ascroll < 0) ascroll += 1;

        if (state.getValue(BlockBeltStraight.VERTICAL)) {
            if (state.getValue(BlockBeltStraight.AXIS) == EnumFacing.Axis.X) {
                RenderUtils.renderScrollingQuad(renderer, sprite,
                        0.25, 0.0, 0.0625, 0.5, 1.0, 0.875, EnumFacing.EAST,
                        r, g, b, 255, l1, l2,
                        scroll, false, false
                );
                RenderUtils.renderScrollingQuad(renderer, sprite,
                        0.75-0.125, 0.0, 0.0625, -0.75, 1.0, 0.875, EnumFacing.WEST,
                        255, 255, 255, 255, l1, l2,
                        scroll, false, false
                );
                RenderUtils.renderScrollingQuad(renderer, sprite,
                        0.25, 0.0, 0.0625, 0.5, 1.0, 0.875, EnumFacing.WEST,
                        r, g, b, 255, l1, l2,
                        ascroll, false, false
                );
                RenderUtils.renderScrollingQuad(renderer, sprite,
                        0.25, 0.0, 0.0625, 0.125, 1.0, 0.875, EnumFacing.EAST,
                        255, 255, 255, 255, l1, l2,
                        ascroll, false, false
                );
                if (part == EnumBeltPart.END) {
                    RenderUtils.renderScrollingQuad(renderer, sprite,
                            0.25, 0.0, 0.0625, 0.5, 1.0, 0.875, EnumFacing.UP,
                            r, g, b, 255, l1, l2,
                            ascroll, true, false
                    );
                    RenderUtils.renderScrollingQuad(renderer, sprite,
                            0.25, 0.875, 0.0625, 0.5, -0.75, 0.875, EnumFacing.DOWN,
                            255, 255, 255, 255, l1, l2,
                            scroll, true, false
                    );
                }
                if (part == EnumBeltPart.START) {
                    RenderUtils.renderScrollingQuad(renderer, sprite,
                            0.25, 0.0, 0.0625, 0.5, 1.0, 0.875, EnumFacing.DOWN,
                            r, g, b, 255, l1, l2,
                            ascroll, true, false
                    );
                    RenderUtils.renderScrollingQuad(renderer, sprite,
                            0.25, 0.875, 0.0625, 0.5, -0.75, 0.875, EnumFacing.UP,
                            255, 255, 255, 255, l1, l2,
                            scroll, true, false
                    );
                }
            } else {
                RenderUtils.renderScrollingQuad(renderer, sprite,
                        0.0625, 0.0, 0.25, 0.875, 1.0, 0.5, EnumFacing.NORTH,
                        r, g, b, 255, l1, l2,
                        scroll, false, false
                );
                RenderUtils.renderScrollingQuad(renderer, sprite,
                        0.0625, 0.0, 0.25, 0.875, 1.0, 0.125, EnumFacing.SOUTH,
                        255, 255, 255, 255, l1, l2,
                        scroll, false, false
                );
                RenderUtils.renderScrollingQuad(renderer, sprite,
                        0.0625, 0.0, 0.25, 0.875, 1.0, 0.5, EnumFacing.SOUTH,
                        r, g, b, 255, l1, l2,
                        ascroll, false, false
                );
                RenderUtils.renderScrollingQuad(renderer, sprite,
                        0.0625, 0.0, 0.75-0.125, 0.875, 1.0, 0.125, EnumFacing.NORTH,
                        255, 255, 255, 255, l1, l2,
                        ascroll, false, false
                );
                if (part == EnumBeltPart.END) {
                    RenderUtils.renderScrollingQuad(renderer, sprite,
                            0.0625, 0.0, 0.25, 0.875, 1.0, 0.5, EnumFacing.UP,
                            r, g, b, 255, l1, l2,
                            ascroll, false, false
                    );
                    RenderUtils.renderScrollingQuad(renderer, sprite,
                            0.0625, 0.875, 0.25, 0.875, -0.75, 0.5, EnumFacing.DOWN,
                            255, 255, 255, 255, l1, l2,
                            ascroll, false, false
                    );
                }
                if (part == EnumBeltPart.START) {
                    RenderUtils.renderScrollingQuad(renderer, sprite,
                            0.0625, 0.0, 0.25, 0.875, 1.0, 0.5, EnumFacing.DOWN,
                            r, g, b, 255, l1, l2,
                            scroll, false, false
                    );
                    RenderUtils.renderScrollingQuad(renderer, sprite,
                            0.0625, 0.875, 0.25, 0.875, -0.75, 0.5, EnumFacing.UP,
                            255, 255, 255, 255, l1, l2,
                            scroll, false, false
                    );
                }
            }
        } else {
            if (state.getValue(BlockBeltStraight.AXIS) == EnumFacing.Axis.X) {
                RenderUtils.renderScrollingQuad(renderer, sprite,
                        0.0, 0.25, 0.0625, 1.0, 0.5, 0.875, EnumFacing.UP,
                        r, g, b, 255, l1, l2,
                        ascroll, true, false
                );
                RenderUtils.renderScrollingQuad(renderer, sprite,
                        0.0, 0.75-0.125, 0.0625, 1.0, 0.125, 0.875, EnumFacing.DOWN,
                        255, 255, 255, 255, l1, l2,
                        scroll, true, false
                );
                RenderUtils.renderScrollingQuad(renderer, sprite,
                        0.0, 0.25, 0.0625, 1.0, 0.5, 0.875, EnumFacing.DOWN,
                        r, g, b, 255, l1, l2,
                        ascroll, true, false
                );
                RenderUtils.renderScrollingQuad(renderer, sprite,
                        0.0, 0.25, 0.0625, 1.0, 0.125, 0.875, EnumFacing.UP,
                        255, 255, 255, 255, l1, l2,
                        scroll, true, false
                );
                if (part == EnumBeltPart.START) {
                    RenderUtils.renderScrollingQuad(renderer, sprite,
                            0.0, 0.25, 0.0625, 1.0, 0.5, 0.875, EnumFacing.WEST,
                            r, g, b, 255, l1, l2,
                            ascroll, false, false
                    );
                    RenderUtils.renderScrollingQuad(renderer, sprite,
                            0.875, 0.25, 0.0625, -0.75, 0.5, 0.875, EnumFacing.EAST,
                            255, 255, 255, 255, l1, l2,
                            ascroll, false, false
                    );
                }
                if (part == EnumBeltPart.END) {
                    RenderUtils.renderScrollingQuad(renderer, sprite,
                            0.0, 0.25, 0.0625, 1.0, 0.5, 0.875, EnumFacing.EAST,
                            r, g, b, 255, l1, l2,
                            scroll, false, false
                    );
                    RenderUtils.renderScrollingQuad(renderer, sprite,
                            0.875, 0.25, 0.0625, -0.75, 0.5, 0.875, EnumFacing.WEST,
                            255, 255, 255, 255, l1, l2,
                            scroll, false, false
                    );
                }
            } else {
                RenderUtils.renderScrollingQuad(renderer, sprite,
                        0.0625, 0.25, 0.0, 0.875, 0.5, 1.0, EnumFacing.UP,
                        r, g, b, 255, l1, l2,
                        ascroll, false, false
                );
                RenderUtils.renderScrollingQuad(renderer, sprite,
                        0.0625, 0.75-0.125, 0.0, 0.875, 0.125, 1.0, EnumFacing.DOWN,
                        255, 255, 255, 255, l1, l2,
                        ascroll, false, false
                );
                RenderUtils.renderScrollingQuad(renderer, sprite,
                        0.0625, 0.25, 0.0, 0.875, 0.5, 1.0, EnumFacing.DOWN,
                        r, g, b, 255, l1, l2,
                        scroll, false, false
                );
                RenderUtils.renderScrollingQuad(renderer, sprite,
                        0.0625, 0.25, 0.0, 0.875, 0.125, 1.0, EnumFacing.UP,
                        255, 255, 255, 255, l1, l2,
                        scroll, false, false
                );
                if (part == EnumBeltPart.START) {
                    RenderUtils.renderScrollingQuad(renderer, sprite,
                            0.0625, 0.25, 0.0, 0.875, 0.5, 1.0, EnumFacing.NORTH,
                            r, g, b, 255, l1, l2,
                            scroll, false, false
                    );
                    RenderUtils.renderScrollingQuad(renderer, sprite,
                            0.0625, 0.25, 0.875, 0.875, 0.5, -0.75, EnumFacing.SOUTH,
                            255, 255, 255, 255, l1, l2,
                            scroll, false, false
                    );
                }
                if (part == EnumBeltPart.END) {
                    RenderUtils.renderScrollingQuad(renderer, sprite,
                            0.0625, 0.25, 0.0, 0.875, 0.5, 1.0, EnumFacing.SOUTH,
                            r, g, b, 255, l1, l2,
                            ascroll, false, false
                    );
                    RenderUtils.renderScrollingQuad(renderer, sprite,
                            0.0625, 0.25, 0.875, 0.875, 0.5, -0.75, EnumFacing.NORTH,
                            255, 255, 255, 255, l1, l2,
                            ascroll, false, false
                    );
                }
            }
        }
        tessellator.draw();
        RenderUtils.finish();
        GlStateManager.enableAlpha();
    }
}
