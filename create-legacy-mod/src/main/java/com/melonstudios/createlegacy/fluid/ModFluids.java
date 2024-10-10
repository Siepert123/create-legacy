package com.melonstudios.createlegacy.fluid;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class ModFluids {
    protected static FluidChocolate chocolate; //yummy
    public static FluidChocolate chocolate() {
        return chocolate;
    }
    public static final MaterialLiquid CHOCOLATE = new MaterialLiquid(MapColor.BROWN);

    static {
        setupFluids();
    }

    public static void setupFluids() {
        FluidRegistry.enableUniversalBucket();

        chocolate = registerFluid(new FluidChocolate());
    }

    protected static <T extends Fluid> T registerFluid(T fluid) {
        FluidRegistry.registerFluid(fluid);
        return fluid;
    }
}
