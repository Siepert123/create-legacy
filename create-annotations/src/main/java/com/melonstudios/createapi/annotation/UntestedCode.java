package com.melonstudios.createapi.annotation;

import java.lang.annotation.*;

/**
 * Untested or test code can be marked with this
 *
 * @author Siepert123
 * @author moddingforreal
 * */
@Target({ElementType.PACKAGE, ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD})
@Documented
@Inherited
@Retention(RetentionPolicy.CLASS) // Discard after loading class
public @interface UntestedCode {
    /**
     * Explain the test code in short, 1 sentence or so should be enough
     * */
    String value() default "Test code - potentially inherited";
    /**
     * Optionally give a reason
     * */
    String why() default "";
}
