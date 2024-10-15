package com.melonstudios.createlegacy.copycat;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.block.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraftforge.common.property.IUnlistedProperty;

public interface ICopycatBlock {
    IUnlistedProperty<IBlockState> getStateProperty();

    public static void setItemModels() {
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.COPYCAT_PANEL));
    }
}
