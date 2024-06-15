package com.siepert.createlegacy.util;

public class TimedLucky {
    private long seed;
    public TimedLucky(long seed) {
        this.seed = seed;
    }
    public TimedLucky() {
        this.seed = System.currentTimeMillis();
    }

    public int nextInt() {
        return nextInt(128);
    }

    public int nextInt(int bound) {
        return (int) (System.currentTimeMillis() + seed) % bound;
    }

    public float nextFloat() {
        return nextFloat(1000);
    }

    public float nextFloat(int precision) {
        return (float) this.nextInt(precision) / precision;
    }
}
