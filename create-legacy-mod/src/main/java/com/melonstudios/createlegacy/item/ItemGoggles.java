package com.melonstudios.createlegacy.item;

import com.melonstudios.createlegacy.CreateLegacy;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemGoggles extends Item {
    public ItemGoggles() {
        setRegistryName("goggles");
        setUnlocalizedName("create.goggles");
        setMaxStackSize(1);

        setCreativeTab(CreateLegacy.TAB_KINETICS);
    }

    @Override
    public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
        return armorType == EntityEquipmentSlot.HEAD;
    }
}
