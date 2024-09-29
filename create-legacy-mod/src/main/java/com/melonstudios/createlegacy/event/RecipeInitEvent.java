package com.melonstudios.createlegacy.event;

import net.minecraftforge.fml.common.eventhandler.Event;

import com.melonstudios.createlegacy.recipe.RecipeInit;

/**
 * Fires before and after {@link RecipeInit} is activated.
 *
 * @since 0.1.0
 */
public abstract class RecipeInitEvent extends Event {
    public static class Pre extends RecipeInitEvent {

    }

    public static class Post extends RecipeInitEvent {

    }
}
