package com.siepert.createlegacy.mainRegistry;

import com.siepert.createlegacy.items.*;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

/** The main item class.
 * Gets registered before the blocks.
 * I think no additional explanation is needed here.*/
public class ModItems {
    public static final List<Item> ITEMS = new ArrayList<Item>();

    public static final Item WRENCH = new ItemWrench();
    public static final Item INGREDIENT = new ItemIngredient();
    public static final Item ADVANCED_INGREDIENT = new ItemIngredientAdvanced();
    public static final Item COMPAT_INGREDIENT = new ItemIngredientCompat();
    public static final Item INCOMPLETE_ITEM = new ItemIncomplete();
    public static final Item SANDPAPER = new ItemSandpaper();
    public static final Item SCRUMPTIOUS_FOOD = new ItemScrumptiousFood();
}
