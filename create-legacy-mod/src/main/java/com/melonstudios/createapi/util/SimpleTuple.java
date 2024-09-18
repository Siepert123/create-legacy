package com.melonstudios.createapi.util;

/**
 * Simple implementation of a tuple
 *
 * @author moddingforreal
 * @since 0.1.0
 * @param <X> First value
 * @param <Y> Second value
 */
public class SimpleTuple<X, Y> {
    private X x;
    private Y y;
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
    public SimpleTuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @author Siepert123
     */
    public SimpleTuple<X, Y> copy() {
        return new SimpleTuple<>(x, y);
    }
    public static <X, Y> SimpleTuple<X, Y> from(X x, Y y) {
        return new SimpleTuple<>(x, y);
    }
}
