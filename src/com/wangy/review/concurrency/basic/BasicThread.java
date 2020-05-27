package com.wangy.review.concurrency.basic;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/5/10 / 13:36
 */
class BasicThread {
    public static void main(String[] args) {
//        single();
        multi();
    }

    private static void single() {
        Thread t = new Thread(new LiftOff());
        t.start();
        System.out.println("waiting for liftoff");
    }

    static void multi() {
        for (int i = 0; i < 5; i++) {
            new Thread(new LiftOff()).start();
        }
        System.out.println("waiting for liftoff");
    }
}
