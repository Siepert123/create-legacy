package com.melonstudios.createlegacy.objects.item;

import com.melonstudios.createlegacy.CreateLegacy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemIngredient extends Item {
    public ItemIngredient() {
        setRegistryName("ingredient");
        setUnlocalizedName("create.ingredient");

        setHasSubtypes(true);
        setMaxDamage(0);

        setCreativeTab(CreateLegacy.TAB_KINETICS);
    }

    private static final String[] VARIANTS = new String[]{

    };

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if (stack.getMetadata() > VARIANTS.length) return "item.create.ingredient";
        return "item.create." + VARIANTS[stack.getMetadata()];
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            //appendation goes here
        }
    }
}
