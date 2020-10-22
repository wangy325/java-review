package com.wangy.review.concurrency.sync;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可中断重入锁
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/18 / 12:33
 */
public class LockingInterrupt {

    // 可重入锁获取锁的时候可以被中断
    private Lock lock = new ReentrantLock();

    public LockingInterrupt() {
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
        LockingInterrupt mbi = new LockingInterrupt();
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
        // 中断t，若不中断，t会一直阻塞
        t.interrupt();
    }
}
