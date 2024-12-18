package com.melonstudios.createlegacy.block.kinetic;

import com.melonstudios.createlegacy.tileentity.TileEntityBearing;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityBearing;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockBearing extends AbstractBlockBearing {
    public BlockBearing() {
        super("bearing");
    }

    @Nullable
    @Override
    public AbstractTileEntityBearing createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBearing();
    }
}
