package com.siepert.createlegacy.integration;

import com.siepert.createapi.Spaghetti;
import com.siepert.createlegacy.CreateLegacyConfigHolder;
import com.siepert.createlegacy.CreateLegacyModData;
import com.siepert.createlegacy.util.handlers.recipes.MillingRecipes;
import com.siepert.createlegacy.util.handlers.recipes.WashingRecipes;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.ingredients.GTRecipeFluidInput;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;

import java.util.*;

/**
 * Class to contain methods to alter or implement certain recipes if certain mods are installed
 *
 * @author moddingforreal
 * */
@Spaghetti(why = "Looks ugly but I'll comment it at some point")
public class ModIntegrationMachineRecipeChecker {
    public static void addModdedRecipes() {
        if (Loader.isModLoaded("gregtech")) { // Checks if gregtech is loaded & ore washing is enabled
            if (CreateLegacyConfigHolder.IntegrationConfig.gregTechConfig.enableFanOreWashing) {
                for (Recipe recipe : RecipeMaps.ORE_WASHER_RECIPES.getRecipeList()) {
                    if (!getCanRecipeBePerformedGTVoltage(recipe))
                        continue;
                    if (gtWasherRecipeOnlyInputWater(recipe)) {
                        WashingRecipes.instance().addWashingRecipe(
                                recipe.getInputs().get(0).getInputStacks()[0], recipe.getOutputs().get(0));
                    }
                }
            }
            if (CreateLegacyConfigHolder.IntegrationConfig.gregTechConfig.enableMillstoneMacerate) {
                for (Recipe recipe : RecipeMaps.MACERATOR_RECIPES.getRecipeList()) {
                    if (!getCanRecipeBePerformedGTVoltage(recipe))
                        continue;
                    if (gtWasherRecipeOnlyInputWater(recipe)) {
                        MillingRecipes.instance().addMillingRecipe(
                                recipe.getInputs().get(0).getInputStacks()[0], recipe.getOutputs().get(0));
                    }
                }
            }
        }
    }

    private static boolean getCanRecipeBePerformedGTVoltage(Recipe recipe) {
        return recipe.getEUt() <= CreateLegacyConfigHolder.IntegrationConfig.gregTechConfig.gtVoltage;
    }
    private static boolean gtWasherRecipeOnlyInputWater(Recipe recipe) {
        boolean containsWater = false;
        for (GTRecipeInput fluidStack : recipe.getFluidInputs()) {
            if (!(fluidStack instanceof GTRecipeFluidInput)) {
                return false;
            }
            FluidStack fluid = ((GTRecipeFluidInput) fluidStack).getInputFluidStack();
            if (fluid.isFluidEqual(FluidRegistry.getFluidStack("water", 1000))) {
                containsWater = true;
            } else if (fluid.isFluidEqual(ItemStack.EMPTY)) {

            } else {
                return false;
            }
        }
        return true;
    }
}
