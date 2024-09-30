package com.melonstudios.createapi.annotation;

import java.lang.annotation.*;

/**
 * Annotated fields will generate compile-time warnings about the constants used in reflection
 *
 * @author moddingforreal
 * @since 0.1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.LOCAL_VARIABLE})
@Documented
public @interface ReflectionConstant {
    /**
     * The reflection path!!!
     * Options: fully.qualified.path.class          (Class)
     *          fully.qualified.path.class#method() (Method)
     *          fully.qualified.path.class#field    (Field)
     * */
    String value();
    /**
     * Whether to suppress the warning
     */
    boolean suppress() default false;
    String altMsg() default ""; // If non-empty, an alternative message can be printed to the log
}
