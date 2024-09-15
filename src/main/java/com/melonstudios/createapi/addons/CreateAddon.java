package com.melonstudios.createapi.addons;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CreateAddon { //TODO: document this
    String modid();

    int createVersion();
    int kineticVersion();

    int loadPriority() default 0;
}
