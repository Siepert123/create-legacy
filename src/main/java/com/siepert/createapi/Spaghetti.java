package com.siepert.createapi;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Code annotated with <code>@Spaghetti</code> should explain the reason for the spaghetticode
 *
 * @author Siepert123
 * */
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface Spaghetti {
    String why();
}
