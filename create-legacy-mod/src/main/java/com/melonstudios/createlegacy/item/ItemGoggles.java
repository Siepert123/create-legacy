package com.melonstudios.createlegacy.item;

import com.melonstudios.createlegacy.CreateLegacy;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ItemGoggles extends ItemArmor {
    public ItemGoggles() {
        super(ArmorMaterial.GOLD, 1, EntityEquipmentSlot.HEAD);
        setRegistryName("goggles");
        setUnlocalizedName("create.goggles");
        setMaxStackSize(1);
        setMaxDamage(0);

        setCreativeTab(CreateLegacy.TAB_KINETICS);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return "create:textures/armor/goggles.png";
    }
}
