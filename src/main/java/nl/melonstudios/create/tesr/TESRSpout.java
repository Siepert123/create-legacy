package nl.melonstudios.create.tesr;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import nl.melonstudios.create.tileentity.TileEntitySpout;
import nl.melonstudios.create.util.RenderUtils;
import org.lwjgl.opengl.GL11;

public class TESRSpout extends TESRBase<TileEntitySpout> {
	protected final double[] level = new double[1];
	protected final double[] levels = new double[4];
	public TESRSpout() {

	}

	@Override
	public void render(TileEntitySpout te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		RenderUtils.prepare(x, y, z);

		if (te.tank.getFluid() != null) {
			World world = this.getWorld();
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder renderer = tessellator.getBuffer();
			renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

			FluidStack liquid = te.tank.getFluid();
			this.setLevels(world, liquid, te.hashCode());
			this.renderLiquid(liquid, world, te.getPos(), 0, renderer);

			tessellator.draw();
		}

		RenderUtils.finish();
	}

	private void renderLiquid(FluidStack liquid, World world, BlockPos pos, int i, BufferBuilder renderer) {
		TextureAtlasSprite sprite = this.mc.getTextureMapBlocks().getAtlasSprite(liquid.getFluid().getStill(liquid).toString());
		int brightness = world.getCombinedLight(pos, liquid.getFluid().getLuminosity(liquid));
		int l1 = brightness >> 0x10 & 0xFFFF;
		int l2 = brightness & 0xFFFF;
		int color = liquid.getFluid().getColor(liquid);
		int a = color >> 24 & 0xFF;
		int r = color >> 16 & 0xFF;
		int g = color >> 8 & 0xFF;
		int b = color & 0xFF;
		RenderUtils.renderFluidSurface(renderer, sprite,
				0.1, 0.1, 0.9, 0.9,
				this.levels[i&3], this.levels[i+1&3], this.levels[i+2&3], this.levels[i+3&3],
				r, g, b, a, l1, l2
		);
	}
	private void setLevels(World world, FluidStack fluid, int offset) {
		this.level[0] = fluid.amount * 0.001;
		long time = world.getTotalWorldTime();
		double lvl1 = Math.sin(Math.toRadians((time+offset) % 360))*0.01+this.level[0];
		double lvl2 = Math.sin(Math.toRadians((time+offset+90) % 360))*0.01+this.level[0];
		double lvl3 = Math.sin(Math.toRadians((time+offset+180) % 360))*0.01+this.level[0];
		double lvl4 = Math.sin(Math.toRadians((time+offset+270) % 360))*0.01+this.level[0];
		this.levels[0] = lvl1;
		this.levels[1] = lvl2;
		this.levels[2] = lvl3;
		this.levels[3] = lvl4;
	}
}
