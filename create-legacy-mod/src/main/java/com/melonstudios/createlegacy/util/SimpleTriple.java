package com.melonstudios.createlegacy.util;

import com.melonstudios.melonlib.math.ITriple;

/**
 * Simple implementation of a triple
 *
 * @author moddingforreal
 * @since 0.1.0
 * @param <X> First value
 * @param <Y> Second value
 * @param <Z> Third value
 */
public class SimpleTriple<X, Y, Z> implements ITriple<X, Y, Z> {
    private X value1;
    private Y value2;
    private Z value3;
    public X getValue1() {
        return value1;
    }
    public void setValue1(X value1) {
        this.value1 = value1;
    }
    public Y getValue2() {
        return value2;
    }
    public void setValue2(Y value2) {
        this.value2 = value2;
    }
    public Z getValue3() {
        return value3;
    }
    public void setValue3(Z value3) {
        this.value3 = value3;
    }
    public SimpleTriple(X value1, Y value2, Z value3) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
    }
    /**
     * Creates a new copy of this SimpleTriple
     *
     * @author Siepert123
     * @author moddingforreal
     * */
    public SimpleTriple<X, Y, Z> copy() {
        return new SimpleTriple<>(value1, value2, value3);
    }
    /**
     * Makes a new SimpleTriple from the parameters
     *
     * @author Siepert123
     * @author moddingforreal
     * */
    public static <X, Y, Z> SimpleTriple<X, Y, Z> from(X x, Y y, Z z) {
        return new SimpleTriple<>(x, y, z);
    }
}
