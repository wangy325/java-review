package com.wangy.review.concurrency.sync;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/5/13 / 17:04
 */
public class AttemptLocking {
    private Lock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        AttemptLocking al = new AttemptLocking();
        al.untimed();
        al.timed();
        new Thread(){
            {setDaemon(true);}

            @Override
            public void run() {
                al.lock.lock();
                System.out.println("fetched");
            }
        }.start();
        // let thread-0 finish
        Thread.sleep(100);
        al.untimed();
        al.timed();

    }

    void untimed() {
        boolean b = lock.tryLock();
        try {
            System.out.println("tryLock(): " + b);
        } finally {
            if (b) lock.unlock();
        }
    }

    void timed() {
        boolean b = false;
        try {
            b = lock.tryLock(2, TimeUnit.SECONDS);
            System.out.println("tryLock(2, TimeUnit.SECONDS): " + b);
        } catch (InterruptedException e) {
            // e.printStackTrace();
        } finally {
            if (b) lock.unlock();
        }
    }
}
