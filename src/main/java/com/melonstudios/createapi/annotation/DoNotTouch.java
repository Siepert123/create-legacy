package com.melonstudios.createapi.annotation;

import java.lang.annotation.*;

/**
 *
 *
 * @author Siepert123
 * @author moddingforreal
 * */
@Documented
@Target({ElementType.PACKAGE, ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DoNotTouch {
    String value() default "Important code, refer to developer";
}
