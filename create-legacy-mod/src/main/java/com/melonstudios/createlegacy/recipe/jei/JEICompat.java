package com.melonstudios.createlegacy.recipe.jei;

import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.item.ModItems;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@JEIPlugin
@ParametersAreNonnullByDefault
public final class JEICompat implements IModPlugin {
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        final IJeiHelpers helpers = registry.getJeiHelpers();

        final IGuiHelper gui = helpers.getGuiHelper();

        registry.addRecipeCategories(new PressingRecipeCategory.Implementation(gui));
        registry.addRecipeCategories(new SawingRecipeCategory.Implementation(gui));
        registry.addRecipeCategories(new WashingRecipeCategory.Implementation(gui));
        registry.addRecipeCategories(new SmeltingFanRecipeCategory.Implementation(gui));
        registry.addRecipeCategories(new SandingRecipeCategory.Implementation(gui));
        registry.addRecipeCategories(new MysteriousRecipeCategory.Implementation(gui));
        registry.addRecipeCategories(new MillingRecipeCategory.Implementation(gui));
    }

    @Override
    public void register(IModRegistry registry) {
        final IIngredientRegistry ingredientRegistry = registry.getIngredientRegistry();
        final IJeiHelpers jeiHelpers = registry.getJeiHelpers();

        IRecipeTransferRegistry recipeTransfer = registry.getRecipeTransferRegistry();


        registry.addRecipes(RecipeMaker.getPressingRecipes(jeiHelpers), "create.pressing");
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.PRESS), "create.pressing");

        registry.addRecipes(RecipeMaker.getSawingRecipes(jeiHelpers), "create.sawing");
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.SAW), "create.sawing");

        registry.addRecipes(RecipeMaker.getMillingRecipes(jeiHelpers), "create.milling");
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.MILLSTONE), "create.milling");

        registry.addRecipes(RecipeMaker.getWashingRecipes(jeiHelpers), "create.washing");
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.FAN), "create.washing");
        registry.addRecipeCatalyst(new ItemStack(Items.WATER_BUCKET), "create.washing");

        registry.addRecipes(RecipeMaker.getSmeltingRecipes(jeiHelpers), "create.fan_smelting");
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.FAN), "create.fan_smelting");
        registry.addRecipeCatalyst(new ItemStack(Items.LAVA_BUCKET), "create.fan_smelting");
        registry.addRecipeCatalyst(new ItemStack(Items.FLINT_AND_STEEL), "create.fan_smelting");

        registry.addRecipes(RecipeMaker.getSandingRecipes(jeiHelpers), "create.sanding");
        registry.addRecipeCatalyst(new ItemStack(ModItems.SANDPAPER), "create.sanding");

        registry.addRecipes(RecipeMaker.getMysteriousRecipes(jeiHelpers), "create.mystery");

        registry.addIngredientInfo(new ItemStack(ModItems.FOOD, 1, 1),
                VanillaTypes.ITEM,
                "desc.create.blazecake.1", "desc.create.blazecake.2"
        );
    }
}
