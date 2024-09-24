package com.melonstudios.createlegacy.event;

import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Want to change something and make sure it's AFTER or BEFORE the original recipes were implemented?
 * Use this!
 *
 * @since 0.1.0
 */
public abstract class RecipeInitEvent extends Event {
    public static class Pre extends RecipeInitEvent {

    }

    public static class Post extends RecipeInitEvent {

    }
}
