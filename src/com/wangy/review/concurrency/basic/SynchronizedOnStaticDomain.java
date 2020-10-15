package com.wangy.review.concurrency.basic;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/5/14 / 22:12
 */
public class SynchronizedOnStaticDomain extends Thread {
    private static Integer count = 0;
    private final static Lock LOCK = new ReentrantLock();

    public SynchronizedOnStaticDomain() {
        start();
    }

    /*@Override
    public void run() {
        LOCK.lock();
        try {
            for (int i = 0; i < 1000; i++) {
                count++;
            }
            System.out.println(Thread.currentThread() + "finished compute: " + count);
        } finally {
            LOCK.unlock();
        }
    }*/

    /** or use this */
    @Override
    public void run() {
        synchronized (LOCK){
            for (int i = 0; i < 500; i++) {
                count++;
                count++;
            }
            System.out.println(Thread.currentThread() + "finished compute: " + count);
        }
    }

    /** or usr atomicInteger*/
/*    @Override
    public void run() {
            for (int i = 0; i < 500; i++) {
                count.getAndIncrement();
                count.getAndIncrement();
            }
            // not all out put count are even!
            System.out.println(Thread.currentThread() + "finished compute: " + count);
    }*/

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new SynchronizedOnStaticDomain();
        }

    }

}
