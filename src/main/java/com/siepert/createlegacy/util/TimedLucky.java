package com.siepert.createlegacy.util;

public class TimedLucky {
    private long seed;
    public TimedLucky(long seed) {
        this.seed = seed;
    }
    public TimedLucky() {
        this.seed = System.currentTimeMillis();
    }

    public int nextInt(int bound) {
        return (int) (System.currentTimeMillis() + seed) % bound;
    }
}
