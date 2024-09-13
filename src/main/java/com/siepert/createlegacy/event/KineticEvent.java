package com.siepert.createlegacy.event;

import com.siepert.createapi.network.NetworkContext;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class KineticEvent extends Event {
    public final NetworkContext context;

    protected KineticEvent(NetworkContext context) {
        this.context = context;
    }

    public static abstract class Tick extends KineticEvent {

        protected Tick(NetworkContext context) {
            super(context);
        }

        public static class Pre extends Tick {
            public Pre(NetworkContext context) {
                super(context);
            }
        }

        public static class Post extends Tick {
            public Post(NetworkContext context) {
                super(context);
            }
        }

    }
}
