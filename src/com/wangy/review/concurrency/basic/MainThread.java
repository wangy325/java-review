package com.wangy.review.concurrency.basic;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/5/10 / 10:26
 */
public class MainThread {
    public static void main(String[] args) {
        LiftOff liftOff = new LiftOff();
        // no new thread created
        liftOff.run();
    }
}
