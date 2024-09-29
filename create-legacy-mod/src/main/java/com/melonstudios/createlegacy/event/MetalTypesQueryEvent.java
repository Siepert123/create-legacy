package com.melonstudios.createlegacy.event;

import net.minecraftforge.fml.common.eventhandler.Event;

import com.melonstudios.createlegacy.recipe.RecipeInit;

import java.util.List;

/**
 * Fires when {@link RecipeInit} needs all metal types to maximize compat.
 * @since 0.1.0
 */
public final class MetalTypesQueryEvent extends Event {
    private final List<String> metals;

    public MetalTypesQueryEvent(List<String> metals) {
        this.metals = metals;
    }

    public void addTypes(String... types) {
        for (String type : types) {
            addType(type);
        }
    }
    public void addType(String type) {
        if (!this.metals.contains(type)) this.metals.add(type);
    }
}
