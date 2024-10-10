package com.melonstudios.createlegacy.fluid;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

import java.awt.*;

public class FluidChocolate extends Fluid {
    public FluidChocolate() {
        super("chocolate", new ResourceLocation("create", "textures/fluid/chocolate"),
                new ResourceLocation("create", "textures/fluid/chocolate_flowing"),
                EnumDyeColor.BROWN.getColorValue());

        setViscosity(500);
        setDensity(2000);

        setUnlocalizedName("fluid.create.chocolate");
    }
}
