package nl.melonstudios.create.tesr.generator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import nl.melonstudios.create.block.BlockRender;
import nl.melonstudios.create.tesr.TESRKineticBase;
import nl.melonstudios.create.tileentity.generator.TileEntityFurnaceFlywheel;
import nl.melonstudios.create.util.EnumRenderPart;
import org.lwjgl.opengl.GL11;

// I lowk forgot GlStateManager was a thing so ignore that i used GL11 for 50% of the time
public class TESRFurnaceFlywheel extends TESRKineticBase<TileEntityFurnaceFlywheel> {
	public TESRFurnaceFlywheel() {

	}

	@Override
	protected void render(TileEntityFurnaceFlywheel te, float pt, float alpha) {
		EnumFacing outputFacing = te.getOutputFacing();
		IBlockState flywheel = BlockRender.byEnum(EnumRenderPart.getFurnaceFlywheel(outputFacing));
		IBakedModel flywheelModel = this.mc.getBlockRendererDispatcher().getModelForState(flywheel);
		this.spinModel(te, pt, outputFacing.getAxis(), flywheelModel, flywheel, 1.0F);
		if (te.engine) {
			this.renderFurnaceHat(outputFacing.rotateYCCW());
			this.renderPokeyArms(te, outputFacing.getOpposite(), pt);
		}
	}

	private void renderFurnaceHat(EnumFacing facing) {
		GL11.glPushMatrix();
		GL11.glTranslatef(facing.getFrontOffsetX() * 3.0F, 0.0F, facing.getFrontOffsetZ() * 3.0F);
		IBlockState state = BlockRender.byEnum(EnumRenderPart.FURNACE_HAT);
		IBakedModel model = this.mc.getBlockRendererDispatcher().getModelForState(state);
		this.renderBakedModel(1.0F, model, state);
		GL11.glPopMatrix();
	}
	private void renderPokeyArms(TileEntityFurnaceFlywheel te, EnumFacing facing, float pt) {
		float angle = this.calculateAngle(te, te.getOutputFacing().getAxis(), pt, facing.getAxisDirection().getOffset(), false);

		GlStateManager.pushMatrix();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		switch (facing) {
			case NORTH:
				GlStateManager.rotate(90, 0.0F, 1.0F, 0.0F);
				break;
			case SOUTH:
				GlStateManager.rotate(-90, 0.0F, 1.0F, 0.0F);
				break;
			case WEST:
				GlStateManager.rotate(180, 0.0F, 1.0F, 0.0F);
				break;
		}
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		GlStateManager.pushMatrix();
		this.transformConnector(false, true, angle);
		this.modelRender(BlockRender.byEnum(EnumRenderPart.LOWER_ROTATING_CONNECTOR));
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		this.transformConnector(false, false, angle);
		this.modelRender(BlockRender.byEnum(EnumRenderPart.LOWER_SLIDING_CONNECTOR));
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		this.transformConnector(true, true, angle);
		this.modelRender(BlockRender.byEnum(EnumRenderPart.UPPER_ROTATING_CONNECTOR));
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		this.transformConnector(true, false, angle);
		this.modelRender(BlockRender.byEnum(EnumRenderPart.UPPER_SLIDING_CONNECTOR));
		GlStateManager.popMatrix();

		GlStateManager.popMatrix();
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
		IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
		this.renderBakedModel(1.0F, model, state);
	}
}
