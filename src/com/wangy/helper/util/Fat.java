package com.wangy.helper.util;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/11/19 / 14:20
 */
public class Fat {
    private volatile double d; // Prevent optimization
    private static int counter = 0;
    private final int id = counter++;

    public Fat() {
        // Expensive, interruptible operation:
        for (int i = 1; i < 10000; i++) {
            d += (Math.PI + Math.E) / (double) i;
        }
    }

    @Override
    public String toString() {
        return "Fat-" + id;
    }
}
