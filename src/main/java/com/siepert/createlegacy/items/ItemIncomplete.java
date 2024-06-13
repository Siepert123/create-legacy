package com.siepert.createlegacy.items;

import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createlegacy.util.IMetaName;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public final class ItemIncomplete extends Item implements IHasModel, IMetaName {
    public static final int INGREDIENT_AMOUNT = 2   ;
    public static final String[] NAME_LOOKUP = new String[] {
            "precision_mechanism", "plate_obsidian"
    };
    public ItemIncomplete() {
        setUnlocalizedName("create:incomplete_");
        setRegistryName("incomplete_item");
        setCreativeTab(CreateLegacy.TAB_CREATE);
        setHasSubtypes(true);
        setMaxDamage(0);

        ModItems.ITEMS.add(this);
    }

    @Override
    public void registerModels() {
        String name;
        for (int i = 0; i < INGREDIENT_AMOUNT; i++) {
            name = NAME_LOOKUP[i];
            CreateLegacy.proxy.registerVariantRenderer(this, i, "incomplete/" + name, "inventory");
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
            CreateLegacy.logger.error("Evil item has been made!");
            CreateLegacy.logger.error(e);
        }
        return "evil_item";
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (tab == CreativeTabs.SEARCH) {
            for (int i = 0; i < INGREDIENT_AMOUNT; i++) {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }
}
