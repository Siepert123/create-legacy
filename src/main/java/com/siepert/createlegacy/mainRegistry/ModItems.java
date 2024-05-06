package com.siepert.createlegacy.mainRegistry;

import com.siepert.createlegacy.items.*;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ModItems {
    public static final List<Item> ITEMS = new ArrayList<Item>();

    public static final Item INGREDIENT = new ItemIngredient();
    public static final Item ADVANCED_INGREDIENT = new ItemIngredientAdvanced();
    public static final Item COMPAT_INGREDIENT = new ItemIngredientCompat();
    public static final Item INCOMPLETE_ITEM = new ItemIncomplete();
    public static final Item SANDPAPER = new ItemSandpaper();
}
