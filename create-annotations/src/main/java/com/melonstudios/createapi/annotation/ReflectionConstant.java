package com.melonstudios.createapi.annotation;

import java.lang.annotation.*;

/**
 * Annotated fields will generate compile-time warnings about the constants used in reflection
 *
 * @author moddingforreal
 * @since 0.1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE})
@Documented
public @interface ReflectionConstant {
    /**
     * The reflection path!!!
     * */
    String value();
    /**
     * Whether to suppress the warning
     */
    boolean suppress() default false;
    String altMsg() default ""; // If non-empty, an alternative message can be printed to the log
}
