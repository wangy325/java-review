package com.wangy.review.concurrency.component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/11/18 / 01:38
 */
public class SemaphoreDemo {

    private static class AcquireTask<T> implements Runnable {
        private static int counter = 0;
        private final int id = counter++;
        private final Pool<T> pool;

        public AcquireTask(Pool<T> pool) {
            this.pool = pool;
        }

        @Override
        public void run() {
            try {
                T item = pool.checkOut();
                System.out.println(this + " acquire " + item);
            } catch (InterruptedException e) {
                // Acceptable way to terminate
            }
        }

        @Override
        public String toString() {
            return "CheckoutTask-" + id;
        }
    }

    private static class ReleaseTask<T> implements Runnable {
        private static int counter = 0;
        private final int id = counter++;
        private final Pool<T> pool;

        public ReleaseTask(Pool<T> pool) {
            this.pool = pool;
        }

        @Override
        public void run() {
            try {
                List<T> items = pool.items;
                for (T item : items) {
                    pool.checkIn(item);
                }
            } catch (Exception e) {
                // Acceptable way to terminate
            }
        }

        @Override
        public String toString() {
            return "CheckoutTask-" + id + " ";
        }
    }

    private static class Fat {
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

    final static int SIZE = 5;

    private void test() throws InterruptedException {
        final Pool<Fat> pool = new Pool<>(Fat.class, SIZE);
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < SIZE; i++) {
            exec.execute(new AcquireTask<>(pool));
        }
        exec.execute(new ReleaseTask<>(pool));
        List<Fat> list = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            Fat f = pool.checkOut();
            System.out.println(i + ": main() acquire " + f);
            list.add(f);
        }
        Future<?> blocked = exec.submit(() -> {
            try {
                // Semaphore prevents additional checkout,
                // so call is blocked:
                pool.checkOut();
            } catch (InterruptedException e) {
                System.out.println("checkOut() Interrupted");
            }
        });
        TimeUnit.SECONDS.sleep(2);
        blocked.cancel(true); // Break out of blocked call
        // release all items
        pool.checkAllIn();
        for (Fat f : list) {
            pool.checkIn(f); // Second checkIn ignored
        }
        exec.shutdown();
    }

    public static void main(String[] args) throws Exception {
        SemaphoreDemo semaphoreDemo = new SemaphoreDemo();
        semaphoreDemo.test();
    }
}
