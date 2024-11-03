package com.melonstudios.createlegacy.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidChocolate extends Fluid {
    public static final int color = 8606770;
    public FluidChocolate() {
        super("chocolate", new ResourceLocation("create", "fluid/chocolate_still"),
                new ResourceLocation("create", "fluid/chocolate_flow"),
                color);

        setViscosity(500);
        setDensity(2000);

        setUnlocalizedName("create.chocolate");
    }
}
