package com.melonstudios.createlegacy.item;

import com.melonstudios.createlegacy.CreateLegacy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.Arrays;
import java.util.Objects;

public class ItemIngredient extends Item {
    public ItemIngredient() {
        setRegistryName("ingredient");
        setUnlocalizedName("create.ingredient");

        setHasSubtypes(true);
        setMaxDamage(0);

        setCreativeTab(CreateLegacy.TAB_KINETICS);
    }

    private static final String[] VARIANTS = new String[]{
        "andesite_alloy", "plate_iron", "plate_gold",
            "ingot_copper", "nugget_copper", "plate_copper",
            "ingot_zinc", "nugget_zinc", "plate_zinc",
            "ingot_brass", "nugget_brass", "plate_brass",
            "crushed_iron", "crushed_gold", "crushed_copper", "crushed_zinc",
            "dust_netherrack", "dust_obsidian", "plate_obsidian",
            "rose_quartz", "rose_quartz_polished",
            "ingot_radiant", "ingot_shadow",
            "propeller", "whisk",
            "electron_tube", "hand_brass", "precision_mechanism",
            "precision_mechanism_incomplete", "plate_obsidian_incomplete"
    };
    private static final String[] TAB_EXCLUDED = new String[]{
        "", "", "", "",
    };

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if (stack.getMetadata() > VARIANTS.length) return "item.create.ingredient";
        return "item.create." + VARIANTS[stack.getMetadata()];
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            for (int i = 0; i < VARIANTS.length; i++) {
                final int finalI = i;
                if (Arrays.stream(TAB_EXCLUDED).anyMatch(value -> Objects.equals(value, VARIANTS[finalI]))
                        && tab != CreativeTabs.SEARCH) continue;
                items.add(new ItemStack(this, 1, i));
            }
        }
    }

    public static void setItemModels() {
        for (int i = 0; i < VARIANTS.length; i++) {
            CreateLegacy.proxy.setItemModel(ModItems.INGREDIENT, i, "ingredient/" + VARIANTS[i]);
        }
    }
}
