package com.melonstudios.createlegacy.item;

import com.melonstudios.createlegacy.CreateLegacy;
import epicsquid.mysticallib.item.ItemFoodBase;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemScrumptious extends ItemFood {

    public ItemScrumptious() {
        super(0, 0, false);

        setRegistryName("food");
        setUnlocalizedName("create.food");

        setCreativeTab(CreateLegacy.TAB_KINETICS);
    }

    private static final float[] saturation = {
            0.2f, 0, 0, 0, 2f, 2.3f, 0, 1.75f, 0
    };
    private static final int[] heal = {
            2, 0, 0, 0, 3, 4, 0, 3, 0
    };
    private static final boolean[] eatable = {
            true, false, false, false, true, true, false, true, false
    };

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            items.add(new ItemStack(this, 1, 0));
            items.add(new ItemStack(this, 1, 1));
            items.add(new ItemStack(this, 1, 2));
            items.add(new ItemStack(this, 1, 3));
            items.add(new ItemStack(this, 1, 4));
            items.add(new ItemStack(this, 1, 5));
            items.add(new ItemStack(this, 1, 6));
            items.add(new ItemStack(this, 1, 7));
            items.add(new ItemStack(this, 1, 8));
        }
    }

    private void _setItemModels() {
        CreateLegacy.setItemModel(this, 0, "food/apple_honey");
        CreateLegacy.setItemModel(this, 1, "food/blazecake");
        CreateLegacy.setItemModel(this, 2, "food/blazecake_base");
        CreateLegacy.setItemModel(this, 3, "food/blazecake_creative");
        CreateLegacy.setItemModel(this, 4, "food/chocolate_berries");
        CreateLegacy.setItemModel(this, 5, "food/chocolate_ingot");
        CreateLegacy.setItemModel(this, 6, "food/dough");
        CreateLegacy.setItemModel(this, 7, "food/sweet_roll");
        CreateLegacy.setItemModel(this, 8, "food/wheat_flour");
    }
    public static void setItemModels() {
        ModItems.FOOD._setItemModels();
    }

    @Override
    public float getSaturationModifier(ItemStack stack) {
        return saturation[stack.getMetadata()];
    }

    @Override
    public int getHealAmount(ItemStack stack) {
        return heal[stack.getMetadata()];
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return eatable[stack.getMetadata()] ?EnumAction.EAT : EnumAction.NONE;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        String base = "item.create.food.";
        switch (stack.getMetadata()) {
            case 0: return base + "apple_honey";
            case 1: return base + "blazecake";
            case 2: return base + "blazecake_base";
            case 3: return base + "blazecake_creative";
            case 4: return base + "chocolate_berries";
            case 5: return base + "chocolate_ingot";
            case 6: return base + "dough";
            case 7: return base + "sweet_roll";
            case 8: return base + "wheat_flour";
            default: return "tooltip.wip";
        }
    }
}
