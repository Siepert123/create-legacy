package com.melonstudios.createlegacy.block.kinetic;

import com.melonstudios.createlegacy.tileentity.TileEntityDepot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockDepot extends AbstractBlockKinetic {
    public BlockDepot() {
        super("depot");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityDepot();
    }
}
