package com.siepert.createlegacy.items;

import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createlegacy.util.IMetaName;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public final class ItemIngredientCompat extends Item implements IHasModel, IMetaName {
    public static final int COMPAT_INGREDIENT_AMOUNT = 9;
    public static final boolean ADD_TO_TAB = false;
    public static final String[] NAME_LOOKUP = new String[] {
            "crushed_aluminium", "crushed_lead", "crushed_nickel",
            "crushed_osmium", "crushed_platinum", "crushed_quicksilver",
            "crushed_silver", "crushed_tin", "crushed_uranium"
    };
    public ItemIngredientCompat() {
        setUnlocalizedName("create:");
        setRegistryName("compat_ingredient");
        setHasSubtypes(true);
        setMaxDamage(0);

        ModItems.ITEMS.add(this);
    }

    @Override
    public void registerModels() {
        String name;
        for (int i = 0; i < COMPAT_INGREDIENT_AMOUNT; i++) {
            name = NAME_LOOKUP[i];
            CreateLegacy.proxy.registerVariantRenderer(this, i, "compat/" + name, "inventory");
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + ((IMetaName)this).getSpecialName(stack);
    }

    @Override
    public String getSpecialName(ItemStack stack) {
        try {
            return NAME_LOOKUP[stack.getItemDamage()];
        } catch (ArrayIndexOutOfBoundsException e) {
            //H
        }
        return "evil_item";
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (ADD_TO_TAB || tab == CreativeTabs.SEARCH) {
            for (int i = 0; i < COMPAT_INGREDIENT_AMOUNT; i++) {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }
}
