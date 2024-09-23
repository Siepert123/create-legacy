package com.melonstudios.createapi.util;

/**
 * Simple implementation of a triple
 *
 * @author moddingforreal
 * @since 0.1.0
 * @param <X> First value
 * @param <Y> Second value
 */
public class SimpleTriple<X, Y, Z> {
    private X x;
    private Y y;
    private Z z;
    public X getX() {
        return x;
    }
    public void setX(X x) {
        this.x = x;
    }
    public Y getY() {
        return y;
    }
    public void setY(Y y) {
        this.y = y;
    }
    public Z getZ() {
        return z;
    }
    public void setZ(Z z) {
        this.z = z;
    }
    public SimpleTriple(X x, Y y, Z z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    /**
     * Creates a new copy of this SimpleTriple
     *
     * @author Siepert123
     * @author moddingforreal
     * */
    public SimpleTriple<X, Y, Z> copy() {
        return new SimpleTriple<>(x, y, z);
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
