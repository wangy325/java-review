package com.wangy.review.concurrency.basic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * simple illustration of thread priorities
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/11 / 18:08
 */
public class SimplePrioroites implements Runnable {
    private int countDown = 2;
    private volatile double d;
    private int priority;

    public SimplePrioroites(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return Thread.currentThread() + ": " + countDown;
    }

    @Override
    public void run() {
        Thread.currentThread().setPriority(priority);
        while (true) {
            for (int i = 0; i < 100000; i++) {
                // 耗时操作
                d += (Math.PI + Math.E) / (double) i;
                if (i % 1000 == 0) {
                    Thread.yield();
                }
            }
            System.out.println(this);
            if (--countDown == 0) {
                return;
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 5 ; i++) {
            executorService.execute(new SimplePrioroites(Thread.MIN_PRIORITY));
        }
        executorService.execute(new SimplePrioroites(Thread.MAX_PRIORITY));
        executorService.shutdown();
    }
}
