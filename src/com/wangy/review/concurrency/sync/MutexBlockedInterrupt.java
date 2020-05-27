package com.wangy.review.concurrency.sync;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可中断重入锁
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/18 / 12:33
 */
public class MutexBlockedInterrupt {

    private Lock lock = new ReentrantLock();

    public MutexBlockedInterrupt() {
        // lock the instance once constructed
        lock.lock();
    }

    void f() {
        try {
            // invoke can be interrupted
            lock.lockInterruptibly();
            System.out.println("acquire lock in f() success");
        }catch (InterruptedException e){
            System.out.println("Interrupted from acquire lock in f()");
        }
    }

    static class MutexTask implements Runnable{
        MutexBlockedInterrupt mbi = new MutexBlockedInterrupt();
        @Override
        public void run() {
            System.out.println("waiting for f()");
            mbi.f();
            System.out.println("Broken out of blocked call");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new MutexTask());
        t.start();
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println("invoke t.interrupt()");
        t.interrupt();
    }
}
