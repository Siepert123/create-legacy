package com.melonstudios.createlegacy.objects.item;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.core.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.Arrays;

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
            "propeller", "whisk",
            "electron_tube", "hand_brass", "precision_mechanism",
            "precision_mechanism_incomplete", "plate_obsidian_incomplete"
    };
    private static final int[] TAB_EXCLUDED = new int[]{
        26, 27
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
                if (Arrays.stream(TAB_EXCLUDED).anyMatch(value -> value == finalI)) continue;
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
