package com.melonstudios.createapi.annotation;

import java.lang.annotation.*;

/**
 * Marks pieces of code as incomplete or unfinished
 *
 * @author moddingforreal
 * @since 0.1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PACKAGE, ElementType.METHOD})
@Documented
public @interface Incomplete {
    /**
     * Explanation of what is missing and why
     */
    String value();
}
