package com.siepert.createlegacy.items;

import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.IHasModel;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {

    public ItemBase(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CreateLegacy.TAB_CREATE);

        ModItems.ITEMS.add(this);
    }

    @Override
    public void registerModels() {
        CreateLegacy.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
