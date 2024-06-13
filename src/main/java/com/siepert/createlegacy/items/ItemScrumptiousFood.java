package com.siepert.createlegacy.items;

import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createlegacy.util.IMetaName;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import java.util.HashMap;

public final class ItemScrumptiousFood extends ItemFood implements IHasModel, IMetaName {
    public static final String[] NAME_LOOKUP = new String[] {
            "wheat_flour", "dough", "chocolate_ingot", "apple_honey", "sweet_roll",
            "chocolate_berries", "blazecake_base", "blazecake",
            "blazecake_creative"
    };
    public static final int FOOD_COUNT = NAME_LOOKUP.length;

    public static final HashMap<Integer, Integer> food_heal_values = new HashMap<>(); //Set to -1 if inedible!
    public static final HashMap<Integer, Float> food_saturation_values = new HashMap<>();

    static {
        food_heal_values.put(0, -1);
        food_heal_values.put(1, -1);
        food_heal_values.put(2, 8);
        food_heal_values.put(3, 6);
        food_heal_values.put(4, 6);
        food_heal_values.put(5, 4);
        food_heal_values.put(6, -1);
        food_heal_values.put(7, -1);
        food_heal_values.put(8, -1);

        food_saturation_values.put(2, 1.2f);
        food_saturation_values.put(3, 1.0f);
        food_saturation_values.put(4, 0.6f);
        food_saturation_values.put(5, 1.2f);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        if (food_heal_values.get(stack.getItemDamage()) == -1) {
            return EnumAction.NONE;
        }
        return EnumAction.EAT;
    }

    @Override
    public int getHealAmount(ItemStack stack) {
        int anInt = food_heal_values.getOrDefault(stack.getItemDamage(), -1);
        if (anInt != -1) {
            return anInt;
        }
        return 0;
    }

    @Override
    public float getSaturationModifier(ItemStack stack) {
        return food_saturation_values.getOrDefault(stack.getItemDamage(), 0.0f);
    }

    @Override
    public ItemFood setPotionEffect(PotionEffect effect, float probability) {
        return super.setPotionEffect(effect, probability);
    }

    public ItemScrumptiousFood() {
        super(4, 4, false);
        setUnlocalizedName("create:");
        setRegistryName("scrumptious_item");
        setCreativeTab(CreateLegacy.TAB_CREATE);
        setHasSubtypes(true);
        setMaxDamage(0);

        ModItems.ITEMS.add(this);
    }

    @Override
    public void registerModels() {
        String name;
        for (int i = 0; i < FOOD_COUNT; i++) {
            name = NAME_LOOKUP[i];
            CreateLegacy.proxy.registerVariantRenderer(this, i, "food/" + name, "inventory");
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + ((IMetaName)this).getSpecialName(stack);
    }

    @Override
    public String getSpecialName(ItemStack stack) {
        return NAME_LOOKUP[stack.getItemDamage()];
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (tab == CreateLegacy.TAB_CREATE || tab == CreativeTabs.SEARCH) {
            for (int i = 0; i < FOOD_COUNT; i++) {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }
}
