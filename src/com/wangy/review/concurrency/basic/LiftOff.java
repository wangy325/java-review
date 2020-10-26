package com.wangy.review.concurrency.basic;

import java.util.concurrent.TimeUnit;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/5/10 / 13:34
 */
public class LiftOff implements Runnable {
    private static int taskCount = 0;
    protected int countDown = 10;
    private final int id = taskCount++;

    public LiftOff() {
    }

    public LiftOff(int countDown) {
        this.countDown = countDown;
    }

    public String status() {
        return "#" + id + "(" + (countDown > 0 ? countDown : "LiftOff!") + "), ";
    }

    @Override
    public void run() {
//        Thread.currentThread().interrupt();
        while (countDown-- > 0) {
            try {
                System.out.println(status());
                Thread.yield();
//                TimeUnit.NANOSECONDS.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
