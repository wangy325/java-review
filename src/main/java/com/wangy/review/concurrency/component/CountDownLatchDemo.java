package com.wangy.review.concurrency.component;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/11/16 / 16:30
 */
public class CountDownLatchDemo {

    private static class TaskPortion implements Runnable {
        private static int counter = 0;
        private final int id = counter++;
        private static Random rand = new Random(47);
        private final CountDownLatch latch;

        TaskPortion(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                doWork();
            } catch (InterruptedException ex) {
                // Acceptable way to exit
            } finally {
                latch.countDown();
            }
        }

        void doWork() throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(rand.nextInt(2000));
            System.out.println(this + "completed");
        }

        @Override
        public String toString() {
            return String.format("%1$-3d ", id);
        }
    }

    /** Waits on the CountDownLatch: */
    private static class WaitingTask implements Runnable {
        private static int counter = 0;
        private final int id = counter++;
        private final CountDownLatch latch;

        WaitingTask(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                latch.await();
                System.out.println("Latch barrier passed for " + this);
            } catch (InterruptedException ex) {
                System.out.println(this + " interrupted");
            }
        }

        @Override
        public String toString() {
            return String.format("WaitingTask %1$-3d ", id);
        }
    }

    static final int SIZE = 10;

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        // 所有任务都必须使用同一个CountDownLatch对象
        CountDownLatch latch = new CountDownLatch(SIZE);
        exec.execute(new WaitingTask(latch));
        for (int i = 0; i < SIZE; i++) {
            exec.execute(new TaskPortion(latch));
        }
        System.out.println("Launched all tasks");
        exec.shutdown(); // Quit when all tasks complete
    }
}
