package com.siepert.createapi.util;

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
}
