package com.melonstudios.createlegacy.tileentity;

public class TileEntityHandCrankRenderer extends AbstractTileEntityKineticRenderer<TileEntityHandCrank> {
    @Override
    public void render(TileEntityHandCrank te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        spinModel(te, x, y, z, partialTicks, te.output().getAxis(), te.getState());
    }
}
