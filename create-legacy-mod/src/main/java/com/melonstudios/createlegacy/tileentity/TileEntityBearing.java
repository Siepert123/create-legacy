package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityBearing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nullable;

public class TileEntityBearing extends AbstractTileEntityBearing {
    @Override
    protected String namePlate() {
        return "Bearing";
    }

    @Nullable
    @Override
    public ITextComponent getDisplayName() {
        return super.getDisplayName().appendText(" - " + I18n.translateToLocal("tooltip.wip"));
    }

    @Override
    public float consumedStressMarkiplier() {
        return 4.0f;
    }
}
