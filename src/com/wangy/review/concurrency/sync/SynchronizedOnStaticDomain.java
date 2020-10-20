package com.wangy.review.concurrency.sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <code>synchronized(this)</code>不能保证静态域的同步安全性，须使用<code>SynchronizedOnStaticDomain.class</code>类锁<br>
 * <p>
 * 或其他静态临时锁
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/14 / 22:12
 */
public class SynchronizedOnStaticDomain {
    private static Integer count = 0;
    private final static Lock LOCK = new ReentrantLock();

    static class Cal1 implements Runnable {
        @Override
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
        }

    }

    /** or use this */
    static class Cal2 implements Runnable {
        @Override
        public void run() {
            // 两个锁是等效的
//            synchronized (SynchronizedOnStaticDomain.class) {
            synchronized (LOCK) {
                for (int i = 0; i < 500; i++) {
                    count++;
                    count++;
                }
                System.out.println(Thread.currentThread() + "finished compute: " + count);
            }
        }
    }

    /** or usr atomicInteger */
    static class Cal3 implements Runnable {
        static AtomicInteger ac = new AtomicInteger(count);

        @Override
        public void run() {
            for (int i = 0; i < 500; i++) {
                ac.getAndIncrement();
                ac.getAndIncrement();
            }
            count = ac.get();
        }
    }

    public static void main(String[] args) {

        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++) {
            pool.submit(new Cal2());
        }
        pool.shutdown();
        while (Thread.activeCount() > 2) Thread.yield();

        System.out.println(count);
        System.out.println(Cal3.ac.get());
    }

}
