package com.melonstudios.createapi.annotation;

import java.lang.annotation.*;

/**
 * Mark code as Spaghetticode
 *
 * @author Siepert123
 * @author moddingforreal
 */
@Documented
@Retention(RetentionPolicy.CLASS) // Discard after loading class
@Target({ElementType.PACKAGE, ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface Spaghetti {
    /**
     * Explain why this code is spaghetti-y
     */
    String value();
}
