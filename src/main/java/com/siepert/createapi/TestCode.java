package com.siepert.createapi;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Siepert123
 * */
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface TestCode {
    String explanation();
}
