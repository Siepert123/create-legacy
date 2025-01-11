package com.melonstudios.createlegacy.block.kinetic;

import com.melonstudios.createapi.CreateAPI;
import com.melonstudios.createlegacy.tileentity.TileEntityBearing;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityBearing;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockBearing extends AbstractBlockBearing {
    public BlockBearing() {
        super("bearing");
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(CreateAPI.stressImpactTooltip(4));
    }

    @Nullable
    @Override
    public AbstractTileEntityBearing createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBearing();
    }
}
