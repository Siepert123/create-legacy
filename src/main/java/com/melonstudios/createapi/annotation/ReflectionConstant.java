package com.melonstudios.createapi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotated fields will generate compile-time warnings about the constants used in reflection
 *
 * @author moddingforreal
 * @since 0.1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE})
public @interface ReflectionConstant {
    /**
     * Whether to suppress the warning
     */
    boolean value() default false;
    String altMsg() default ""; // If non-empty, an alternative message can be printed to the log
}
